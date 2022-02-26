package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

/**
 * Base interface for all messages dispatched into a
 * {@link at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker}.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public sealed interface Message permits GrandPrixStartMessage, GrandPrixEndMessage, RunnerReadyMessage, RoundCompletedMessage, RunnerFinishedMessage {
}
