package housewifei;

import java.util.concurrent.TimeUnit;

/**
 * DayLightCycleSimulation Object
 *
 * Used to simulate the day light cycle. Must be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */
public class DayLightCycleSimulation extends EnvironmentSimulation implements Runnable {

    /* Cycle time in seconds. */
    long cycle_time;

    /* Set to true when the simulation is running. Set to false to gracefully stop the simulation. */
    boolean running = false;

    /**
     * Default constructor with cycle time as parameter.
     * @param cycle_time Cycle time in seconds.
     */
    public DayLightCycleSimulation(long cycle_time) {
        this.cycle_time = cycle_time;
        this.state = 0;
    }

    /**
     * Day light cycle simulation thread. Changes its state every cycle time and notifies any observing controller.
     */
    @Override
    public void run() {
        System.out.println("Sim : DayLight Cycle simulation started with cycle time "+cycle_time+"s.");
        try {
            while (running) {
                TimeUnit.SECONDS.sleep(cycle_time);
                synchronized (this) {
                    if (getState() == 0) {
                        setState(1);
                        System.out.println("Sim : It is now day.");
                    } else {
                        setState(0);
                        System.out.println("Sim : It is now night.");
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Sim : DayLight Cycle simulation interrupted.");
        } finally {
            System.out.println("Sim : DayLight Cycle simulation stopped.");
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
