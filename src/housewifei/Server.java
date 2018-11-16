package housewifei;

import util.*;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Server Object
 *
 * Controls the house automation. Needs to be connected to {@link Controller}s, and listens to a {@link ServerNotifier}.
 * @author Alexe Simon and Mawait Maxime
 */
public class Server implements Runnable, TalkativeObject {

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

    private PrintMediator pm = PrintMediator.getInstance();

    private String name = "SVR";

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
            pm.showMessage(this,"Error while reading file "+config_file_controllers+" ("+e+").", PrintPriority.ERROR);
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
            pm.showMessage(this,"Error while reading file "+config_file_rules+" ("+e+").", PrintPriority.ERROR);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Manually add a controller to the list and adds the {@link ServerNotifier} to it.
     * @param pin The pin this controller is connected to. Must not be in use.
     * @param controller The controller to be connected.
     */
    public void connectController(int pin, Controller controller) {
        if(pinIsValid(pin) && !isPinUsed(pin)) {
            controllers[pin] = controller;
            controller.setServer(eventListener);
            controller.setPin(pin);
        }
        else
            pm.showMessage(this,pin+" already in use.", PrintPriority.ERROR);

    }

    public void disconnectController(int pin) {
        if(pinIsValid(pin) && isPinUsed(pin)) {
            getController(pin).setServer(null);
            getController(pin).setPin(-1);
            controllers[pin] = null;
        }
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }

    public Controller getController(int pin) {
        if(pinIsValid(pin) && isPinUsed(pin))
            return controllers[pin];
        else {
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
            return null;
        }
    }

    public Controller[] getControllers() {
        return controllers;
    }

    public boolean isPinUsed(int pin) {
        return pinIsValid(pin) && controllers[pin] != null;
    }

    public boolean pinIsValid(int pin) {
        return (pin >= 0 && pin < controllers.length);
    }

    /**
     * Starts a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void startController(int pin) {
        getController(pin).startController();
    }

    /**
     * Stops a single controller on a certain pin.
     * @param pin The pin the controller is connected to.
     */
    public void stopController(int pin) {
        getController(pin).stopController();
    }

    /**
     * Starts all controllers connected to this server.
     */
    public void startAllControllers() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (getController(i) != null)
                getController(i).startController();
        }
    }

    /**
     * Gracefully stops all controllers connected to this server.
     */
    public void stopAllControllers() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (getController(i) != null)
                getController(i).stopController();
        }

    }

    public EnvironmentSimulation getControllerEnvironment(int pin) {
        if(getController(pin) != null)
            return getController(pin).getEnvironment();
        else
            return null;
    }

    public void setControllerEnvironment(int pin, EnvironmentSimulation environment) {
        if(isPinUsed(pin))
            getController(pin).setEnvironment(environment);
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }

    public void startControllerEnvironment(int pin) {
        if(isPinUsed(pin))
            getController(pin).startEnvironmentSimulation();
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }
    public void startControllerEnvironment(int pin, int cycleTime) {
        if(isPinUsed(pin))
            getController(pin).startEnvironmentSimulation((long) cycleTime);
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }

    public void startAllEnvironments() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (getController(i) != null)
                getController(i).startEnvironmentSimulation();
        }
    }

    public void stopAllEnvironments() {
        for (int i = 0 ; i < controllers.length ; i++) {
            if (getController(i) != null)
                getController(i).stopEnvironmentSimulation();
        }
    }

    public void stopControllerEnvironment(int pin) {
        if(isPinUsed(pin))
            getController(pin).stopEnvironmentSimulation();
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }

    public void setControllerEnvironmentState(int pin, int state) {
        if(isPinUsed(pin))
            getController(pin).setEnvironmentState(state);
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
    }

    public void setControllerState(int pin, int state) {
        if(isPinUsed(pin))
            getController(pin).changeState(state);
        else
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
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
            return getController(pin).updateIsState(state);
        else {
            pm.showMessage(this,"no controller connected on pin "+pin+".", PrintPriority.ERROR);
            return false;
        }
    }

    public void checkRules() {
        boolean didJob = false;
        for (int i = 0; i < rules.size() ; i++) {
            try {
                if (rules.get(i).evaluateExpression(this)) {
                    didJob = true;
                    pm.showMessage(this,"Activating rule "+ i +": "+rules.get(i)+".", PrintPriority.DEBUG);
                    rules.get(i).executeConsequence(this);
                }

            } catch (ParseException e) {
                pm.showMessage(this, "caught parse exception : "+ e +" in rule "+i+". Removing rule.", PrintPriority.ERROR);
                rules.remove(i);
            }

        }
        if(!didJob)
            pm.showMessage(this,"No change needed.", PrintPriority.DEBUG);
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
        pm.showMessage(this, "Server started.", PrintPriority.INFO);
        while(running) {
            synchronized (eventListener) {
                try {
                    eventListener.wait();
                    checkRules();

                } catch (InterruptedException e) {

                }
            }
        }
        pm.showMessage(this, "Server stopped.", PrintPriority.INFO);
    }

}
