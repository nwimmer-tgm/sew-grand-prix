package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

/**
 * A message holding information about the round a runner just finished.
 *
 * @param runnerName  The name of the runner in question.
 * @param roundNumber The number of the round which the runner just finished.
 * @param roundTime   The time it took the runner to complete this round. Measured in milliseconds.
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public record RoundCompletedMessage(String runnerName, int roundNumber, long roundTime) implements Message {
}
