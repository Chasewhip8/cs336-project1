package test.nfa;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import fa.nfa.NFA;

public class OurNFATest {
    private NFA dfa1() {
        NFA dfa = new NFA();
        dfa.addSigma('0');
        dfa.addSigma('1');

        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.setFinal("b"));

        assertFalse(dfa.addState("a"));
        assertFalse(dfa.setStart("c"));
        assertFalse(dfa.setFinal("c"));

        assertTrue(dfa.addTransition("a", Set.of("a"), '0'));
        assertTrue(dfa.addTransition("a", Set.of("b"), '1'));
        assertTrue(dfa.addTransition("b", Set.of("a"), '0'));
        assertTrue(dfa.addTransition("b", Set.of("b"), '1'));

        assertFalse(dfa.addTransition("c", Set.of("b"), '1'));
        assertFalse(dfa.addTransition("a", Set.of("c"), '1'));
        assertFalse(dfa.addTransition("a", Set.of("b"), '2'));

        return dfa;
    }

    @Test
    public void testAddExistingState() {
        NFA nfa = basicNFA();
        assertFalse(nfa.addState("s1"));
    }

    @Test
    public void testAddTransitionToNonExistingState() {
        NFA nfa = basicNFA();
        assertFalse(nfa.addTransition("s1", Set.of("s3"), 'a'));
    }

    @Test
    public void testAddTransitionWithNonExistingSymbol() {
        NFA nfa = basicNFA();
        assertFalse(nfa.addTransition("s1", Set.of("s2"), 'z'));
    }

    @Test
    public void testTransitionWithEpsilon() {
        NFA nfa = basicNFA();
        assertTrue(nfa.addTransition("s1", Set.of("s2"), 'e'));
    }

    @Test
    public void testEClosureWithEpsilonTransition() {
        NFA nfa = basicNFA();
        nfa.addTransition("s1", Set.of("s2"), 'e');
        assertEquals(Set.of(nfa.getState("s1"), nfa.getState("s2")), nfa.eClosure(nfa.getState("s1")));
    }

    @Test
    public void testIsDFA() {
        NFA nfa = basicNFA();
        assertFalse(nfa.isDFA());
    }

    @Test
    public void testAccepts() {
        NFA nfa = basicNFA();
        assertTrue(nfa.accepts("a"));
        assertFalse(nfa.accepts("b"));
        assertFalse(nfa.accepts("aa"));
    }

    @Test
    public void testMaxCopies() {
        NFA nfa = basicNFA();
        assertEquals(1, nfa.maxCopies("a"));
        assertEquals(1, nfa.maxCopies("b"));
        assertEquals(1, nfa.maxCopies("aa"));
    }

    @Test
    public void testIsStartAndIsFinal() {
        NFA nfa = basicNFA();
        assertTrue(nfa.isStart("s1"));
        assertFalse(nfa.isStart("s2"));
        assertFalse(nfa.isFinal("s1"));
        assertTrue(nfa.isFinal("s2"));
    }

    @Test
    public void testMultipleTransitionsFromOneState() {
        NFA nfa = new NFA();

        nfa.addSigma('b');
        assertTrue(nfa.addState("s1"));
        assertTrue(nfa.setStart("s1"));
        assertTrue(nfa.addState("s2"));
        assertTrue(nfa.setFinal("s2"));
        assertTrue(nfa.addState("s3"));
        assertTrue(nfa.addTransition("s1", Set.of("s2"), 'b'));
        assertTrue(nfa.addTransition("s1", Set.of("s3"), 'b'));
        assertTrue(nfa.accepts("b"));
    }


    @Test
    public void testTransitionToMultipleStates() {
        NFA nfa = basicNFA();
        assertTrue(nfa.addState("s3"));
        assertTrue(nfa.addTransition("s1", Set.of("s2", "s3"), 'a'));
        assertTrue(nfa.accepts("a"));
    }

    @Test
    public void testEClosureWithMultipleEpsilonTransitions() {
        NFA nfa = basicNFA();
        nfa.addState("s3");
        nfa.addTransition("s1", Set.of("s3"), 'e');
        nfa.addTransition("s3", Set.of("s2"), 'e');
        assertEquals(Set.of(nfa.getState("s1"), nfa.getState("s2"), nfa.getState("s3")), nfa.eClosure(nfa.getState("s1")));
    }

    @Test
    public void testSetInitialStateToExistingFinalState() {
        NFA nfa = basicNFA();
        assertTrue(nfa.setStart("s2"));
        assertTrue(nfa.isStart("s2"));
        assertFalse(nfa.isStart("s1"));
    }

    @Test
    public void testSetFinalStateToExistingInitialState() {
        NFA nfa = basicNFA();
        assertTrue(nfa.setFinal("s1"));
        assertTrue(nfa.isFinal("s1"));
    }

    @Test
    public void testMultipleSymbolsInSequence() {
        NFA nfa = new NFA();

        // Setting up sigma
        nfa.addSigma('a');
        nfa.addSigma('b');

        // Setting up states
        assertTrue(nfa.addState("s1"));
        assertTrue(nfa.setStart("s1"));
        assertTrue(nfa.addState("s2"));
        assertTrue(nfa.addState("s3"));
        assertTrue(nfa.setFinal("s3"));

        // Adding transitions
        assertTrue(nfa.addTransition("s1", Set.of("s2"), 'a'));
        assertTrue(nfa.addTransition("s2", Set.of("s3"), 'b'));

        // Check if the NFA accepts the string "ab"
        assertTrue(nfa.accepts("ab"));

        // Check if the NFA rejects the string "ba"
        assertFalse(nfa.accepts("ba"));
    }

    @Test
    public void testEpsilonTransitions() {
        NFA nfa = new NFA();

        // Setting up sigma
        nfa.addSigma('a');

        // Setting up states
        assertTrue(nfa.addState("s1"));
        assertTrue(nfa.setStart("s1"));
        assertTrue(nfa.addState("s2"));
        assertTrue(nfa.addState("s3"));
        assertTrue(nfa.setFinal("s3"));

        // Adding transitions (epsilon transition represented by null)
        assertTrue(nfa.addTransition("s1", Set.of("s2"), 'e'));
        assertTrue(nfa.addTransition("s2", Set.of("s3"), 'a'));

        // Check if the NFA accepts the string "a"
        assertTrue(nfa.accepts("a"));

        // Check if the NFA rejects the string "aa"
        assertFalse(nfa.accepts("aa"));
    }

    @Test
    public void testCyclesInNFA() {
        NFA nfa = new NFA();

        // Setting up sigma
        nfa.addSigma('a');

        // Setting up states
        assertTrue(nfa.addState("s1"));
        assertTrue(nfa.setStart("s1"));
        assertTrue(nfa.addState("s2"));
        assertTrue(nfa.addState("s3"));
        assertTrue(nfa.setFinal("s3"));

        // Adding transitions
        assertTrue(nfa.addTransition("s1", Set.of("s2"), 'a'));
        assertTrue(nfa.addTransition("s2", Set.of("s2", "s3"), 'a')); // cycle on s2

        // Check if the NFA accepts the string "aa"
        assertTrue(nfa.accepts("aa"));

        // Check if the NFA rejects the string "a"
        assertFalse(nfa.accepts("a"));
    }
}
