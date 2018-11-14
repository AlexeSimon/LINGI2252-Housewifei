package housewifei;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 * Server Object
 *
 * Controls the house automation. Needs to be connected to {@link Controller}s, and listens to a {@link ServerNotifier}.
 * @author Alexe Simon and Mawait Maxime
 */
public class Server implements Runnable {

    /** Controllers connected to this server. */
    ArrayList<Controller> controllers;

    /** Server Notifier this server is listening to. */
    ServerNotifier eventListener;

    /** Set to true if the controller thread is running. Set to false when the controller must be gracefully stopped. */
    boolean running = false;

    /**
     * Default constructor with number of pin. Automatically instantiate a {@link ServerNotifier}. Use with {@link #connectController(int, Controller)}
     * @param number_of_pins The number of pin this server has.
     */
    public Server(int number_of_pins) {
        this.controllers = new ArrayList<Controller>(number_of_pins);
        this.eventListener = new ServerNotifier();
    }

    /**
     * Future final controller with config files as input. Not yet implemented.
     * @param parameter_file name of the parameter file
     */
    public Server(String parameter_file) {
        /* PART 1 : Parsing of fist config file on pins and present controllers */

        // NOT DONE IN THIS PROTOTYPE

        /* PART 2 : Parsing of second config file on rules consisting of conditions and events */

        // NOT DONE IN THIS PROTOTYPE

        /* PART 3 : Run Server Thread */

        // NOT DONE IN THIS PROTOTYPE
        ConfigReader cfg = new ConfigReader(parameter_file);

        this.eventListener = new ServerNotifier();
        this.controllers = new ArrayList<>(cfg.getNb_controllers());

        ControllerFactory factory = new ControllerFactory();

        for (int i = 0; i < cfg.getNb_controllers(); i++){
            Controller c = (Controller) factory.create(cfg.getControllers().getString(Integer.toString(i)));
            connectController(i,c);
        }
    }

    /**
     * Manually add a controller to the list and adds the {@link ServerNotifier} to it.
     * @param pin The pin this controller is connected to. Must not be in use.
     * @param controller The controller to be connected.
     */
    public void connectController(int pin, Controller controller) {
        controllers.add(pin, controller);
        controller.setServer(eventListener);
        controller.setPin(pin);
    }

    /**
     * Starts a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void startController(int pin) {
        controllers.get(pin).startController();
    }

    /**
     * Stops a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void stopController(int pin) {
        controllers.get(pin).stopController();
    }

    /**
     * Starts all controllers connected to this server.
     */
    public void startAllControllers() {
        for (Controller controller : controllers)
            controller.startController();
    }

    /**
     * Gracefully stops all controllers connected to this server.
     */
    public void stopAllControllers() {
        for (Controller controller : controllers)
            controller.stopController();
    }

    /**
     * Returns the {@link ServerNotifier} for this server.
     * @return The ServerNotifier this server is listening to.
     */
    public ServerNotifier getEventListener() {
        return this.eventListener;
    }

    /**
     * Sets the {@link ServerNotifier} for this server. Does not update any supposedly connected controller.
     * @param eventListener The ServerNotifier this server must listen to.
     */
    public void setEventListener(ServerNotifier eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Some hard coded rules for demonstration of the correct server-controllers thread implementation.
     */
    public void checkHardCodedRules() {
        /* for the hard coded prototype only, the pins must be as follow:
         *  0 : InfraredCamera - 0 for no human present, 1 for human present
         *  1 : LightSensor - 0 for dark, 1 for light
         *  2 : RadioReceiver - 0 for off, 1 for on
         *  3 : ControlledLight - 0 for off, 1 for on
         *  4 : ColdSensor - 0 for normal, 1 for cold
         *  5 : ControlledHeater - 0 for off, 1 for on
         */

        boolean flag = true;

        // if human is home and it is dark and manual remote is on ON position and light is off, turn on light
        if(controllers.get(0).updateIsState(1) && controllers.get(1).updateIsState(0) && controllers.get(2).updateIsState(1) && controllers.get(3).updateIsState(0)) {
            System.out.println("Svr : Server decided light has to be turned on.");
            controllers.get(3).changeState(1);
            flag = false;
        } // if human isn't home or it is day or manual remote is on OFF position and light is on, turn off light
        else if((controllers.get(0).updateIsState(0) || controllers.get(1).updateIsState(1) || controllers.get(2).updateIsState(0)) && controllers.get(3).updateIsState(1)) {
            System.out.println("Svr : Server decided light has to be turned off.");
            controllers.get(3).changeState(0);
            flag = false;
        } // else there is nothing to do
        if(controllers.get(4).updateIsState(1) && controllers.get(0).updateIsState(1) && controllers.get(5).updateIsState(0)) {
            System.out.println("Svr : Server decided heater has to be turned on.");
            controllers.get(5).changeState(1);
            flag = false;
        }
        else if((controllers.get(4).updateIsState(0) || controllers.get(0).updateIsState(0)) && controllers.get(5).updateIsState(1)){
            System.out.println("Svr : Server decided heater has to be turned off.");
            controllers.get(5).changeState(0);
            flag = false;
        }
        if(flag)
            System.out.println("Svr : Server decided no change is needed.");
        System.out.println();
    }

    /**
     * Starts the server.
     */
    public void startServer() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    /**
     * Gracefully stops the server.
     */
    public void stopServer() {
        running = false;
    }

    /**
     * Generic thread function for a server. Uses {@link #checkHardCodedRules()} for demonstration.
     */
    public void run() {
        System.out.print("Svr : Server started.\n");
        while(running) {
            synchronized (eventListener) {
                try {
                    eventListener.wait(5000);
                    System.out.print("Svr : Server awakened by notification.\n");
                    checkHardCodedRules();

                } catch (InterruptedException e) {
                    System.out.print("Svr : Server interrupted.\n");
                }
            }
        }
        System.out.print("Svr : Server stopped.\n");
    }
}
