package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link SleepTimeProvider} implementation.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class SleepTimeManager implements SleepTimeProvider {

    @Override
    public int retrieveNextSleepTime(String runnerName) {
        return ThreadLocalRandom.current().nextInt(500, 1_000);
    }
}
