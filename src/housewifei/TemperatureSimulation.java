package housewifei;
import java.util.concurrent.TimeUnit;

/**
 * TemperatureSimulation Object
 *
 * Used to simulate the temperature oscillating between warm = 1 and cold = 0. Must be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */
public class TemperatureSimulation extends EnvironmentSimulation implements Runnable {
    /* Constructor inherited. */
    public TemperatureSimulation() {
        this(0, 4);
    }

    /* Constructor inherited. */
    public TemperatureSimulation(int state, long cycle_time) {
        super(state, cycle_time);
        setMessage(1, "Sim : Temperature is Warm.");
        setMessage(2, "Sim : Temperature is Cold.");
    }
}
