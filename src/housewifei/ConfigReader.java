package housewifei;
import org.json.JSONObject;

import java.io.*;

import java.io.BufferedReader;

public class ConfigReader {

    private JSONObject json;
    private JSONObject controllers;
    private JSONObject rules;
    private int nb_controllers;
    private int nb_rules;

    public ConfigReader(String filename) {
        this.json = new JSONObject(readFromConfigFile(filename));
        this.controllers = json.getJSONObject("controllers");
        this.rules = json.getJSONObject("rules");
        this.nb_controllers = json.getBigInteger("nb_controllers").intValue();
        this.nb_rules = json.getBigInteger("nb_rules").intValue();
    }

    public JSONObject getJson() {
        return json;
    }

    public JSONObject getControllers() {
        return controllers;
    }

    public JSONObject getRules() {
        return rules;
    }

    public int getNb_controllers(){
        return this.nb_controllers;
    }

    public int getNb_rules() {
        return nb_rules;
    }

    private static String readFromConfigFile(String filename) {
        BufferedReader bf= null;
        String text = "";
        try {
            bf = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = bf.readLine()) != null) {
                text += line;
            }
        } catch (IOException e) {
            System.err.println("!!!!! Error : "+e.getMessage() + " !!!!!");
            text = "{}";
        }
        finally {
            if (bf != null){
                try {
                    bf.close();
                } catch (IOException e) {
                    System.err.println("!!!!! Error : "+e.getMessage() + " !!!!!");
                }
            }
        }
        return text;
    }

}
