package util;

import housewifei.Server;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.Scanner;

public class CommandInterpreter implements TalkativeObject {

    private Object temp;
    private Scanner sc;
    private boolean running;
    private Object context;
    private PrintMediator pm;
    private boolean silent = false;
    private String name = "CI";

    public CommandInterpreter(Object context) {
        sc = new Scanner(System.in);
        running = false;
        this.context = context;
        this.pm = PrintMediator.getInstance();
    }

    public boolean interpret(String string) {
        return false;
    }

    public void reflectionInterpret(String input) {
        try {
        if(context != null) {
            if(!interpret(input)) {
                String[] split = input.split(" ");
                CompanyParser cp = new CompanyParser();
                Object[] params = new Object[split.length-1];
                Class<?>[] parameterTypes = new Class<?>[split.length-1];
                for(int i = 1; i < split.length; i++) {
                    if (split[i].equals("temp")) {
                        parameterTypes[i-1] = temp.getClass();
                        params[i-1] = temp;
                    } else {
                        parameterTypes[i-1] = cp.recognizeStringClass(split[i]);
                        if (parameterTypes[i-1] == null) {
                            throw new IllegalArgumentException("non recognized string class "+split[i]);
                        }
                        else if (parameterTypes[i-1] == int.class) {
                            params[i-1] = Integer.parseInt(split[i]);
                        }
                        else if (parameterTypes[i-1] == float.class) {
                            params[i-1] = Float.parseFloat(split[i]);
                        }
                        else if (parameterTypes[i-1] == String.class) {
                            params[i-1] = split[i];
                        }
                    }
                }
                Method method = context.getClass().getMethod(split[0], parameterTypes);
                temp = method.invoke(context, params);
                if(temp != null)
                    pm.showMessage(this,temp.toString(),PrintPriority.INFO);
            }
        }
        } catch (Exception e) {
            pm.showMessage(this,"error during reflection ("+e+")",PrintPriority.ERROR);
        }
    }

    public String waitForInput() {

        return sc.nextLine();
    }

    public void startConsole() {
        running = true;
        String tempString;
        pm.showMessage(this,"Command Interpreter started.", PrintPriority.INFO);
        while (running) {
            tempString = waitForInput();
            if(tempString.equals("exit"))
                running = false;
            else if (running)
                reflectionInterpret(tempString);
        }
        pm.showMessage(this,"Command Interpreter stopped.", PrintPriority.INFO);

    }

    public void stopConsole() {
        running = false;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
