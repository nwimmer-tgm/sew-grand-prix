package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageConsumer;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RoundCompletedMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.RunnerReadyMessage;

/**
 * A simple {@link EventDispatcher} implementation.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public class RunnerEventManager implements EventDispatcher {

    private final MessageConsumer messageConsumer;

    public RunnerEventManager(final MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void dispatch(String runner, RunnerEvent update) throws InterruptedException {
        final Message message = switch (update.type()) {
            case READY -> new RunnerReadyMessage(runner);
            case ROUND_COMPLETED -> new RoundCompletedMessage(runner, update.round(), update.timeBetweenUpdates());
        };

        this.messageConsumer.put(message);
    }
}
