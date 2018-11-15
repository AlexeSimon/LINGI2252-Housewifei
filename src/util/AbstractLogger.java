package util;

/* code inspired by https://www.tutorialspoint.com/design_pattern/chain_of_responsibility_pattern.htm, last visited 15/11/2018 */
public abstract class AbstractLogger {

    final static int INFO = 1;
    final static int DEBUG = 2;
    final static int ERROR = 3;

    protected int level;

    protected AbstractLogger nextLogger;

    public void setNextLogger(AbstractLogger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message) {
        if (this.level <= level)
            write(message);
        if (nextLogger != null)
            nextLogger.logMessage(level, message);
    }

    abstract protected void write(String message);
}
