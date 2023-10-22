package fa.nfa;

import fa.State;

import java.util.*;

/**
 * This class represents a Non-Deterministic Finite Automaton (NFA) and provides
 * methods to manipulate and query its states, transitions, and behaviors.
 * <p>
 * An NFA is defined by:
 * <ul>
 *     <li>An input alphabet, Sigma (σ), which also includes the epsilon (ε) transition</li>
 *     <li>A finite set of states</li>
 *     <li>A transition function that determines the set of next states for a given state and input symbol</li>
 *     <li>An initial (or start) state</li>
 *     <li>A set of final (or accepting) states</li>
 * </ul>
 * This class also provides functionality to add states, define transitions, set initial and final states,
 * and evaluate whether a given string can be accepted by the NFA through any of its possible paths.
 * </p>
 * <p>
 * Additionally, it supports operations like computing the epsilon closure of a state and determining
 * the maximum number of active states the NFA can be in after processing a given string.
 * </p>
 * <p>
 * Note: This implementation uses {@link LinkedHashSet} to maintain the order of insertion for states and the alphabet.
 * Transitions are stored in a {@link HashMap} for efficient lookup. Due to its non-deterministic nature, an NFA can
 * transition to multiple states for a given input symbol, or even without any input through epsilon transitions.
 * </p>
 *
 * @author Max Monciardini and Chase Whipple
 * @see NFAState
 * @see NFAInterface
 */
public class NFA implements NFAInterface {
    // Sigma is the input alphabet
    private final Set<Character> sigma;

    // A set to store all DFA states
    private final Set<NFAState> states;

    // A transitionMap to map transition to next state
    private final HashMap<Transition, Set<NFAState>> transitionMap;

    // The initial state of the DFA
    private NFAState initialState;

    /**
     * Default constructor.
     * Initializes states, sigma, and transitionMap. The initial state is set to null.
     */
    public NFA() {
        this.sigma = new LinkedHashSet<>();
        this.sigma.add('e');

        this.states = new LinkedHashSet<>();
        this.transitionMap = new HashMap<>();
        this.initialState = null;
    }

    /**
     * Constructor that accepts sigma and states.
     * @param sigma Set of input symbols
     * @param states Set of DFA states
     */
    public NFA(Set<Character> sigma, Set<NFAState> states) {
        this.sigma = new LinkedHashSet<>(sigma);
        this.initialState = null;
        this.transitionMap = new HashMap<>();

        this.states = new LinkedHashSet<>(states.size());
        for (NFAState state : states){
            NFAState newState = state.clone();
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
        NFAState newState = new NFAState(name);
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
        NFAState state = (NFAState) getState(name);
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
        NFAState state = (NFAState) getState(name);
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
    public NFAState getState(String name) {
        for (NFAState state : states) {
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
        NFAState state = (NFAState) getState(name);
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
        NFAState state = (NFAState) getState(name);
        if (state == null){
            return false;
        }

        return state.isInitialState();
    }

    /**
     * Determines if a given string is accepted by the NFA.
     * This method uses a breadth-first search strategy to traverse the NFA and
     * check if it reaches a final state with the given input string.
     * @param s the string to be checked
     * @return true if the string is accepted; false otherwise
     */
    @Override
    public boolean accepts(String s) {
        if (initialState == null){
            return false;
        }

        Queue<SearchState> states = new ArrayDeque<>();
        states.add(new SearchState(s, initialState));

        while (!states.isEmpty()){
            SearchState searchState = states.remove();

            if (processEpsilonState(states, searchState) || processStates(states, searchState)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Computes the maximum number of active states the NFA can be in after processing
     * a given string. This is essentially the "width" of the NFA at its broadest point during
     * the processing of the string.
     * @param s the input string
     * @return the maximum number of active states
     */
    @Override
    public int maxCopies(String s) {
        if (initialState == null){
            return 0;
        }

        // All initial states
        Set<NFAState> activeStates = new HashSet<>();
        activeStates.add(initialState);
        activeStates.addAll(eClosure(initialState));

        int maxCount = activeStates.size();

        // Build entire tree out and find the maximum width while doing so
        for (char symbol : s.toCharArray()) {
            Set<NFAState> nextStates = new HashSet<>();
            for (NFAState currentState : activeStates) {
                Set<NFAState> directTransitions = getToState(currentState, symbol);
                if (directTransitions != null) {
                    nextStates.addAll(directTransitions);
                }

                // Now, for each of the direct transitions, compute the eClosure
                for (NFAState nextState : new HashSet<>(nextStates)) {
                    nextStates.addAll(eClosure(nextState));
                }
            }
            activeStates = nextStates;

            // Update the max count if current active states are greater
            maxCount = Math.max(maxCount, activeStates.size());
        }

        return maxCount;
    }

    /**
     * Processes the given state with the next symbol in the input string and
     * adds the resulting states to the queue for further processing.
     * This method is used during the breadth-first search traversal in the `accepts` method.
     * @param states the queue of states to be processed
     * @param searchState the current state and the remaining part of the input string
     * @return true if the NFA reaches a final state with the remaining input string; false otherwise
     */
    private boolean processStates(Queue<SearchState> states, SearchState searchState) {
        char symbol = searchState.getSearchString().charAt(0);
        NFAState state = searchState.getCurrentState();

        Set<NFAState> nextStates = getToState(state, symbol);
        if (nextStates == null){
            return false;
        }

        // This string has the char we are currently on consumed.
        String nextString = searchState.getSearchString().substring(1);
        if (nextString.isEmpty()){
            for (NFAState nextState : nextStates) {
                if (nextState.isFinalState()) {
                    return true;
                }
            }
            return false;
        }

        for (NFAState nextState : nextStates) {
            states.add(new SearchState(nextString, nextState));
        }
        return false;
    }

    /**
     * Processes the given state with the next symbol in the input string and
     * adds the resulting states to the queue for further processing.
     * This method is used during the breadth-first search traversal in the `accepts` method.
     * @param states the queue of states to be processed
     * @param searchState the current state and the remaining part of the input string
     * @return true if the NFA reaches a final state with the remaining input string; false otherwise
     */
    private boolean processEpsilonState(Queue<SearchState> states, SearchState searchState) {
        Set<NFAState> nextStates = eClosure(searchState.getCurrentState());

        if (nextStates == null){
            return false;
        }

        // This string has the char we are currently on consumed.
        for (NFAState nextState : nextStates) {
            if (nextState.isFinalState() && searchState.getSearchString().isEmpty()){
                return true;
            }

            if (nextState != searchState.getCurrentState()){
                states.add(new SearchState(searchState.getSearchString(), nextState));
            }
        }
        return false;
    }

    /**
     * Retrieves the set of states the NFA transitions to from a given state
     * on a specific input symbol.
     * @param from the starting state
     * @param onSymb the input symbol
     * @return the set of states reachable from the starting state on the input symbol
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        Transition transition = new Transition(from, onSymb);
        return transitionMap.get(transition);
    }

    /**
     * Computes the epsilon closure of a given state. The epsilon closure is the set of
     * states reachable from the given state only by epsilon transitions.
     * @param s the starting state
     * @return the set of states in the epsilon closure of the given state
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> eClosure = new HashSet<>();
        Stack<NFAState> stack = new Stack<>();

        stack.push(s);
        while (!stack.isEmpty()) {
            NFAState currentState = stack.pop();
            eClosure.add(currentState);

            Set<NFAState> maybeNextStates = getToState(currentState, 'e');
            if (maybeNextStates == null){
                continue;
            }

            stack.addAll(maybeNextStates);
        }
        return eClosure;
    }

    /**
     * Adds a transition from one state to another on a given input symbol.
     * @param fromState is the name of the state where the transition starts
     * @param toStates is the name of the states where the transition ends
     * @param onSymb is the input symbol triggering the transition
     * @return true if the transition is added successfully;
     * false if one of the states doesn't exist or the symbol is not in the alphabet
     */
    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        if (!sigma.contains(onSymb)){
            return false;
        }

        NFAState state = (NFAState) getState(fromState);
        if (state == null){
            return false;
        }

        for (String toState : toStates) {
            NFAState endState = (NFAState) getState(toState);
            if (endState == null){
                return false;
            }

            Transition transition = new Transition(state, onSymb);
            transitionMap.putIfAbsent(transition, new HashSet<>());

            Set<NFAState> stateList = transitionMap.get(transition);
            stateList.add(endState);
        }

        return true;
    }

    /**
     * Determines if the current NFA behaves like a DFA.
     * An NFA behaves like a DFA if, for every state and every symbol in the alphabet (excluding epsilon),
     * there is exactly one transition.
     *
     * @return true if the NFA behaves like a DFA; false otherwise.
     */
    @Override
    public boolean isDFA() {
        for (Map.Entry<Transition, Set<NFAState>> entry : transitionMap.entrySet()) {
            if (entry.getKey().transition == 'e' || entry.getValue().size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * This class represents a transition in a deterministic finite automaton (DFA).
     * A transition is defined by a starting state and a character symbol, which
     * guides the transition to the next state.
     * @author Max Monciardini and Chase Whipple
     */
    public static class Transition {
        // The state from which the transition originates.
        private final State state;

        // The input symbol that triggers the transition.
        private final char transition;

        /**
         * Creates a new Transition object from a starting state and a transition symbol.
         *
         * @param state The state from which the transition originates.
         * @param transition The input symbol that triggers the transition.
         */
        public Transition(State state, char transition) {
            this.state = state;
            this.transition = transition;
        }

        /**
         * This method returns the current state from where the DFA is transiting.
         *
         * @return The state from which the transition originates.
         */
        public State getState() {
            return state;
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
            return transition == that.transition && Objects.equals(state, that.state);
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
            return Objects.hash(state, transition);
        }
    }

    /**
     * This class represents a state of the search in the non-deterministic finite automaton (NFA).
     * It contains a search string and the current state of the automaton.
     */
    public static class SearchState {
        // Represents the string for which we are searching in some context.
        private final String searchString;

        // Represents the current state of the NFA during a search operation.
        private final NFAState currentState;

        /**
         * This constructor initializes a new instance of the SearchState class.
         *
         * @param searchString A String that represents the search term.
         * @param currentState A NFAState representing the current state of the non-deterministic finite automaton.
         */
        public SearchState(String searchString, NFAState currentState) {
            this.searchString = searchString;
            this.currentState = currentState;
        }

        /**
         * This method returns the search string for this instance of `SearchState`.
         *
         * @return searchString The search string.
         */
        public String getSearchString() {
            return searchString;
        }

        /**
         * This method returns the current state of the non-deterministic finite automaton.
         *
         * @return currentState The current state of the NFA.
         */
        public NFAState getCurrentState() {
            return currentState;
        }

        /**
         * This method overrides the equals method from the Object superclass.
         * It checks for equality based on the searchString and currentState properties of this object.
         *
         * @param o An Object that we want to compare to the current instance.
         * @return A boolean that is true if the objects are equal and false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchState that = (SearchState) o;
            return Objects.equals(searchString, that.searchString) && Objects.equals(currentState, that.currentState);
        }

        /**
         * This method overrides the hashCode method from the Object superclass.
         * It returns a hash code value for this object, which is a function of the searchString and currentState.
         *
         * @return An int representing the hash code value of this object.
         */
        @Override
        public int hashCode() {
            return Objects.hash(searchString, currentState);
        }
    }
}
