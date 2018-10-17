public class EnvironmentSimulation {
    int state;

    public EnvironmentSimulation() {
        this.state = 0;
    }

    public EnvironmentSimulation(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            notifyAll();
        }
    }

}
