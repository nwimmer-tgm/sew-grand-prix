package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

public interface TaskFactory {

    ComputationTask create(final double difficulty, final double randomMultiplier);
}
