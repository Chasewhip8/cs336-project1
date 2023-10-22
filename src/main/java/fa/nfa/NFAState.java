package fa.nfa;

import fa.State;

/**
 * This class represents a DFA state.
 * This subclass of State has added functionality to track whether the state is an initial state
 * or a final (accepting) state for a deterministic finite automaton (DFA).
 * It also implements the Cloneable interface, indicating that instances of this class can be cloned.
 *
 * @author elenasherman
 */
public class NFAState extends State implements Cloneable {
    // A boolean flag indicating whether this State is the initial state of the DFA.
    private boolean initialState;

    // A boolean flag indicating whether this State is a final or accepting state of the DFA.
    private boolean finalState;

    /**
     * Constructor takes a name and initializes a NFAState object
     * with initialState and finalState set to false.
     *
     * @param name The name of the NFAState.
     */
    public NFAState(String name) {
        super(name);
        this.initialState = false;
        this.finalState = false;
    }

    /**
     * Check if the NFAState is the initial state.
     *
     * @return True if it is the initial state, otherwise false.
     */
    public boolean isInitialState() {
        return initialState;
    }

    /**
     * Set whether or not the NFAState is the initial state.
     *
     * @param initialState A boolean value to set whether this is an initial state.
     */
    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }

    /**
     * Check if the NFAState is a final state.
     *
     * @return True if it is the final state, otherwise false.
     */
    public boolean isFinalState() {
        return finalState;
    }

    /**
     * Set whether or not the NFAState is a final state.
     *
     * @param finalState A boolean value to set whether this is a accepting state.
     */
    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    /**
     * Overridden hashCode method to provide a unique hash based on state name.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * Compares this state to the specified object, checking if they are logically equivalent.
     * The result is true only if the argument is not null and is a State object that has the same name as this object.
     *
     * @param obj The object to compare this State against.
     * @return True if the given object represents a State equivalent to this state, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            return this.getName().equals(((State) obj).getName());
        }
        return false;
    }

    /**
     * Creates and returns a copy of this NFAState instance.
     * The precise meaning of "copy" may depend on the class. The general intent is that, for any object x,
     * the expression x.clone() != x will be true, and the expression x.clone().getClass() == x.getClass()
     * will be true, but these are not absolute requirements.
     *
     * @return A clone of this instance.
     * @throws AssertionError If the object's class does not support the Cloneable interface.
     */
    @Override
    public NFAState clone() {
        try {
            return (NFAState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
