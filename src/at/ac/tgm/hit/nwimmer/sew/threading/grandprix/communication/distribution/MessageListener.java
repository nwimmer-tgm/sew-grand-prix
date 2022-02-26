package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;

/**
 * Can be registered at an {@link MessageDistributor} and will be notified for each message a
 * {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker} receives.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
@FunctionalInterface
public interface MessageListener {

    /**
     * Called for every message received.
     *
     * @param message The message that was received.
     */
    void onMessage(final Message message);
}
