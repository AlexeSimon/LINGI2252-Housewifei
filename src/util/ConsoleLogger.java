package util;

/* code inspired by https://www.tutorialspoint.com/design_pattern/chain_of_responsibility_pattern.htm, last visited 15/11/2018 */
public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Standard Console::Logger: " + message);
    }

}
