package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

/**
 * Provides a {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.Runner} with the next sleep time.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public interface SleepTimeProvider {

    /**
     * Returns the next sleep time for the runner with the given {@code runnerName}.
     *
     * @param runnerName The runner's name.
     * @return The next sleep time.
     */
    int retrieveNextSleepTime(final String runnerName);
}
