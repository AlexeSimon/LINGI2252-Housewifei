/**
 * EnvironmentSimulation Object
 *
 * Used to simulate an environment for controllers. This is the non runnable basic version.
 * @author Alexe Simon and Mawait Maxime
 */

public class EnvironmentSimulation {
    /* Environmental state of this simulation. */
    int state;

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

}
