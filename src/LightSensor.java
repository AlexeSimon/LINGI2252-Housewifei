public class LightSensor extends Controller {
    /** type of controller : 0 is for sensors & receptors only, 1 is for actuators and controllers
     * class specific : must be set by children classes*/
    final static int TYPE = 0;
    /** String description of the controller */
    String description = "Basic prototype of a light sensor with 2 states. State is 0 if it is dark, and 1 if there is light.";

}
