package housewifei;
import java.util.concurrent.TimeUnit;


public class MainClass {

    public static void main(String[] args) {

        Server myServer = new Server("controllers.txt", "rules.txt");

        myServer.startAllControllers();
        myServer.startServer();

        myServer.getControllerEnvironment(0).setName("HumanPresence");
        myServer.startControllerEnvironment(0,2);
        myServer.getControllerEnvironment(1).setName("Light");
        myServer.startControllerEnvironment(1,3);
        myServer.getControllerEnvironment(2).setName("RemoteSwitch");
        myServer.setControllerEnvironmentState(2,1);
        myServer.getControllerEnvironment(4).setName("Temperature");
        myServer.startControllerEnvironment(4,4);


        try {
            TimeUnit.SECONDS.sleep(15);

        } catch (InterruptedException e)
        {

        }

        myServer.stopAllControllers();
        myServer.stopAllEnvironments();
        myServer.stopServer();

    }

}
