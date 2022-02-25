package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Coordinator {

    public static void main(String[] args) {
        final Coordinator coordinator = new Coordinator(3);
        coordinator.startCompetition();
    }

    /**
     * Runners push updates into this queue when they finish a round
     */
    private final BlockingQueue<UpdateInformation> updateQueue;

    /**
     * Used to inform runners to start
     */
    private final CountDownLatch startSignalLatch;

    private final int runnerCount;
    private int runnersFinished;

    public Coordinator(final int runnerCount) {
        this.runnerCount = runnerCount;
        this.updateQueue = new ArrayBlockingQueue<>(runnerCount);
        this.startSignalLatch = new CountDownLatch(1);

        for (int i = 0; i < this.runnerCount; i++) {
            final Runner runner = new Runner(this.startSignalLatch, this.updateQueue);
            final Thread thread = new Thread(runner);
            thread.start();
        }

        System.out.println("Die Läufer sind bereit...");
    }

    public void startCompetition() {
        System.out.println("Start!");

        this.startSignalLatch.countDown();

        while (runnersFinished < this.runnerCount) {
            try {
                final UpdateInformation update = this.updateQueue.take();
                String output = "Thread %s hat Runde %s nach %sms abgeschlossen!".formatted(update.threadName(), update.round(), update.timeTook());

                if (update.round() == 3) {
                    output += " Platz %s!".formatted(++runnersFinished);
                }

                System.out.println(output);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
