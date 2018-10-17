import java.util.ArrayList;
import java.util.LinkedList;

public class Server {

    public static final int REFRESH_RATE = 500; //refreshes every 500ms

    ArrayList<Controller> controllers;
    ArrayList<String> conditions;
    ArrayList<String> events;
    LinkedList<int[]> previous_states;

    public Server(String config_file_pins, String config_file_rules) {
        /* PART 1 : Parsing of fist config file on pins and present controllers */

        /* PART 2 : Parsing of second config file on rules consisting of conditions and events */

        /* PART 3 : Run Server Thread */
    }


    public boolean checkRule(String rule) {
        return false;
    }

    public boolean runEvent(String event) {
        return false;
    }

    public boolean verifyControllerState(String controller_state) {
        return false;
    }

    public boolean changeControllerToState(String controller_state) {
        return false;
    }

}
