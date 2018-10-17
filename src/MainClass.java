import java.util.concurrent.TimeUnit;

public class MainClass {

    public static void main(String[] args) {
        /* for the hard coded prototype only, the pins must be as follow:
         *  0 : InfraredCamera
         *  1 : LightSensor
         *  2 : RadioReceiver
         *  3 : ControlledLight
         */

        System.out.println("MAIN : Simulation will run for 15 seconds. First 3 seconds are for start-up time, with remote control deactivated. Next 7 seconds are fully simulated with remote control activated. Final 5 seconds are fully simulated with remote control deactivated. Simulation starts at night, human out of home and remote control set to OFF. Lights are to be turned on only if human is home and it is night and remote control is activated.");
        System.out.println("MAIN : Now instantiating Server, Controllers and Simulations.");
        Server myServer = new Server(4);

        InfraredCamera myCamera = new InfraredCamera();
        LightSensor myLightSensor = new LightSensor();
        RadioReceiver myRadioReceiver = new RadioReceiver();
        ControlledLight myControlledLight = new ControlledLight();

        myServer.connectController(0, myCamera);
        myServer.connectController(1, myLightSensor);
        myServer.connectController(2, myRadioReceiver);
        myServer.connectController(3, myControlledLight);

        HumanPresenceSimulation myHumanSim = new HumanPresenceSimulation(2);
        DayLightCycleSimulation myDayLightSim = new DayLightCycleSimulation(3);
        EnvironmentSimulation myRemote = new EnvironmentSimulation();
        EnvironmentSimulation myLightEnvironment = new EnvironmentSimulation();

        myCamera.setEnvironment(myHumanSim);
        myLightSensor.setEnvironment(myDayLightSim);
        myRadioReceiver.setEnvironment(myRemote);
        myControlledLight.setEnvironment(myLightEnvironment);

        System.out.println("MAIN : Starting Simulations...");

        myHumanSim.startSimulation();
        myDayLightSim.startSimulation();

        System.out.println("MAIN : Starting Controllers...");

        myServer.startAllControllers();

        System.out.println("MAIN : Starting Server...");

        myServer.startServer();

        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println("MAIN : Remote control manually set to ON.");
            myRemote.setState(1);
            TimeUnit.SECONDS.sleep(7);
            System.out.println("MAIN : Remote control manually set to OFF.");
            myRemote.setState(0);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {

        } finally {
            System.out.println("MAIN : Gracefully stopping Simulations...");
            myDayLightSim.stopSimulation();
            myHumanSim.stopSimulation();
            System.out.println("MAIN : Gracefully stopping controllers...");
            myServer.stopAllControllers();
            System.out.println("MAIN : Gracefully stopping server...");
            myServer.stopServer();
        }


    }
}
