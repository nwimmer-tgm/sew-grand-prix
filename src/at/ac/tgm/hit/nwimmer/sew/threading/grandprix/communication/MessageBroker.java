package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Combines the {@link MessageConsumer} and {@link MessageProvider} to create a simple message broker.
 *
 * The broker is completely thread safe and can handle concurrent read and write operations.
 *
 * The rationale behind splitting the broker into two interfaces is that most other services either only produce new
 * messages or handle them. This way it is immediately clear which part a service has.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class MessageBroker implements MessageConsumer, MessageProvider {

    private final BlockingQueue<Message> backend;

    /**
     * Constructs a new broker with the specified maximum capacity. The broker blocks if the maximum capacity is reached
     * and new messages get inserted.
     * @param maxCapacity Maximum capacity of the underlying message queue. As long as the handler can do his job
     *                    reasonably fast, the number of runners in this Grand Prix should be a fair value.
     */
    public MessageBroker(final int maxCapacity) {
        this.backend = new ArrayBlockingQueue<>(maxCapacity);
    }

    @Override
    public void put(Message message) throws InterruptedException {
        this.backend.put(message);
    }

    @Override
    public Message take() throws InterruptedException {
        return this.backend.take();
    }
}
