package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.ComputationTask;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.TaskProvider;

import java.util.concurrent.CountDownLatch;

/**
 * A runner represents an entity in the Grand Prix which executes certain tasks to finish rounds and eventually win the
 * race.
 *
 * @author Niklas Wimmer
 * @since 2022-02-24
 */
public class Runner implements Runnable {

    private final CountDownLatch startLatch;
    private final CountDownLatch readyLatch;

    private final EventDispatcher updateDispatcher;
    private final TaskProvider taskProvider;

    public Runner(final CountDownLatch startLatch,
                  final CountDownLatch readyLatch,
                  final EventDispatcher updateDispatcher,
                  final TaskProvider taskProvider) {
        this.startLatch = startLatch;
        this.readyLatch = readyLatch;

        this.updateDispatcher = updateDispatcher;
        this.taskProvider = taskProvider;
    }

    @Override
    public void run() {
        final String name = Thread.currentThread().getName();

        try {
            startLatch.await();

            updateDispatcher.dispatch(name, new RunnerEvent(RunnerEvent.EventType.READY, -1));
            readyLatch.countDown();

            while (true) {
                final ComputationTask task = this.taskProvider.retrieveNextTask(name);

                if (task == null) {
                    // request to stop execution
                    break;
                }

                final long time = System.currentTimeMillis();
                task.run();
                updateDispatcher.dispatch(name, new RunnerEvent(RunnerEvent.EventType.ROUND_COMPLETED, System.currentTimeMillis() - time));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
