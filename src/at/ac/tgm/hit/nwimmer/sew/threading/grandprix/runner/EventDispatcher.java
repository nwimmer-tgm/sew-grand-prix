package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

/**
 * Dispatches {@link RunnerEvent RunnerEvents} to the
 * {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker}, converting the event to an
 * appropriate message.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public interface EventDispatcher {

    /**
     * Dispatches the event back to the main coordinator thread.
     *
     * @param runner The name of the runner this event comes from.
     * @param update The event which occurred.
     */
    void dispatch(final String runner, final RunnerEvent update) throws InterruptedException;
}
