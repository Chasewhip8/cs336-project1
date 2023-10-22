package fa.nfa;

import java.util.Objects;

public class SearchState {
    private final String searchString;
    private final NFAState currentState;

    public SearchState(String searchString, NFAState currentState) {
        this.searchString = searchString;
        this.currentState = currentState;
    }

    public String getSearchString() {
        return searchString;
    }

    public NFAState getCurrentState() {
        return currentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchState that = (SearchState) o;
        return Objects.equals(searchString, that.searchString) && Objects.equals(currentState, that.currentState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchString, currentState);
    }
}
