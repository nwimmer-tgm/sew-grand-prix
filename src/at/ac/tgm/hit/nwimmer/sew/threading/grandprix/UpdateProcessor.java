package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

public interface UpdateProcessor {

    int processUpdateAndGetNextSleepTime(final UpdateInformation update);
}
