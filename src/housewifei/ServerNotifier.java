package housewifei;

/**
 * ServerNotifier Object
 *
 * Used by controllers (see {@link Controller}) to communicate with a server (see {@link Server}).
 * Notifier object used to have a single server waiting on multiple controllers.
 * @author Alexe Simon and Mawait Maxime
 */

public class ServerNotifier {

    //The notified pin
    private int pin;

    //The state received
    private int state;

    public ServerNotifier() {
        pin = -1;
        state = -1;
    }

    public int getPin() {
        return pin;
    }

    public int getState() {
        return state;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMsg(int pin, int state) {
        setPin(pin);
        setState(state);
    }
}
