package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

public class GrandPrix {

    public static void main(String[] args) {
        final Coordinator coordinator = new Coordinator();
        coordinator.startCompetition();
    }
}
