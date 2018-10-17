public class InfraredCamera extends Controller {
    /** type of controller : 0 is for sensors & receptors only, 1 is for actuators and controllers
     * class specific : must be set by children classes*/
    final static int TYPE = 0;
    /** String description of the controller */
    String description = "Basic prototype of an infrared camera with 2 states. State is 0 if there is no source of heat, and 1 if there is. Example : detects human presence.";

}
