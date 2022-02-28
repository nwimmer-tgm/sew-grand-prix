package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

public class ComputePiTaskFactory implements TaskFactory {

    private static final int BASE_PRECISION = 500_000_000;
    private static final int DIFFICULTY_ADDITION = 1_000_000_000;

    @Override
    public ComputationTask create(final double difficulty, final double randomMultiplier) {
        final int loopCount = (int) ((BASE_PRECISION + DIFFICULTY_ADDITION * difficulty) * randomMultiplier);

        return () -> {
            double sum = 1;
            int sign = -1;
            double divisor = 3;

            for (int i = 0; i < loopCount; i++) {
                sum += sign / divisor;
                divisor += 2;
                sign *= -1;
            }

            final double ignored = sum * 4; // found pi
        };
    }
}
