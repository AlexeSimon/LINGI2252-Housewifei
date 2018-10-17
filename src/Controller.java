/**
 * Abstract Controller Object
 *
 * <P>Various attributes of controllers and functions to be implemented by children classes.</P>
 *
 */

public abstract class Controller implements Runnable {
    /** server pin on which the controller is connected */
    int pin;

    /** saved state of the controller ; depends on each controller */
    int state;

    /** real state of the environment ; depends on each controller ; only for the pure software prototype version */
    EnvironmentSimulation environment;

    /** to notify the server of any detected change. */
    ServerNotifier server;

    /** type of controller : 0 is for sensors & receptors only, 1 is for actuators and controllers
     * class specific : must be set by children classes*/
    final static int TYPE = 0;

    /** String description of the controller */
    String description = "This controller has no description.";

    /**
     * Default Constructor. Uses default description and sets state, environment state and pin to 0.
     */
    public Controller() {
        this.pin = 0;
        this.state = 0;
    }

    /**
     * Constructor with pin parameter.
     * @param pin The pin the controller is connected to.
     */
    public Controller(int pin) {
        this();
        this.pin = pin;
    }

    /**
     * Constructor with pin and starting state parameters.
     * @param pin The pin the controller is connected to.
     * @param starting_state The starting state of this controller.
     */
    public Controller(int pin, int starting_state) {
        this(pin);
        this.state = starting_state;
    }

    /**
     * Constructor with pin, starting state and description as parameters.
     * @param pin The pin the controller is connected to.
     * @param starting_state The starting state of this controller.
     * @param description The description of this controller.
     */
    public Controller(int pin, int starting_state, String description) {
        this(pin, starting_state);
        this.description = description;
    }

    /**
     * Constructor with pin, starting state and starting environment state parameters. Should only be used in pure testing, software environment.
     * @param pin The pin the controller is connected to.
     * @param starting_state The starting state of this controller.
     * @param environment The environment simulation of this controller.
     */
    public Controller(int pin, int starting_state, EnvironmentSimulation environment) {
        this(pin, starting_state);
        this.environment = environment;
    }

    /**
     * Constructor with pin, starting state, starting environment state and description parameters. Should only be used in pure testing, software environment.
     * @param pin The pin the controller is connected to.
     * @param starting_state The starting state of this controller.
     * @param environment The environment simulation of this controller.
     * @param description The description of this controller.
     */
    public Controller(int pin, int starting_state, EnvironmentSimulation environment, String description) {
        this(pin, starting_state, environment);
        this.description = description;
    }

    /**
     * Prints info on the controller to standard output.
     */
    public void sayHello() {
        System.out.print(this.getClass().getSimpleName()+" on pin "+pin+" running.\n");
    }

    /**
     * Returns the saved state of this controller. Does not update it beforehand.
     * @return The saved state of this controller without refreshing it.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Setter for saved state. Should only be used in pure testing, software environment.
     * @param new_state The new state of this controller.
     */
    public void setState(int new_state) {
        this.state = new_state;
    }

    /**
     * Returns the environment simulation. Should only be used in pure testing, software environment.
     */
    public EnvironmentSimulation getEnvironment() {
        return this.environment;
    }

    /**
     * Setter for environment simulation. Should only be used in pure testing, software environment.
     * @param new_environment The new environment simulation of this controller.
     */
    public void setEnvironment(EnvironmentSimulation new_environment) {
        this.environment = environment;
    }

    /**
     * Returns the pin on which this controller is connected.
     * @return The pin on which this controller is connected.
     */
    public int getPin() {
        return this.pin;
    }

    /**
     * Sets the pin on which this controller is connected.
     * @param new_pin The new pin.
     */
    public void setPin(int new_pin) {
        this.pin = new_pin;
    }

    /**
     * TYPE getter.
     * @return The TYPE of this controller. 0 for pure sensors and receptors, 1 for controllers and actuators.
     */
    public int getTYPE() {
        return TYPE;
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
     * @param new_description The new description.
     */
    public void setDescription(String new_description) {
        this.description = new_description;
    }

    /**
     * Commands the Controller to change its hardware state.
     * Pure sensors/receptors ignore this command.
     * WARNING : This is a prototype, software only version.
     * @param new_state New state to be changed to.
     * @return True if state change was successful, false if it failed or was ignored.
     */
    public boolean changeState(int new_state) {
        if (this.TYPE == 1) {   // only for controllers / actuators
            this.state = new_state;
            this.environment.setState(new_state);
            return true;
        }
        else
            return false;

    }

    /**
     * Commands the Controller or Receptor to update its state to correspond to environment state.
     * WARNING : This is a prototype, software only version, and its next state is given as argument.
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
     * Commands the Controller or Receptor to update its state to correspond to environment state, returns it.
     * WARNING : This is a prototype, software only version.
     * @return The state of the Controller or Receptor after environmental update.
     */
    public int updateGetState() {
        this.state = this.environment.getState();
        return this.state;
    }

    public void startController() {

    }

    public void run() {

    }
}
