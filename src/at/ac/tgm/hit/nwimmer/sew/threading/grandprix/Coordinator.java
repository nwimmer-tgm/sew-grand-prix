package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class Coordinator implements UpdateProcessor {

    private static final int runnerCount = 3;
    private static final int roundCount = 3;

    private final BlockingQueue<UpdateInformation> updateQueue;
    private final CountDownLatch startSignalLatch;
    private int runnersFinished;

    public Coordinator() {
        this.updateQueue = new ArrayBlockingQueue<>(runnerCount);
        this.startSignalLatch = new CountDownLatch(1);

        for (int i = 0; i < runnerCount; i++) {
            final Runner runner = new Runner(this.startSignalLatch, this);
            final Thread thread = new Thread(runner);

            thread.start();
        }

        System.out.println("Die Läufer sind bereit...");
    }

    public void startCompetition() {
        System.out.println("Start!");

        this.startSignalLatch.countDown();

        while (runnersFinished < runnerCount) {
            try {
                final UpdateInformation update = this.updateQueue.take();

                switch (update.type()) {
                    case READY -> System.out.println("Thread %s is ready!".formatted(update.runnerName()));
                    case ROUND_COMPLETED -> {
                        String output = "Thread %s hat Runde %s nach %sms abgeschlossen!".formatted(update.runnerName(), update.round(), update.timeBetweenUpdates());

                        if (update.round() == roundCount) {
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

    @Override
    public int processUpdateAndGetNextSleepTime(UpdateInformation update) {
        this.updateQueue.offer(update);
        if (update.round() < roundCount) {
            return ThreadLocalRandom.current().nextInt(5000, 10000);
        } else {
            return -1;
        }
    }
}
