/**
 * Controller Object
 *
 * Used to represent sensors, receptors, controllers and actuators alike.
 * Needs a valid server and environment to be run in a thread.
 * @author Alexe Simon and Mawait Maxime
 */

public class Controller implements Runnable {
    /** Internally saved state of the controller ; depends on each controller. */
    int state;

    /** Server pin on which the controller is connected ; mostly used for identification. */
    int pin;

    /** Object representing the connection to the server. Allows to notify the server of any detected change. */
    ServerNotifier server;

    /** Simulation of the real state of the environment ; depends on each controller ; only for the pure software prototype version. */
    EnvironmentSimulation environment;

    /** String description of the controller. */
    String description = "This controller has no description.";

    /** Set to true if the controller thread is running. Set to false when the controller must be gracefully stopped. */
    boolean running = false;

    /**
     * Default Constructor. Sets state and pin to 0. A valid server and environment must then be added for this controller to be run in a thread.
     */
    public Controller() {
        this.state = 0;
        this.pin = 0;
    }

    /**
     * Constructor with starting state, pin, server, environment and description parameters.
     * @param starting_state The starting state of this controller.
     * @param pin The pin the controller is connected to.
     * @param server The notifier of the server this controller is connected to.
     * @param environment The environment simulation of this controller.
     * @param description The description of this controller.
     */
    public Controller(int starting_state, int pin, ServerNotifier server, EnvironmentSimulation environment, String description) {
        this.state = starting_state;
        this.pin = pin;
        this.server = server;
        this.environment = environment;
        this.description = description;
    }

    /**
     * Returns the internally saved state of this controller. Does not update it beforehand.
     * @return The internally saved state of this controller without updating it.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Setter for internally saved state. Should only be used in pure testing, software environment. See {@link #changeState(int)}.
     * @param state The new state of this controller.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Returns the pin on which this controller is connected.
     * @return The pin on which this controller is connected.
     */
    public int getPin() {
        return this.pin;
    }

    /**
     * Sets the pin on which this controller is connected. Does not update any supposedly connected server.
     * @param pin The new pin.
     */
    public void setPin(int pin) {
        this.pin = pin;
    }

    /**
     * Returns the server notifier on which this controller is connected.
     * @return The server notifier on which this controller is connected.
     */
    public ServerNotifier getServer() {
        return this.server;
    }

    /**
     * Sets the server notifier on which this controller is connected. Does not update any supposedly connected server.
     * @param server The new server notifier.
     */
    public void setServer(ServerNotifier server) {
        this.server = server;
    }

    /**
     * Returns the environment simulation connected to this server.
     * @return The environment simulation connected to this server.
     */
    public EnvironmentSimulation getEnvironment() {
        return this.environment;
    }

    /**
     * Setter for environment simulation. Should only be used in pure testing, software environment.
     * @param environment The new environment simulation of this controller.
     */
    public void setEnvironment(EnvironmentSimulation environment) {
        this.environment = environment;
    }

    /**
     * Description getter.
     * @return The String description of this controller.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Changes the description of this controller.
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Commands the Controller to change its hardware state. Also updates current environment simulation if connected to.
     * Pure sensors/receptors should not use this command.
     * @param state New state to be changed to.
     * @return True if state change was detected, false if not.
     */
    public boolean changeState(int state) {
        if (this.state != state) {   // only if change is needed
            this.state = state; // change state internally
            if (this.environment != null)
                this.environment.setState(state); // update environment state
            return true;
        }
        return false;
    }

    /**
     * Commands the Controller or Receptor to update its state to correspond to environment state.
     * @return True if a change in environment was detected and the state changed. False otherwise.
     */
    public boolean updateState() {
        int new_state = this.environment.getState();
        if (this.state != new_state) {
            this.state = new_state;
            return true;
        }
        else
            return false;
    }

    /**
     * Commands the Controller or Receptor to update its state to correspond to environment state and return it.
     * @return The state of the Controller or Receptor after environmental update.
     */
    public int updateGetState() {
        this.state = this.environment.getState();
        return this.state;
    }

    /**
     * Compares argument received with the state saved internally.
     * @param state State to be compared with the internal state.
     * @return true if equal, false else.
     */
    public boolean isState(int state) {
        return this.state == state;
    }

    /**
     * Similar to {@link #isState(int)}, but updates beforehand.
     * @param state State to be compared with the internal state.
     * @return true if equal, false else.
     */
    public boolean updateIsState(int state) {
        this.state = this.environment.getState();
        return this.state == state;
    }

    /**
     * Starts the controller thread.
     */
    public void startController() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    /**
     * Stops gracefully the controller thread.
     */
    public void stopController() {
        running = false;
    }

    /**
     * Generic controller thread. Observes its environment, and notifies its server when a change was detected.
     */
    @Override
    public void run() {
        System.out.print("Con : " + this.getClass().getSimpleName()+" on pin "+pin+" started.\n");
        while(running) {
            synchronized (environment) {
                try {
                    environment.wait(5000); // time out of 5 seconds in case simulations turns off.

                    if (updateState()) {
                        System.out.print("Con : " + this.getClass().getSimpleName()+" on pin "+pin+" detected environmental state change and notified server.\n");
                        synchronized (server) {
                            server.notify();
                        }
                    }

                } catch (InterruptedException e) {
                    System.out.print("Con : " + this.getClass().getSimpleName()+" on pin "+pin+" interrupted.\n");
                }
            }
        }
        System.out.print("Con : " + this.getClass().getSimpleName()+" on pin "+pin+" stopped.\n");
    }
}
