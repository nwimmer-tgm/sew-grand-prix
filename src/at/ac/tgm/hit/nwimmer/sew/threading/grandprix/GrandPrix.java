package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageDistributionService;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageDistributor;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.GrandPrixStartMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.Runner;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.RunnerStateManager;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.SleepTimeManager;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.SleepTimeProvider;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * @author Niklas Wimmer
 * @since 2022-02-24
 */
public class GrandPrix {

    public static final int runnerCount = 3;
    public static final int roundCount = 3;

    private final MessageBroker broker;
    private final MessageDistributionService distributor;

    private final CountDownLatch startRunnersLatch;

    private boolean ready;

    public GrandPrix() {
        this.broker = new MessageBroker(runnerCount);
        this.distributor = new MessageDistributionService(this.broker);

        this.startRunnersLatch = new CountDownLatch(1);

        this.ready = false;
    }

    public MessageDistributor prepare() {
        this.prepareRunners();
        this.prepareMessageDistributor();

        this.ready = true;

        return this.distributor;
    }

    private void prepareRunners() {
        final RunnerStateManager runnerStateManager = new RunnerStateManager(this.broker);
        final SleepTimeProvider sleepTimeProvider = new SleepTimeManager(runnerStateManager);

        for (int i = 0; i < runnerCount; i++) {
            final Runner runner = new Runner(this.startRunnersLatch, runnerStateManager, sleepTimeProvider);
            final Thread thread = new Thread(runner, "Runner-" + i);

            thread.start();
        }
    }

    private void prepareMessageDistributor() {
        final Thread t = new Thread(this.distributor, "Message Distributor");
        t.start();
    }

    public void start() {
        if (!this.ready) {
            throw new IllegalStateException("The Coordinator needs to be prepared before it can be started!");
        }

        this.distributor.start();

        try {
            this.broker.put(new GrandPrixStartMessage(LocalDateTime.now(), runnerCount, roundCount));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        this.startRunnersLatch.countDown();
    }
}
