package housewifei;

public class ControllerFactory implements Factory{

    public Object create(String pattern){
        Object res;
        switch (pattern) {
            case "ControlledLight":
                res = new ControlledLight();
                break;
            case "ControlledHeater":
                res = new ControlledHeater();
                break;
            case "InfraredCamera":
                res = new InfraredCamera();
                break;
            case "LightSensor":
                res = new LightSensor();
                break;
            case "RadioReceiver":
                res = new RadioReceiver();
                break;
            default:
                res = new Controller();
                break;
        }
        return res;
    }

}
