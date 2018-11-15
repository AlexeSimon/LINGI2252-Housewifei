package util;

/* code inspired by https://www.tutorialspoint.com/design_pattern/chain_of_responsibility_pattern.htm, last visited 15/11/2018 */
public class FileLogger extends AbstractLogger {

    public FileLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("File::Logger: " + message);
    }

}
