package housewifei;
import java.util.concurrent.TimeUnit;


public class MainClass {

    public static void main(String[] args) {
        /* for the hard coded prototype only, the pins must be as follow:
         *  0 : housewifei.InfraredCamera
         *  1 : housewifei.LightSensor
         *  2 : housewifei.RadioReceiver
         *  3 : housewifei.ControlledLight
         */

        /*System.out.println("MAIN : Simulation will run for 15 seconds. First 3 seconds are for start-up time, with light remote control deactivated. Next 7 seconds are fully simulated with light emote control activated. Final 5 seconds are fully simulated with light remote control deactivated. Simulation starts at night, human out of home, light remote control set to OFF, and it is cold. Lights are to be turned on only if human is home and it is night and remote control is activated. Heater is to be turned on only if human is home and temperature is normal.");
        System.out.println("MAIN : Now instantiating housewifei.Server, Controllers and Simulations.");
        Server myServer = new Server(6);

        InfraredCamera myCamera = new InfraredCamera();
        LightSensor myLightSensor = new LightSensor();
        RadioReceiver myRadioReceiver = new RadioReceiver();
        ControlledLight myControlledLight = new ControlledLight();
        ColdSensor myColdSensor = new ColdSensor();
        ControlledHeater myHeater = new ControlledHeater();

        myServer.connectController(0, myCamera);
        myServer.connectController(1, myLightSensor);
        myServer.connectController(2, myRadioReceiver);
        myServer.connectController(3, myControlledLight);
        myServer.connectController(4, myColdSensor);
        myServer.connectController(5, myHeater);

        HumanPresenceSimulation myHumanSim = new HumanPresenceSimulation(2);
        DayLightCycleSimulation myDayLightSim = new DayLightCycleSimulation(3);
        EnvironmentSimulation myRemote = new EnvironmentSimulation();
        EnvironmentSimulation myLightEnvironment = new EnvironmentSimulation();
        TemperatureSimulation myTempSim = new TemperatureSimulation(4);
        EnvironmentSimulation myHeaterEnvironment = new EnvironmentSimulation();

        myCamera.setEnvironment(myHumanSim);
        myLightSensor.setEnvironment(myDayLightSim);
        myRadioReceiver.setEnvironment(myRemote);
        myControlledLight.setEnvironment(myLightEnvironment);
        myColdSensor.setEnvironment(myTempSim);
        myHeater.setEnvironment(myHeaterEnvironment);

        System.out.println("MAIN : Starting Simulations...");

        myHumanSim.startSimulation();
        myDayLightSim.startSimulation();
        myTempSim.startSimulation();

        System.out.println("MAIN : Starting Controllers...");

        myServer.startAllControllers();

        System.out.println("MAIN : Starting housewifei.Server...");

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
            myTempSim.stopSimulation();
            System.out.println("MAIN : Gracefully stopping controllers...");
            myServer.stopAllControllers();
            System.out.println("MAIN : Gracefully stopping server...");
            myServer.stopServer();
        }*/

        /*ConfigReader cf = new ConfigReader("src/housewifei/config.json");
        System.out.println(cf.getNb_rules());*/

        Server srv = new Server("src/housewifei/config.json");
        for (Controller c: srv.controllers) {
            System.out.println(c.getDescription());
        }

    }
}
