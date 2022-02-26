package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution;

/**
 * Distributes all messages put into a {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker}
 * to all via {@link #registerListener(MessageListener)} registered {@link MessageListener MessageListeners}.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public interface MessageDistributor {

    /**
     * Registers a new listener to this distributor.
     *
     * @param listener The listener to register. Will receive updates for all received messages.
     */
    void registerListener(final MessageListener listener);
}
