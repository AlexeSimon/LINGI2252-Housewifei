package util;

// Code greatly inspired from https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form, released by author to Public Domain, last visited 14 November 2018
public class BooleanExpressionEvaluator {
        protected int pos, ch;
        protected String expression;

        public BooleanExpressionEvaluator(String expression) {
            this.expression = expression;
        }

        protected void nextChar() {
            ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
        }

        protected boolean eat(int charToEat) {
            while (ch == ' ')
                nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        public boolean parse() {
            pos = -1;
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

        protected boolean parseExpression() {
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

        protected boolean parseTerm() {
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

        protected boolean parseFactor() {
            if (eat('!'))
                return !parseFactor(); // logical not

            boolean x;
            int startPos = this.pos;

            if (eat('(')) { // parentheses
                x = parseExpression();
                eat(')');
            }
            else if (ch >= '0' && ch <= '9') { // numbers
                x = parseNumber(startPos);
            }
            else {
                throw new RuntimeException("Unexpected : " + (char)ch);
            }
            return x;
        }

        protected boolean parseNumber(int startPos) {

            while (ch >= '0' && ch <= '9')
                nextChar();
            int number = Integer.parseInt(expression.substring(startPos, this.pos));
            return (number != 0);

        }
}
