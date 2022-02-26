package at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages;

import java.time.LocalDateTime;

/**
 * A message holding information about the end of a Grand Prix.
 *
 * @author Niklas Wimmer
 * @since 2022-02-26
 */
public record GrandPrixEndMessage(LocalDateTime endTime) implements Message {
}
