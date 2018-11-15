package housewifei;

import util.CompanyFileReader;
import util.CompanyFileReaderBuilder;

import java.util.ArrayList;

/**
 * Server Object
 *
 * Controls the house automation. Needs to be connected to {@link Controller}s, and listens to a {@link ServerNotifier}.
 * @author Alexe Simon and Mawait Maxime
 */
public class Server implements Runnable {

    /** Controllers connected to this server. */
    private Controller[] controllers;

    /** Rules. */
    private ArrayList<Rule> rules;

    /** Server Notifier this server is listening to. */
    private ServerNotifier eventListener;

    /** Set to true if the controller thread is running. Set to false when the controller must be gracefully stopped. */
    private boolean running = false;

    /* To hold self thread. */
    private Thread thread;


    /* Set to to true to silence server's prints */
    private boolean silent = false;

    /**
     * Default constructor with number of pin. Automatically instantiate a {@link ServerNotifier}. Use with {@link #connectController(int, Controller)}
     * @param number_of_pins The number of pin this server has.
     */
    public Server(int number_of_pins) {
        this.controllers = new Controller[number_of_pins];
        this.eventListener = new ServerNotifier();
    }

    /**
     * Future final controller with config files as input. Not yet implemented.
     * @param config_file_controllers name of the config file holding the controllers
     * @param config_file_rules name of the config file holding the rules
     */
    public Server(String config_file_controllers, String config_file_rules) {
        /* PART 0 : INIT */
        this.eventListener = new ServerNotifier();

        /* PART 1 : Parsing of fist config file on pins and present controllers */
        try {
            CompanyFileReaderBuilder fileReaderBuilder = new CompanyFileReaderBuilder(config_file_controllers);
            CompanyFileReader raf = fileReaderBuilder
                    .setIgnoreEmptyLines(true)
                    .setRemoveSpaces(true)
                    .setRemoveComments(true)
                    .setCommentSymbol("#")
                    .build();

            this.controllers = new Controller[raf.countUsefulLines()];

            String line;
            int count = 0;

            while ((line = raf.smartReadLine()) != null) {
                Controller newController = (Controller) Class.forName("housewifei."+line).getConstructor().newInstance();
                newController.setDescription(line+" on pin "+count);
                newController.setEnvironment(new EnvironmentSimulation());
                connectController(count, (Controller) newController);
                count++;
            }
        } catch (Exception e) {
            print("Error while reading file "+config_file_controllers+". "+e+".");
            System.exit(1);
        }

        /* PART 2 : Parsing of second config file on rules consisting of conditions and events */

        try {
            CompanyFileReaderBuilder fileReaderBuilder = new CompanyFileReaderBuilder(config_file_rules);
            CompanyFileReader raf = fileReaderBuilder
                    .setIgnoreEmptyLines(true)
                    .setRemoveSpaces(true)
                    .setRemoveComments(true)
                    .setCommentSymbol("#")
                    .build();

            rules = new ArrayList<Rule>(raf.countUsefulLines());

            String line;

            while ((line = raf.smartReadLine()) != null) {
                    String[] parts = line.split("@");
                    if (parts.length != 2)
                        throw new RuntimeException("Syntax error not using @ correctly.");
                    rules.add(new Rule(parts[0], parts[1]));
                }

        } catch (Exception e) {
            print("Error while reading file "+config_file_rules+". "+e+".");
            System.exit(1);
        }

        /* PART 3 : Run Server Thread */

        /*// NOT DONE IN THIS PROTOTYPE
        ConfigReader cfg = new ConfigReader(parameter_file);

        this.eventListener = new ServerNotifier();
        this.controllers = new ArrayList<>(cfg.getNb_controllers());

        ControllerFactory factory = new ControllerFactory();

        for (int i = 0; i < cfg.getNb_controllers(); i++){
            Controller c = (Controller) factory.create(cfg.getControllers().getString(Integer.toString(i)));
            connectController(i,c);
        }*/
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean value) {
        silent = value;
    }

    public void print(String message) {
        if(!silent)
            System.out.println(message);
    }

    /**
     * Manually add a controller to the list and adds the {@link ServerNotifier} to it.
     * @param pin The pin this controller is connected to. Must not be in use.
     * @param controller The controller to be connected.
     */
    public void connectController(int pin, Controller controller) {
        if(!isPinUsed(pin)) {
            controllers[pin] = controller;
            controller.setServer(eventListener);
            controller.setPin(pin);
        }
        else
            print("Error : "+pin+" already in use.");

    }

    public void disconnectController(int pin) {
        if(isPinUsed(pin)) {
            controllers[pin].setServer(null);
            controllers[pin].setPin(-1);
            controllers[pin] = null;
        }
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    public Controller getController(int pin) {
        if(isPinUsed(pin))
            return controllers[pin];
        else {
            print("Error : No controller connected on pin "+pin+".");
            return null;
        }
    }

    public Controller[] getControllers() {
        return controllers;
    }

    public boolean isPinUsed(int pin) {
        return controllers[pin] != null;
    }

    /**
     * Starts a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void startController(int pin) {
        if(isPinUsed(pin))
            controllers[pin].startController();
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    /**
     * Stops a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void stopController(int pin) {
        if(isPinUsed(pin))
            controllers[pin].stopController();
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    /**
     * Starts all controllers connected to this server.
     */
    public void startAllControllers() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (controllers[i] != null)
                controllers[i].startController();
        }
    }

    /**
     * Gracefully stops all controllers connected to this server.
     */
    public void stopAllControllers() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (controllers[i] != null)
                controllers[i].stopController();
        }

    }

    public EnvironmentSimulation getControllerEnvironment(int pin) {
        if(isPinUsed(pin))
            return controllers[pin].getEnvironment();
        else {
            print("Error : No controller connected on pin "+pin+".");
            return null;
        }
    }

    public void setControllerEnvironment(int pin, EnvironmentSimulation environment) {
        if(isPinUsed(pin))
            controllers[pin].setEnvironment(environment);
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    public void startControllerEnvironment(int pin) {
        if(isPinUsed(pin))
            controllers[pin].startEnvironmentSimulation();
        else
            print("Error : No controller connected on pin "+pin+".");
    }
    public void startControllerEnvironment(int pin, long cycleTime) {
        if(isPinUsed(pin))
            controllers[pin].startEnvironmentSimulation(cycleTime);
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    public void startAllEnvironments() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (controllers[i] != null)
                controllers[i].startEnvironmentSimulation();
        }
    }

    public void stopAllEnvironments() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (controllers[i] != null)
                controllers[i].stopEnvironmentSimulation();
        }
    }

    public void stopControllerEnvironment(int pin) {
        if(isPinUsed(pin))
            controllers[pin].stopEnvironmentSimulation();
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    public void setControllerEnvironmentState(int pin, int state) {
        if(isPinUsed(pin))
            controllers[pin].setEnvironmentState(state);
        else
            print("Error : No controller connected on pin "+pin+".");
    }

    public void setControllerState(int pin, int state) {
        if(isPinUsed(pin))
            controllers[pin].changeState(state);
        else
            print("Error : No controller connected on pin "+pin+".");
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

    public boolean isControllerInState(int pin, int state) {
        if(isPinUsed(pin))
            return controllers[pin].updateIsState(state);
        else {
            print("Error : No controller connected on pin "+pin+".");
            return false;
        }
    }

    public void checkRules() {
        for (Rule rule : rules) {
            if (rule.evaluateExpression(this))
                rule.executeConsequence(this);
        }
    }


    /**
     * Starts the server.
     */
    public void startServer() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Stops the server.
     */
    public void stopServer() {
        running = false;
        thread.interrupt();
    }

    /**
     * Generic thread function for a server.
     */
    public void run() {
        print("Svr : Server started.\n");
        while(running) {
            synchronized (eventListener) {
                try {
                    eventListener.wait();
                    print("Svr : Server awakened by notification.\n");
                    checkRules();

                } catch (InterruptedException e) {
                    print("Svr : Server interrupted.\n");
                }
            }
        }
        print("Svr : Server stopped.\n");
    }

}
