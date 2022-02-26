package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

public record UpdateInformation(String runnerName, UpdateType type, int round, long timeBetweenUpdates) {

    public enum UpdateType {
        READY,
        ROUND_COMPLETED
    }
}
