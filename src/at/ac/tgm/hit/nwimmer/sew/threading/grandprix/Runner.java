package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class Runner implements Runnable {

    private final CountDownLatch latch;
    private final Queue<UpdateInformation> updateQueue;

    public Runner(final CountDownLatch latch, Queue<UpdateInformation> updateQueue) {
        this.latch = latch;
        this.updateQueue = updateQueue;
    }

    @Override
    public void run() {
        try {
            latch.await();
            for (int i = 0; i < 3; i++) {
                final long time = System.currentTimeMillis();
                Thread.sleep(ThreadLocalRandom.current().nextInt(5000, 10000));
                this.updateQueue.offer(new UpdateInformation(Thread.currentThread().getName(), i + 1, System.currentTimeMillis() - time));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
