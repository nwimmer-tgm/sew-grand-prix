package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

import java.time.LocalDateTime;

/**
 * A message holding information about the start of a Grand Prix.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public record GrandPrixStartMessage(LocalDateTime startTime, int runnerCount, int roundCount) implements Message {
}
