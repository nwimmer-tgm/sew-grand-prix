package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;

import java.util.concurrent.BlockingQueue;

/**
 * The producing part of the {@link MessageBroker}.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 * @see MessageBroker
 */
public interface MessageProvider {

    /**
     * Retrieves the oldest method from the broker. Blocks the thread if there is no message currently available until
     * a new message gets put into the broker again.
     * @return The oldest message currently present in the broker.
     * @throws InterruptedException when the thread gets interrupted while this method blocks
     * @see BlockingQueue#take()
     */
    Message take() throws InterruptedException;
}
