package housewifei;

import util.BooleanExpressionEvaluator;

public class HousewifeiBooleanExpressionEvaluator extends BooleanExpressionEvaluator {

    Server server;

    public HousewifeiBooleanExpressionEvaluator(Server server, String expression) {
        super(expression);
        this.server = server;
    }

    @Override
    public boolean parseNumber(int startPos) {
        int tempPin;
        int tempState;
        int delta;
        while (ch >= '0' && ch <= '9')
            nextChar();
        tempPin = Integer.parseInt(expression.substring(startPos, this.pos));
        if(!eat('_'))
            throw new RuntimeException("Unexpected : " + (char) ch);
        delta = this.pos;
        while (ch >= '0' && ch <= '9') nextChar();
        tempState = Integer.parseInt(expression.substring(delta, this.pos));
        return server.isControllerInState(tempPin, tempState);
    }
}
