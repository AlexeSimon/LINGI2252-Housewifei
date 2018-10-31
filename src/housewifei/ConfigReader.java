package housewifei;
import org.json.JSONObject;

import java.io.*;

import java.io.BufferedReader;

public class ConfigReader {

    private JSONObject json;

    public ConfigReader(String filename) {
        this.json = new JSONObject(readFromConfigFile(filename));
    }

    public JSONObject getJson() {
        return json;
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
