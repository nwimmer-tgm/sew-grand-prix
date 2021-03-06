package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageDistributor;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Application {

    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("d.L.u H:m:s").toFormatter();

    public static void main(String[] args) {
        final GrandPrix grandPrix = new GrandPrix();
        final MessageDistributor publisher = grandPrix.prepare();
        publisher.registerListener(Application::printMessage);
        grandPrix.start();
    }

    public static void printMessage(final Message message) {
        switch (message) {
            case GrandPrixStartMessage startMsg -> System.out.printf(
                    "Grand Prix gestartet um %s mit %s Läufern, welche %s Runden absolvieren müssen.%n",
                    dateTimeFormatter.format(startMsg.startTime()),
                    startMsg.runnerCount(),
                    startMsg.roundCount());
            case GrandPrixEndMessage endMsg -> System.out.printf(
                    "Grand Prix abgeschlossen um %s.%n",
                    dateTimeFormatter.format(endMsg.endTime()));
            case RunnerReadyMessage readyMsg -> System.out.printf(
                    "Runner %s is ready!%n",
                    readyMsg.runnerName());
            case RoundCompletedMessage roundCompletedMsg -> System.out.printf(
                    "Runner %s hat Runde %s nach %sms abgeschlossen!%n",
                    roundCompletedMsg.runnerName(),
                    roundCompletedMsg.roundNumber(),
                    roundCompletedMsg.roundTime());
            case RunnerFinishedMessage runnerFinishedMsg -> System.out.printf(
                    "Runner %s hat das Rennen als %s. abgeschlossen!%n",
                    runnerFinishedMsg.runnerName(),
                    runnerFinishedMsg.position());
        }
    }
}
