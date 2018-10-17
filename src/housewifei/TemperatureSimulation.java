package housewifei;
import java.util.concurrent.TimeUnit;

/**
 * TemperatureSimulation Object
 *
 * Used to simulate the temperature oscillating between cold = 1 and normal = 0. Must be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */
public class TemperatureSimulation extends EnvironmentSimulation implements Runnable {

    /* Cycle time in seconds. */
    long cycle_time;

    /* Set to true when the simulation is running. Set to false to gracefully stop the simulation. */
    boolean running = false;

    /**
     * Default constructor with cycle time as parameter.
     * @param cycle_time Cycle time in seconds.
     */
    public TemperatureSimulation(long cycle_time) {
        this.cycle_time = cycle_time;
        this.state = 0;
    }

    /**
     * Human presence simulation thread. Changes its state every cycle time and notifies any observing controller.
     */
    @Override
    public void run() {
        System.out.println("Sim : Temperature simulation started with cycle time "+cycle_time+"s.");
        try {
            while (running) {
                TimeUnit.SECONDS.sleep(cycle_time);
                synchronized (this) {
                    if (getState() == 0) {
                        setState(1);
                        System.out.println("Sim : Temperature is now cold.");
                    } else {
                        setState(0);
                        System.out.println("Sim : Temperature is normal.");
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Sim : Temperature simulation interrupted.");
        } finally {
            System.out.println("Sim : Temperature simulation stopped.");
        }
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    /**
     * Gracefully stops the simulation.
     */
    public void stopSimulation() {
        running = false;
    }
}
