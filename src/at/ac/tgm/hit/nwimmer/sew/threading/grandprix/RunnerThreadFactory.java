package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.EventDispatcher;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.Runner;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.TaskProvider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class RunnerThreadFactory {

    private static final ThreadGroup runnerThreadGroup = new ThreadGroup("Runners");
    private static final AtomicInteger currentRunnerCount = new AtomicInteger();

    public static Thread newRunnerThread(final CountDownLatch startLatch,
                                         final CountDownLatch readyLatch,
                                         final EventDispatcher dispatcher,
                                         final TaskProvider taskProvider) {
        final Runner runner = new Runner(startLatch, readyLatch, dispatcher, taskProvider);
        return new Thread(runnerThreadGroup, runner, String.valueOf(currentRunnerCount.getAndIncrement()));
    }
}
