public class RadioReceiver extends Controller{
    /** type of controller : 0 is for sensors & receptors only, 1 is for actuators and controllers
     * class specific : must be set by children classes*/
    final static int TYPE = 0;
    /** String description of the controller */
    String description = "Basic prototype of a radio receiver with  states. State is 0 if there is no command in the queue, 1 if a turn on command was received, and 2 if a turned off command was received.";

}

