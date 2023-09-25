package fa.dfa;

import fa.DFAState;

import java.util.Objects;

/**
 * This class represents a transition in a deterministic finite automaton (DFA).
 * A transition is defined by a starting state and a character symbol, which
 * guides the transition to the next state.
 */
public class Transition {
    // The state from which the transition originates.
    private final DFAState startState;

    // The input symbol that triggers the transition.
    private final char transition;

    /**
     * Creates a new Transition object from a starting state and a transition symbol.
     *
     * @param startState The state from which the transition originates.
     * @param transition The input symbol that triggers the transition.
     */
    public Transition(DFAState startState, char transition) {
        this.startState = startState;
        this.transition = transition;
    }

    /**
     * This method returns the current state from where the DFA is transiting.
     *
     * @return The state from which the transition originates.
     */
    public DFAState getStartState() {
        return startState;
    }

    /**
     * This method returns the character symbol that this Transition object
     * represents.
     *
     * @return The input symbol that triggers the transition.
     */
    public char getTransition() {
        return transition;
    }

    /**
     * This method checks if this transition is equivalent to a given object.
     * A transition is deemed equivalent if and only if the object is also a
     * Transition, and their startState fields are equivalent as per the
     * DFAState class's equals method, and their transition fields have the
     * same value.
     *
     * @param o The object with which this transition is to be compared with.
     * @return true if the object is equivalent to this transition; false
     * otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return transition == that.transition && Objects.equals(startState, that.startState);
    }

    /**
     * This method returns the hash code of this transition object.
     * The hash code of a transition is calculated based on its startState
     * and transition fields.
     *
     * @return The hash code of this transition.
     */
    @Override
    public int hashCode() {
        return Objects.hash(startState, transition);
    }
}
