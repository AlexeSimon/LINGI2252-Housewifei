package housewifei;

import java.lang.reflect.InvocationTargetException;

public class ControllerBuilder {
    private Controller controller;

    public ControllerBuilder(String controller_type) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        controller = (Controller) Class.forName("housewifei."+controller_type).getConstructor().newInstance();
    }

    public ControllerBuilder setPin(int pin) {
        controller.setPin(pin);
        return this;
    }

    public ControllerBuilder setState(int state) {
        controller.setState(state);
        return this;
    }

    public ControllerBuilder setEnvironmentState(int state) {
        controller.setEnvironmentState(state);
        return this;
    }

    public ControllerBuilder setEnvironment(EnvironmentSimulation environment){
        controller.setEnvironment(environment);
        return this;
    }

    public ControllerBuilder setServer(ServerNotifier server){
        controller.setServer(server);
        return this;
    }

    public ControllerBuilder setDescription(String description){
        controller.setDescription(description);
        return this;
    }

    public ControllerBuilder setSilent(boolean silent){
        controller.setSilent(silent);
        return this;
    }

    public ControllerBuilder setEnvironmentCycleTime(long time){
        controller.setEnvironmentCycleTime(time);
        return this;
    }

    public ControllerBuilder startController(){
        controller.startController();
        return this;
    }

    public ControllerBuilder startEnvironmentSimulation(){
        controller.startEnvironmentSimulation();
        return this;
    }

    public ControllerBuilder startEnvironmentSimulation(long time){
        controller.startEnvironmentSimulation(time);
        return this;
    }

    public Controller build() {
        return controller;
    }
}
