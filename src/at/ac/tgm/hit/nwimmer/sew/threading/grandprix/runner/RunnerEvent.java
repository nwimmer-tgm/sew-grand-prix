package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

/**
 * Models an update send by a runner to the {@link EventDispatcher}.
 *
 * @author Niklas Wimmer
 * @since 2022-02-24
 */
public record RunnerEvent(EventType type, int round, long timeBetweenUpdates) {

    /**
     * The type of event which occurred.
     */
    public enum EventType {
        READY,
        ROUND_COMPLETED
    }
}
