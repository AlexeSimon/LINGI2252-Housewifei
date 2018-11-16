package util;

import java.util.Date;

/* inspired from https://www.tutorialspoint.com/design_pattern/chain_of_responsibility_pattern.htm, https://www.tutorialspoint.com/design_pattern/singleton_pattern.htm and https://www.tutorialspoint.com/design_pattern/mediator_pattern.htm, last visited 16/11/2018 */
public class PrintMediator implements PrintPriority {
    private static PrintMediator instance = new PrintMediator();

    private AbstractLogger logger;
    private int priorityMin = 0;
    private int priorityMax = 0;

    private PrintMediator() {
        AbstractLogger errorLogger = new ErrorLogger(PrintPriority.ERROR);
        AbstractLogger fileLogger = new FileLogger(PrintPriority.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(PrintPriority.INFO);

        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);
        this.logger = errorLogger;
    }

    public static PrintMediator getInstance() {
        return instance;
    }

    public void showMessage(TalkativeObject sender, String message, int level) {
        if (!sender.isSilent() && level >= priorityMin && (level <= priorityMax || priorityMax == 0))
            logger.logMessage(level, new Date().toString() + " [" + sender.getName() + "] : " + message);
    }

    public int getPriorityMin() {
        return priorityMin;
    }

    public void setPriorityMin(int priorityMin) {
        this.priorityMin = priorityMin;
    }

    public int getPriorityMax() {
        return priorityMax;
    }

    public void setPriorityMax(int priorityMax) {
        this.priorityMax = priorityMax;
    }
}
