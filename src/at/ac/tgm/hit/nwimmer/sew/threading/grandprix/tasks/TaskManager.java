package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.GrandPrix;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.RunnerRoundRetriever;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link TaskProvider} implementation.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class TaskManager implements TaskProvider {

    private final RunnerRoundRetriever runnerRoundRetriever;

    private final List<TaskFactory> taskFactories;

    public TaskManager(final RunnerRoundRetriever runnerRoundRetriever, final List<TaskFactory> taskFactories) {
        this.runnerRoundRetriever = runnerRoundRetriever;
        this.taskFactories = taskFactories;
    }

    // TODO runner could theoretically call this before the value in the runner coordinator gets updated, sync it
    @Override
    public ComputationTask retrieveNextTask(String runnerName) {
        final int currentRound = this.runnerRoundRetriever.getCurrentRound(runnerName);

        if (currentRound <= GrandPrix.roundCount) {
            final double difficulty = (1.0 / GrandPrix.roundCount) * currentRound;
            final double random = ThreadLocalRandom.current().nextGaussian(1.0, 0.4);
            final double rangedRandom = Math.min(Math.max(random, 0.5), 1.5);

            return this.taskFactories.get(currentRound - 1).create(difficulty, rangedRandom);
        } else {
            return null; // poison pill, indicates to the runner that it should stop
        }
    }
}
