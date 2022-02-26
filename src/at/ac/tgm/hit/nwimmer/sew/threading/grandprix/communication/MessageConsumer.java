package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;

/**
 * The consuming part of the {@link MessageBroker}.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 * @see MessageBroker
 */
public interface MessageConsumer {

    /**
     * Puts a new message into the broker. Blocks the thread if the max capacity of the broker is reached until messages
     * get taken out of the broker again.
     *
     * @param message The new message to be inserted into the broker.
     * @throws InterruptedException when the thread gets interrupted while this method blocks
     * @see java.util.concurrent.BlockingQueue#put(Object) 
     */
    void put(final Message message) throws InterruptedException;
}
