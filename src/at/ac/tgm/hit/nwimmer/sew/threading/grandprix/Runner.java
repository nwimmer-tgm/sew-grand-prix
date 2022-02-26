package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import java.util.concurrent.CountDownLatch;

public class Runner implements Runnable {

    private final CountDownLatch latch;
    private final UpdateProcessor sleepTimeProvider;

    public Runner(final CountDownLatch latch, UpdateProcessor updateQueue) {
        this.latch = latch;
        this.sleepTimeProvider = updateQueue;
    }

    @Override
    public void run() {
        try {
            latch.await();

            int sleepTime = sleepTimeProvider.processUpdateAndGetNextSleepTime(new UpdateInformation(Thread.currentThread().getName(), UpdateInformation.UpdateType.READY, 0,-1));

            for (int currentRound = 1; sleepTime > -1; ++currentRound) {
                final long time = System.currentTimeMillis();
                Thread.sleep(sleepTime);
                sleepTime = sleepTimeProvider.processUpdateAndGetNextSleepTime(new UpdateInformation(Thread.currentThread().getName(), UpdateInformation.UpdateType.ROUND_COMPLETED, currentRound, System.currentTimeMillis() - time));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
