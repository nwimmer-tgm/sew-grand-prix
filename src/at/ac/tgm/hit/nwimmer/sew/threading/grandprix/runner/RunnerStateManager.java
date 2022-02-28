package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.GrandPrix;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageConsumer;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.GrandPrixEndMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RoundCompletedMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RunnerFinishedMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RunnerReadyMessage;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the round the runner currently is in, acting directly on the received {@link RunnerEvent RunnerEvents}.
 *
 * Uses an {@link MessageConsumer} to dispatch received events to the front end.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class RunnerStateManager implements EventDispatcher, RunnerRoundRetriever {

    private final MessageConsumer messageConsumer;

    /**
     * Needs to be concurrent, as multiple runners could potentially dispatch at the same time.
     * The AtomicIntegers would not be needed necessarily, as every runner only distributes events about itself. This
     * means that we will never process two events at the same time targeting the same runner. But they are still
     * convenient to update the integer value in the map.
     */
    private final ConcurrentMap<String, AtomicInteger> runnerToCurrentRound;
    /**
     * Needs to be an AtomicInteger as multiple threads could access and update its value at the same time.
     */
    private final AtomicInteger runnersFinished;

    public RunnerStateManager(final MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;

        this.runnerToCurrentRound = new ConcurrentHashMap<>(GrandPrix.runnerCount);
        this.runnersFinished = new AtomicInteger(0);
    }

    @Override
    public void dispatch(String runner, RunnerEvent update) throws InterruptedException {
        switch (update.type()) {
            case READY -> this.handleRunnerReadyEvent(runner);
            case ROUND_COMPLETED -> this.handleRunnerRoundCompleteEvent(runner, update.timeBetweenUpdates());
        }
    }

    @Override
    public int getCurrentRound(String runnerName) {
        final AtomicInteger currentRound = this.runnerToCurrentRound.get(runnerName);

        return currentRound == null ? 0 : currentRound.intValue();
    }

    private void handleRunnerReadyEvent(final String runnerName) throws InterruptedException {
        final AtomicInteger round = new AtomicInteger(1);
        final boolean alreadyPresent = this.runnerToCurrentRound.putIfAbsent(runnerName, round) != null;

        if (alreadyPresent) {
            // the runner already notified us that it is ready, ignore it now
            return;
        }

        this.messageConsumer.put(new RunnerReadyMessage(runnerName));
    }

    private void handleRunnerRoundCompleteEvent(final String runnerName, final long timeBetweenUpdates) throws InterruptedException {
        final AtomicInteger currentRound = this.runnerToCurrentRound.get(runnerName);

        if (currentRound == null) {
            // the runner never notified us that it is ready, ignore it
            return;
        }

        this.messageConsumer.put(new RoundCompletedMessage(runnerName, currentRound.intValue(), timeBetweenUpdates));

        if (currentRound.incrementAndGet() > GrandPrix.roundCount) {
            final int currentRunnersFinished = this.runnersFinished.incrementAndGet();

            this.messageConsumer.put(new RunnerFinishedMessage(runnerName, currentRunnersFinished));

            if (currentRunnersFinished == GrandPrix.runnerCount) {
                this.messageConsumer.put(new GrandPrixEndMessage(LocalDateTime.now()));
            }
        }
    }
}
