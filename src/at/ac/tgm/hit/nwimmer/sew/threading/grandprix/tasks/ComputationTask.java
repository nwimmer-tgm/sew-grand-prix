package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks;

@FunctionalInterface
public interface ComputationTask {

    void run() throws InterruptedException;
}
