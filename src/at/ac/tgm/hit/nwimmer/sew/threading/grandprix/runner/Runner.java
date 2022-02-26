package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.Coordinator;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.SleepTimeProvider;

import java.util.concurrent.CountDownLatch;

/**
 * A runner represents an entity in the Grand Prix which executes certain tasks to finish rounds and eventually win the
 * race.
 *
 * @author Niklas Wimmer
 * @since 2022-02-24
 */
public class Runner implements Runnable {

    private final CountDownLatch latch;
    private final EventDispatcher updateDispatcher;
    private final SleepTimeProvider sleepTimeProvider;

    public Runner(final CountDownLatch latch,
                  final EventDispatcher updateDispatcher,
                  final SleepTimeProvider sleepTimeProvider) {
        this.latch = latch;
        this.updateDispatcher = updateDispatcher;
        this.sleepTimeProvider = sleepTimeProvider;
    }

    @Override
    public void run() {
        final String name = Thread.currentThread().getName();

        try {
            latch.await();

            updateDispatcher.dispatch(name, new RunnerEvent(RunnerEvent.EventType.READY, 0, -1));

            for (int currentRound = 1; currentRound <= Coordinator.roundCount; ++currentRound) {
                final int sleepTime = this.sleepTimeProvider.retrieveNextSleepTime(name);

                final long time = System.currentTimeMillis();
                Thread.sleep(sleepTime);
                updateDispatcher.dispatch(name, new RunnerEvent(RunnerEvent.EventType.ROUND_COMPLETED, currentRound, System.currentTimeMillis() - time));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
