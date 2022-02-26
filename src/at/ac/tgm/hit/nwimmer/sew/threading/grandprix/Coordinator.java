package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RoundCompletedMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RunnerReadyMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.Runner;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.RunnerEventManager;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.EventDispatcher;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.SleepTimeManager;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.SleepTimeProvider;

import java.util.concurrent.CountDownLatch;

public class Coordinator {

    public static final int runnerCount = 3;
    public static final int roundCount = 3;

    private final MessageBroker broker;

    private final CountDownLatch startSignalLatch;
    private int runnersFinished;

    public Coordinator() {
        this.broker = new MessageBroker(runnerCount);
        this.startSignalLatch = new CountDownLatch(1);

        this.setupRunners();

        System.out.println("Die Läufer sind bereit...");
    }

    private void setupRunners() {
        final SleepTimeProvider sleepTimeProvider = new SleepTimeManager();
        final EventDispatcher updateDispatcher = new RunnerEventManager(this.broker);

        for (int i = 0; i < runnerCount; i++) {
            final Runner runner = new Runner(this.startSignalLatch, updateDispatcher, sleepTimeProvider);
            final Thread thread = new Thread(runner);

            thread.start();
        }
    }

    public void startCompetition() {
        System.out.println("Start!");

        this.startSignalLatch.countDown();

        while (runnersFinished < runnerCount) {
            try {
                final Message message = this.broker.take();

                switch (message) {
                    case RunnerReadyMessage readyMsg -> System.out.println("Thread %s is ready!".formatted(readyMsg.runnerName()));
                    case RoundCompletedMessage roundCompletedMsg -> {
                        String output = "Thread %s hat Runde %s nach %sms abgeschlossen!".formatted(
                                roundCompletedMsg.runnerName(),
                                roundCompletedMsg.roundNumber(),
                                roundCompletedMsg.roundTime());

                        if (roundCompletedMsg.roundNumber() == roundCount) {
                            output += " Platz %s!".formatted(++runnersFinished);
                        }

                        System.out.println(output);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
