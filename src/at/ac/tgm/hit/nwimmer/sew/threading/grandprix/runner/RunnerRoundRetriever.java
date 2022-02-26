package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

/**
 * Provides access to the round a runner is currently in.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public interface RunnerRoundRetriever {

    /**
     * Returns the round the runner with the given name is currently in.
     *
     * @param runnerName The name of the runner in question.
     * @return The round the runner is currently in.
     */
    int getCurrentRound(final String runnerName);
}
