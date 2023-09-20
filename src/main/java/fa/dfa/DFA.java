package fa.dfa;

import fa.DFAState;
import fa.State;

import java.util.*;

public class DFA implements DFAInterface {
    private final Set<Character> sigma;
    private final Set<DFAState> states;
    private final HashMap<Transition, DFAState> transitionMap;

    private DFAState initialState;

    public DFA() {
        this.sigma = new LinkedHashSet<>();
        this.states = new LinkedHashSet<>();
        this.transitionMap = new HashMap<>();
        this.initialState = null;
    }

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

    @Override
    public boolean addState(String name) {
        DFAState newState = new DFAState(name);
        return states.add(newState);
    }

    @Override
    public boolean setFinal(String name) {
        return false;
    }

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

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

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

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public State getState(String name) {
        for (DFAState state : states) {
            if (state.getName().equals(name)) {
                return state;
            }
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        return state.isFinalState();
    }

    @Override
    public boolean isStart(String name) {
        DFAState state = (DFAState) getState(name);
        if (state == null){
            return false;
        }

        return state.isInitialState();
    }

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
        transitionMap.putIfAbsent(transition, endState);
        return true;
    }

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
