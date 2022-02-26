package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

/**
 * A message indicating that a runner is ready for start.
 *
 * @param runnerName The name of the runner in question.
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public record RunnerReadyMessage(String runnerName) implements Message {
}
