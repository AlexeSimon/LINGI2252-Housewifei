package housewifei;

import java.util.concurrent.TimeUnit;

/**
 * DayLightCycleSimulation Object
 *
 * Used to simulate the day light cycle. Must be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */
public class DayLightCycleSimulation extends EnvironmentSimulation implements Runnable {

    /* Constructor inherited. */
    public DayLightCycleSimulation() {
        this(0, 3);
    }

    /* Constructor inherited. */
    public DayLightCycleSimulation(int state, long cycle_time) {
        super(state, cycle_time);
        setMessage(1, "Sim : It is now Day.");
        setMessage(2, "Sim : It is now Night.");
    }


}
