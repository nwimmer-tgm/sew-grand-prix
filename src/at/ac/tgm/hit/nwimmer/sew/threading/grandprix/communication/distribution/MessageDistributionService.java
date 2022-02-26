package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageProvider;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.GrandPrixEndMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Distributes messages from the {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker} to
 * all registered {@link MessageListener}. New listeners can be registered via {@link #registerListener(MessageListener)}
 * <p>
 * The front ends can register their listeners here and update ui state based on the messages received.
 * <p>
 * This class implements {@link Runnable} and is meant to be executed in a separate thread. It can be passed to a thread
 * at any time, but distribution of messages will only start as soon as {@link #start()} is called. The distributor will
 * shut itself down when it receives an {@link GrandPrixEndMessage} from the broker, though it can be shut down manually
 * via {@link #shutdown()} as well.
 *
 * @author Niklas Wimmer
 * @see MessageListener
 * @since 2022-02-26
 */
public class MessageDistributionService implements MessageDistributor, Runnable {

    private final MessageProvider messageProvider;
    private final Set<MessageListener> messageListeners;

    private final CountDownLatch startLatch;

    private boolean active;

    /**
     * Constructs a new distributor bound to the given message provider. All events received via this provider will be
     * forwarded to all registered message listeners.
     *
     * @param messageProvider
     */
    public MessageDistributionService(final MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        this.messageListeners = new HashSet<>();

        this.startLatch = new CountDownLatch(1);

        this.active = false;
    }

    public void start() {
        this.active = true;
        this.startLatch.countDown();
    }

    public void shutdown() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void run() {
        try {
            this.startLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        while (active) {
            try {
                final Message message = this.messageProvider.take();

                for (final MessageListener listener : this.messageListeners) {
                    listener.onMessage(message);
                }

                if (message instanceof GrandPrixEndMessage) {
                    this.shutdown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void registerListener(final MessageListener listener) {
        if (this.isActive()) {
            return;
        }

        this.messageListeners.add(listener);
    }
}
