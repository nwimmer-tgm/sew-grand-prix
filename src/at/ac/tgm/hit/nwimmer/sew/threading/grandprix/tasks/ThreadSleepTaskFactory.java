package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

public class ThreadSleepTaskFactory implements TaskFactory {

    private static final int BASE_SLEEP_TIME = 500;
    private static final int DIFFICULTY_ADDITION = 1_000;

    @Override
    public ComputationTask create(double difficulty, double randomMultiplier) {
        final int sleepTime = (int) ((BASE_SLEEP_TIME + DIFFICULTY_ADDITION * difficulty) * randomMultiplier);

        return () -> Thread.sleep(sleepTime);
    }
}
