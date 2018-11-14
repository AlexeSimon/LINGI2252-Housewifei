package housewifei;

import java.io.RandomAccessFile;
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
        /* PART 1 : Parsing of fist config file on pins and present controllers */
        try {
            RandomAccessFile raf = new RandomAccessFile(config_file_controllers, "r");
            String line;
            int count = 0;
            this.eventListener = new ServerNotifier();
            while ((line = raf.readLine()) != null)
                count++;
            this.controllers = new Controller[count];
            raf.seek(0);
            count = 0;
            while ((line = raf.readLine()) != null) {
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
            RandomAccessFile raf = new RandomAccessFile(config_file_rules, "r");
            String line;
            rules = new ArrayList<Rule>(5);
            int count = 0;
            while ((line = raf.readLine()) != null) {
                line = line.split("#")[0];
                line = line.replaceAll("\\s", "");
                if (line.length() > 0) {
                    String[] parts = line.split("@");
                    if (parts.length != 2)
                        throw new Exception("Syntax error not using @ correctly");
                    rules.add(new Rule(parts[0], parts[1]));
                    count++;
                }
            }



        } catch (Exception e) {
            print("Error while reading file "+config_file_rules+". "+e+".");
            System.exit(1);
        }

        /* PART 3 : Run Server Thread */

        // NOT DONE IN THIS PROTOTYPE

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
            if (rule.evaluateExpression())
                rule.executeConsequence();
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

    private class Rule {

        private String expression;
        private String consequence;


        public Rule(String expression, String consequence) {
            this.expression = expression;
            this.consequence = consequence;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public String getConsequence() {
            return consequence;
        }

        public void setConsequence(String consequence) {
            this.consequence = consequence;
        }

        public boolean evaluateExpression() {
            // Code greatly inspired from https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form, eleased by author to Public Domain, last visited 14 November 2018
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ')
                        nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                boolean parse() {
                    nextChar();
                    boolean x = parseExpression();
                    if (pos < expression.length())
                        throw new RuntimeException("Unexpected 1: " + (char)ch);
                    return x;
                }

                // Grammar:
                // expression = term | expression `^` term | expression `|` term
                // term = factor | term '&' factor
                // factor = '!' factor | `(` expression `)` | number '_' number
                // () > NOT > AND > XOR = OR

                boolean parseExpression() {
                    boolean x = parseTerm();
                    while(true) {
                        if(eat('^')) {
                            x = (parseTerm() ^ x); // xor
                        }
                        else if (eat('|')) {
                            x = (parseTerm() || x); // or
                        }
                        else {
                            return x;
                        }
                    }
                }

                boolean parseTerm() {
                    boolean x = parseFactor();
                    while (true) {
                        if(eat('&')) { ;
                            x = (parseFactor() && x); // and
                        }
                        else {
                            return x;
                        }
                    }
                }

                boolean parseFactor() {
                    if (eat('!'))
                        return !parseFactor(); // logical not

                    boolean x;
                    int tempPin;
                    int tempState;
                    int delta;
                    int startPos = this.pos;

                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    }
                    else if (ch >= '0' && ch <= '9') { // numbers
                        while (ch >= '0' && ch <= '9') nextChar();
                        tempPin = Integer.parseInt(expression.substring(startPos, this.pos));
                        if(!eat('_'))
                            throw new RuntimeException("Unexpected 2: " + (char)ch);
                        delta = this.pos;
                        while (ch >= '0' && ch <= '9') nextChar();
                        tempState = Integer.parseInt(expression.substring(delta, this.pos));
                        x = isControllerInState(tempPin, tempState);
                    }
                    else {
                        throw new RuntimeException("Unexpected 3: " + (char)ch);
                    }
                    return x;
                }
            }.parse();
        }

        public void executeConsequence() {
            int tempPin;
            int tempState;
            String[] parts = consequence.split("&");
            for (String part : parts) {
                String[] subs = part.split("_");
                tempPin = Integer.parseInt(subs[0]);
                tempState = Integer.parseInt(subs[1]);
                print("Svr : setting "+controllers[tempPin].getDescription()+" to state " +tempState+".");
                setControllerState(tempPin, tempState);
            }
        }

    }

}
