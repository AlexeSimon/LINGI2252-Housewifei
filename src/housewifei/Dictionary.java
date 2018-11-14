package housewifei;

public class Dictionary {
    public static String getDescription(String pattern){
        String res;
        switch (pattern) {
            case "ControlledLight":
                res = "Basic prototype of an automated light with 2 states. State is 0 if it is switched off, and 1 if it is switched on.";
                break;
            case "ControlledHeater":
                res = "Basic prototype of an automated heater with 2 states. State is 0 if it is switched off, and 1 if it is switched on.";
                break;
            case "InfraredCamera":
                res = "Basic prototype of an infrared camera for human detection with 2 states. State is 0 if there is no human, and 1 if there is.";
                break;
            case "LightSensor":
                res = "Basic prototype of a light sensor with 2 states. State is 0 if it is dark, and 1 if there is light.";
                break;
            case "RadioReceiver":
                res = "Basic prototype of a radio receiver with 2 states. State is 0 if the last command was turn off, 1 if the last command was turn on.";
                break;
            default:
                res = "Basic prototype of a controller with 2 states";
                break;
        }
        return res;
    }
}
