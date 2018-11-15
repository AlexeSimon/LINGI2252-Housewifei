package housewifei;

import util.CompanyParser;

public class HousewifeiParser extends CompanyParser {

    public HousewifeiParser() {

    }

    public int[] parseControllerState(String string) {
        int[] ans = new int[2];
        String[] subs = string.split("_");
        ans[0] = Integer.parseInt(subs[0]);
        ans[1] = Integer.parseInt(subs[1]);
        return ans;
    }


    private class BooleanExpressionEvaluator {
        int pos = -1;
        int ch;
        Server server;
        String expression;


    }

}
