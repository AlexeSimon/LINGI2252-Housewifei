package housewifei;

import java.util.concurrent.TimeUnit;

/**
 * HumanPresenceSimulation Object
 *
 * Used to simulate the presence of a human. Must be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */
public class HumanPresenceSimulation extends EnvironmentSimulation implements Runnable {

    /* Constructor inherited. */
    public HumanPresenceSimulation() {
        this(0, 2);
    }

    /* Constructor inherited. */
    public HumanPresenceSimulation(int state, long cycle_time) {
        super(state, cycle_time);
        setMessage(1, "Sim : Human is now home.");
        setMessage(2, "Sim : Human is now out.");
    }
}
