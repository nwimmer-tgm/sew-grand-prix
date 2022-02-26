package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

/**
 * A message holding information about a runner which just finished the race.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public record RunnerFinishedMessage(String runnerName, int position) implements Message {
}
