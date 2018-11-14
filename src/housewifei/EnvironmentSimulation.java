package housewifei;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * EnvironmentSimulation Object
 *
 * Used to simulate an environment for controllers. Can be ran with a basic 2 states cycle.
 * @author Alexe Simon and Mawait Maxime
 */

public class EnvironmentSimulation implements Runnable {
    /* Environmental state of this simulation. */
    private int state;

    /* Cycle time in seconds, with default value of 5 seconds.*/
    private long cycleTime = 5;

    /* Set to true when the simulation is running. Set to false to gracefully stop the simulation. */
    private boolean running = false;

    /* Object holding self thread, used to interrupt it.*/
    private Thread thread;

    /** Set to true and the environment won't print any messages. */
    private boolean silent = false;

    private String name = this.getClass().getSimpleName();

    /* Messages to be shown if Simulation is ran in a thread. */
    private List<String> messages;

    /**
     * Default constructor, sets state to 0.
     */
    public EnvironmentSimulation() {
        this.state = 0;
    }

    /**
     * Constructor with state parameter.
     * @param state Starting state for this environment simulation.
     */
    public EnvironmentSimulation(int state) {
        this.state = state;
    }

    /**
     * Constructor with state and cycle time as parameters.
     * @param state Starting state for this environment simulation.
     * @param cycleTime Cycle time in seconds.
     */
    public EnvironmentSimulation(int state, long cycleTime) {
        this.state = state;
        this.cycleTime = cycleTime;
    }

    public void refreshMessages() {
        messages = Arrays.asList(
                "Sim : "+name+" started.",                                   // 0 - thread start
                "Sim : "+name+" now in state 1.",                            // 1 - changed to state 1
                "Sim : "+name+" now in state 0.",                            // 2 - changed to state 0
                "Sim : "+name+" interrupted.",                               // 3 - thread interrupted
                "Sim : "+name+" stopped.",                                   // 4 - thread stopped
                "Sim : "+name+" already running.");                          // 5 - thread already running
    }

    /**
     * Returns the environmental state of this simulation.
     * @return Environment state.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the environmental state of this simulation, and notifies all the controllers that might be listening if the environmental state was changed.
     * @param state The environment state to be set to.
     */
    public synchronized void setState(int state) {
        if (this.state != state) {
            this.state = state;
            notifyAll();
        }
    }

    public long getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(long cycleTime) {
        this.cycleTime = cycleTime;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean value) {
        silent = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMessages() {
        if (messages == null)
            refreshMessages();
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setMessage(int index, String message) {
        if (messages == null)
            refreshMessages();
        this.messages.set(index, message);
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        if (!running) {
            if (messages == null)
                refreshMessages();
            running = true;
            thread = new Thread(this);
            thread.start();
        }
        else
            System.out.println(messages.get(5));
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        running = false;
        if(thread != null)
            thread.interrupt();
    }

    public void print(String message) {
        if(!silent)
            System.out.println(message);
    }

    /**
     * Basic simulation thread. Changes its state every cycle time and notifies any observing controller.
     */
    @Override
    public void run() {
        print(messages.get(0));
        try {
            while (running) {
                TimeUnit.SECONDS.sleep(cycleTime);
                synchronized (this) {
                    if (getState() == 0) {
                        setState(1);
                        print(messages.get(1));
                    } else {
                        setState(0);
                        print(messages.get(2));
                    }
                }
            }
        } catch (InterruptedException e) {
            print(messages.get(3));
        } finally {
            print(messages.get(4));
        }
    }

}
