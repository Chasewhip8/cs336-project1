package fa;

public class DFAState extends State implements Cloneable {
    private boolean initialState;
    private boolean finalState;

    public DFAState(String name) {
        super(name);
        this.initialState = false;
        this.finalState = false;
    }

    public boolean isInitialState() {
        return initialState;
    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State state) {
            return this.getName().equals(state.getName());
        }
        return false;
    }

    @Override
    public DFAState clone() {
        try {
            return (DFAState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
