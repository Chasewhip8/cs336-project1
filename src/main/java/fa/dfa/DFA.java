package fa.dfa;

import fa.DFAState;
import fa.State;

import java.util.*;

public class DFA implements DFAInterface {
    // Sigma is the input alphabet
    private final Set<Character> sigma;

    // A set to store all DFA states
    private final Set<DFAState> states;

    // A transitionMap to map transition to next state
    private final HashMap<Transition, DFAState> transitionMap;

    // The initial state of the DFA
    private DFAState initialState;

    /**
     * Default constructor.
     * Initializes states, sigma, and transitionMap. The initial state is set to null.
     */
    public DFA() {
        this.sigma = new LinkedHashSet<>();
        this.states = new LinkedHashSet<>();
        this.transitionMap = new HashMap<>();
        this.initialState = null;
    }

    /**
     * Constructor that accepts sigma and states.
     * @param sigma Set of input symbols
     * @param states Set of DFA states
     */
    public DFA(Set<Character> sigma, Set<DFAState> states) {
        this.sigma = new LinkedHashSet<>(sigma);
        this.initialState = null;
        this.transitionMap = new HashMap<>();

        this.states = new LinkedHashSet<>(states.size());
        for (DFAState state : states){
            DFAState newState = state.clone();
            if (newState.isInitialState()){
                this.initialState = state;
            }
            this.states.add(newState);
        }
    }

    /**
     * Add a new state to the DFA.
     * @param name is the label of the state
     * @return true if a new state is added successfully;
     * false if a state with the same name already exists
     */
    @Override
    public boolean addState(String name) {
        DFAState newState = new DFAState(name);
        return states.add(newState);
    }

    /**
     * Set an existing state as an accepting (final) state.
     * @param name is the label of the state to be set as final
     * @return true if the state exists and is set to final;
     * false if no state with such name exists
     */
    @Override
    public boolean setFinal(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        state.setFinalState(true);
        return true;
    }

    /**
     * Set an existing state as an initial (start) state.
     * Unset the previously set initial state if any.
     * @param name is the label of the state to be set as initial
     * @return true if the state exists and is set to initial;
     * false if no state with such name exists
     */
    @Override
    public boolean setStart(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        if (initialState != null){
            initialState.setInitialState(false);
        }

        state.setInitialState(true);
        this.initialState = state;
        return true;
    }

    /**
     * Add a symbol to the alphabet Sigma.
     * @param symbol to add to the alphabet set
     */
    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    /**
     * Determines whether the DFA accepts a given input string.
     * @param s - the input string
     * @return true if the string s is accepted by the DFA;
     * false otherwise
     */
    @Override
    public boolean accepts(String s) {
        DFAState currentState = this.initialState;

        if (currentState == null){
            return false;
        }

        for (char c : s.toCharArray()) {
            Transition transition = new Transition(currentState, c);
            DFAState nextState = transitionMap.get(transition);

            if (nextState == null){
                return false;
            }

            currentState = nextState;
        }

        return currentState.isFinalState();
    }

    /**
     * Getter for Sigma
     * @return the alphabet of the DFA
     */
    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    /**
     * Returns state with the given name.
     * @param name of a state
     * @return state object if exists; null otherwise
     */
    @Override
    public State getState(String name) {
        for (DFAState state : states) {
            if (state.getName().equals(name)) {
                return state;
            }
        }
        return null;
    }

    /**
     * Determines if a state with a given name is final.
     * @param name the name of the state
     * @return true if a state with that name exists and it is final;
     * false otherwise
     */
    @Override
    public boolean isFinal(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        return state.isFinalState();
    }

    /**
     * Determines if a state with a given name is the start state.
     * @param name the name of the state
     * @return true if a state with that name exists and it is the start state;
     * false otherwise
     */
    @Override
    public boolean isStart(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        return state.isInitialState();
    }

    /**
     * Adds a transition from one state to another on a given input symbol.
     * @param fromState is the name of the state where the transition starts
     * @param toState is the name of the state where the transition ends
     * @param onSymb is the input symbol triggering the transition
     * @return true if the transition is added successfully;
     * false if one of the states doesn't exist or the symbol is not in the alphabet
     */
    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        if (!sigma.contains(onSymb)){
            return false;
        }

        DFAState state = (DFAState) getState(fromState);
        if (state == null){
            return false;
        }

        DFAState endState = (DFAState) getState(toState);
        if (endState == null){
            return false;
        }

        Transition transition = new Transition(state, onSymb);

        // Check to see if it is already in the transition map.
        // If it is we can return true if the end state is the SAME as the one we are trying to insert
        State previousEndState = transitionMap.putIfAbsent(transition, endState);
        if (previousEndState == null){
            return true;
        }
        return previousEndState.equals(endState);
    }

    /**
     * Creates a new DFA which transitions labels are
     * swapped between symbols symb1 and symb2.
     * @param symb1 the first symbol to be swapped
     * @param symb2 the second symbol to be swapped
     * @return a new copy of the DFA with swapped transitions;
     * return null if no such symbols exist
     */
    @Override
    public DFA swap(char symb1, char symb2) {
        // Verify that the transitions exist
        if (!sigma.contains(symb1) && !sigma.contains(symb2)){
            return null;
        }

        DFA newDfa = new DFA(this.sigma, this.states);

        // Create a new DFA with the swapped states
        for (Map.Entry<Transition, DFAState> entry : transitionMap.entrySet()){
            Transition currentTransition = entry.getKey();
            DFAState nextState = entry.getValue();
            if (currentTransition.getTransition() == symb1) {
                if (!newDfa.addTransition(currentTransition.getStartState().getName(), nextState.getName(), symb2)){
                    return null;
                }
            } else if (currentTransition.getTransition() == symb2) {
                if (!newDfa.addTransition(currentTransition.getStartState().getName(), nextState.getName(), symb1)){
                    return null;
                }
            } else {
                if (!newDfa.addTransition(currentTransition.getStartState().getName(), nextState.getName(), currentTransition.getTransition())){
                    return null;
                }
            }
        }
        return newDfa;
    }

    /**
     * Construct a string representation of the DFA.
     * @return a String that specifies the states, alphabets, transitions,
     * start state and final states of the DFA
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Q = { ");
        for (DFAState state : this.states) {
            sb.append(state.getName()).append(" ");
        }
        sb.append("}\n");

        sb.append("Sigma = { ");
        for (char c : this.sigma) {
            sb.append(c).append(" ");
        }
        sb.append("}\ndelta = \n	");

        for (char c : this.sigma) {
            sb.append(c).append("	");
        }
        sb.append("\n");

        for (DFAState state : this.states) {
            sb.append(state.getName());
            for (char c : this.sigma) {
                Transition transition = new Transition(state, c);
                DFAState newState = this.transitionMap.get(transition);
                if (newState == null){
                    sb.append("	ERR");
                } else {
                    sb.append("	").append(newState.getName());
                }
            }
            sb.append("\n");
        }

        if (this.initialState != null){
            sb.append("\nq0 = ").append(this.initialState.getName());
        }

        sb.append("\nF = { ");
        for (DFAState state : this.states) {
            if (!state.isFinalState()){
                continue;
            }
            sb.append(state.getName()).append(" ");
        }
        sb.append("}\n");

        return sb.toString();
    }
}
