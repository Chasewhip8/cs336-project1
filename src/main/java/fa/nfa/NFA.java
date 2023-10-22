package fa.nfa;

import fa.Transition;

import java.util.*;

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

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        Transition transition = new Transition(from, onSymb);
        return transitionMap.get(transition);
    }

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

    @Override
    public boolean isDFA() {
        return false;
    }
}
