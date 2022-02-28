package at.ac.tgm.hit.nwimmer.sew.threading.grandprix;

import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.MessageBroker;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageDistributionService;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageDistributor;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.distribution.MessageListener;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.GrandPrixStartMessage;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.communication.messages.Message;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.Runner;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.runner.RunnerStateManager;
import at.ac.tgm.hit.nwimmer.sew.threading.grandprix.tasks.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Ein Grand Prix zwischen 3 verschiedenen Läufern, über 3 Runden hinweg. Die Läufer müssen in jeder Runde einen
 * {@link ComputationTask} durchführen.
 * <p>
 * Ablauf:
 * <ol>
 *     <li>Konstruktion: interne Felder werden initialisiert, das Rennen ist <b>nicht</b> bereit</li>
 *     <li>{@link #prepare()}: alle notwendigen Threads (einer pro {@link Runner} und einer für den
 *     {@link MessageDistributor}) werden erstellt und gestartet. Die Threads blocken direkt, und warten auf das
 *     eigentliche Startsignal</li>
 *     <li>{@link #start()}: gibt allen Threads das Startsignal, das Rennen beginnt</li>
 * </ol>
 *
 * @author Niklas Wimmer
 * @since 2022-02-24
 */
public class GrandPrix {

    public static final int runnerCount = 3;
    public static final int roundCount = 3;

    private static final List<TaskFactory> defaultTaskFactories = List.of(
            new ComputePiTaskFactory(),
            new ThreadSleepTaskFactory(),
            new ComputePiTaskFactory()
    );

    private final MessageBroker broker;
    private final MessageDistributionService distributor;

    private final CountDownLatch startRunnersLatch;

    private boolean ready;

    public GrandPrix() {
        this.broker = new MessageBroker(runnerCount);
        this.distributor = new MessageDistributionService(this.broker);

        this.startRunnersLatch = new CountDownLatch(1);

        this.ready = false;
    }

    /**
     * Initialisiert alle notwendigen Threads, um das Rennen durchzuführen. Die Threads werden direkt gestartet, blocken
     * aber sofort und warten auf das eigentliche Startsignal (via {@link #start()}).
     * <p>
     * Für jeden Läufer wird ein separater Thread gestartet, ein weiterer Thread wird für den {@link MessageDistributor}
     * verwendet.
     * <p>
     * Bei dem zurückgegebenen {@link MessageDistributor} können {@link MessageListener} registriert werden, welche
     * über den Ablauf des Programms via {@link Message Messages} benachrichtigt werden.
     *
     * @return Der für dieses Rennen zuständige {@link MessageDistributor}, bei dem {@link MessageListener} registriert
     * werden können.
     */
    public MessageDistributor prepare() {
        this.prepareRunners();
        this.prepareMessageDistributor();

        this.ready = true;

        return this.distributor;
    }

    private void prepareRunners() {
        final RunnerStateManager runnerStateManager = new RunnerStateManager(this.broker);
        final TaskProvider taskProvider = new TaskManager(runnerStateManager, defaultTaskFactories);

        for (int i = 0; i < runnerCount; i++) {
            final Runner runner = new Runner(this.startRunnersLatch, runnerStateManager, taskProvider);
            final Thread thread = new Thread(runner, "Runner-" + i);

            thread.start();
        }
    }

    private void prepareMessageDistributor() {
        final Thread t = new Thread(this.distributor, "Message Distributor");
        t.start();
    }

    /**
     * Started das Rennen, ab diesem Moment können keine weiteren {@link MessageListener} mehr bei dem
     * {@link MessageDistributor} registriert werden, weil ab jetzt schon Nachrichten versendet werden.
     * <p>
     * {@link #prepare()} <b>muss</b> vor dieser Methode aufgerufen werden.
     */
    public void start() {
        if (!this.ready) {
            throw new IllegalStateException("The Coordinator needs to be prepared before it can be started!");
        }

        this.distributor.start();

        try {
            this.broker.put(new GrandPrixStartMessage(LocalDateTime.now(), runnerCount, roundCount));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        this.startRunnersLatch.countDown();
    }
}
