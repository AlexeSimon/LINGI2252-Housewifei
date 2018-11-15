package housewifei;

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

    public boolean evaluateExpression(Server server) {
        // Code greatly inspired from https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form, eleased by author to Public Domain, last visited 14 November 2018
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            boolean parse() {
                nextChar();
                boolean x = parseExpression();
                if (pos < expression.length())
                    throw new RuntimeException("Unexpected : " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `^` term | expression `|` term
            // term = factor | term '&' factor
            // factor = '!' factor | `(` expression `)` | number '_' number
            // () > NOT > AND > XOR = OR

            boolean parseExpression() {
                boolean x = parseTerm();
                while(true) {
                    if(eat('^')) {
                        x = (parseTerm() ^ x); // xor
                    }
                    else if (eat('|')) {
                        x = (parseTerm() || x); // or
                    }
                    else {
                        return x;
                    }
                }
            }

            boolean parseTerm() {
                boolean x = parseFactor();
                while (true) {
                    if(eat('&')) { ;
                        x = (parseFactor() && x); // and
                    }
                    else {
                        return x;
                    }
                }
            }

            boolean parseFactor() {
                if (eat('!'))
                    return !parseFactor(); // logical not

                boolean x;
                int tempPin;
                int tempState;
                int delta;
                int startPos = this.pos;

                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                }
                else if (ch >= '0' && ch <= '9') { // numbers
                    while (ch >= '0' && ch <= '9') nextChar();
                    tempPin = Integer.parseInt(expression.substring(startPos, this.pos));
                    if(!eat('_'))
                        throw new RuntimeException("Unexpected : " + (char)ch);
                    delta = this.pos;
                    while (ch >= '0' && ch <= '9') nextChar();
                    tempState = Integer.parseInt(expression.substring(delta, this.pos));
                    x = server.isControllerInState(tempPin, tempState);
                }
                else {
                    throw new RuntimeException("Unexpected : " + (char)ch);
                }
                return x;
            }
        }.parse();
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
