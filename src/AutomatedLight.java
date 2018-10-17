public class AutomatedLight extends Controller {
    /** type of controller : 0 is for sensors & receptors only, 1 is for actuators and controllers
     * class specific : must be set by children classes*/
    final static int TYPE = 1;
    /** String description of the controller */
    String description = "Basic prototype of an automated light with 2 states. State is 0 if it is switched off, and 1 if it is switched on.";
}
