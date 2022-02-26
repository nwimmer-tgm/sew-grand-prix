package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.GrandPrix;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.RunnerRoundRetriever;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link SleepTimeProvider} implementation.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class SleepTimeManager implements SleepTimeProvider {

    private final RunnerRoundRetriever runnerRoundRetriever;

    public SleepTimeManager(final RunnerRoundRetriever runnerRoundRetriever) {
        this.runnerRoundRetriever = runnerRoundRetriever;
    }

    // TODO runner could theoretically call this before the value in the runner coordinator gets updated, sync it
    @Override
    public int retrieveNextSleepTime(String runnerName) {
        if (this.runnerRoundRetriever.getCurrentRound(runnerName) < GrandPrix.roundCount) {
            return ThreadLocalRandom.current().nextInt(500, 1_000);
        } else {
            return -1; // poison pill, indicates to the runner that it should stop
        }
    }
}
