package fa.dfa;

import fa.DFAState;

import java.util.Objects;

public class Transition {
    private final DFAState startState;
    private final char transition;

    public Transition(DFAState startState, char transition) {
        this.startState = startState;
        this.transition = transition;
    }

    public DFAState getStartState() {
        return startState;
    }

    public char getTransition() {
        return transition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return transition == that.transition && Objects.equals(startState, that.startState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startState, transition);
    }
}
