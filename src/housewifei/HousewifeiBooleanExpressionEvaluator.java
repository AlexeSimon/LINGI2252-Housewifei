package housewifei;

import util.BooleanExpressionEvaluator;

import java.text.ParseException;

public class HousewifeiBooleanExpressionEvaluator extends BooleanExpressionEvaluator {

    Server server;

    public HousewifeiBooleanExpressionEvaluator(Server server, String expression) {
        super(expression);
        this.server = server;
    }

    @Override
    public boolean parseNumber(int startPos) throws ParseException {
        HousewifeiParser parser = new HousewifeiParser();
        while (ch >= '0' && ch <= '9')
            nextChar();
        if(!eat('_'))
            handleError();
        while (ch >= '0' && ch <= '9')
            nextChar();
        int[] ans = parser.parseControllerState(expression.substring(startPos, this.pos));
        return server.isControllerInState(ans[0], ans[1]);
    }
}
