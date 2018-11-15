package housewifei;

import java.text.ParseException;

public class Rule {

    private String expression;
    private String consequence;


    public Rule(String expression, String consequence) {
        this.expression = expression;
        this.consequence = consequence;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public boolean evaluateExpression(Server server) throws ParseException {

        HousewifeiBooleanExpressionEvaluator evaluator = new HousewifeiBooleanExpressionEvaluator(server, expression);
        return evaluator.parse();
    }

    public void executeConsequence(Server server) {
        HousewifeiParser parser = new HousewifeiParser();
        consequence = parser.removeSpaces(consequence);
        String[] parts = consequence.split("&");
        for (String part : parts) {
            int[] job = parser.parseControllerState(part);
            server.print("Svr : setting "+server.getController(job[0]).getDescription()+" to state " +job[1]+".");
            server.setControllerState(job[0], job[1]);
        }
    }

}
