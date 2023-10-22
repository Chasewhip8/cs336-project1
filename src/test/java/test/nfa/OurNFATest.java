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

    private NFA createSimpleNFA() {
        NFA nfa = new NFA();
        nfa.addSigma('a');
        nfa.addSigma('b');
        nfa.addState("q0");
        nfa.setStart("q0");
        nfa.addState("q1");
        nfa.addTransition("q0", Set.of("q0", "q1"), 'a');
        nfa.addTransition("q1", Set.of("q1"), 'b');
        nfa.setFinal("q1");
        return nfa;
    }

    private NFA createEpsilonNFA() {
        NFA nfa = new NFA();
        nfa.addSigma('a');
        nfa.addSigma('b');
        nfa.addState("A");
        nfa.setStart("A");
        nfa.addState("B");
        nfa.addTransition("A", Set.of("B"), 'e');
        nfa.addState("C");
        nfa.addTransition("B", Set.of("C"), 'a');
        nfa.setFinal("C");
        return nfa;
    }

    @Test
    public void testSimpleNFAInstantiation() {
        NFA nfa = createSimpleNFA();
        assertNotNull(nfa);
    }

    @Test
    public void testSimpleNFAAcceptance() {
        NFA nfa = createSimpleNFA();
        assertTrue(nfa.accepts("a"));
        assertTrue(nfa.accepts("aa"));
        assertTrue(nfa.accepts("aab"));
        assertFalse(nfa.accepts("b"));
        assertFalse(nfa.accepts("abab"));
    }

    @Test
    public void testSimpleNFAMaxCopies() {
        NFA nfa = createSimpleNFA();
        assertEquals(2, nfa.maxCopies("a"));
        assertEquals(2, nfa.maxCopies("aa"));
        assertEquals(2, nfa.maxCopies("aab"));
        assertEquals(1, nfa.maxCopies("b"));
        assertEquals(2, nfa.maxCopies("abab"));
    }

    @Test
    public void testEpsilonNFAInstantiation() {
        NFA nfa = createEpsilonNFA();
        assertNotNull(nfa);
    }

    @Test
    public void testEpsilonNFAAcceptance() {
        NFA nfa = createEpsilonNFA();
        assertTrue(nfa.accepts("a"));
        assertFalse(nfa.accepts("b"));
        assertFalse(nfa.accepts("ab"));
    }

    @Test
    public void testEpsilonNFAMaxCopies() {
        NFA nfa = createEpsilonNFA();
        assertEquals(2, nfa.maxCopies("a"));
        assertEquals(2, nfa.maxCopies("b"));
        assertEquals(2, nfa.maxCopies("ab"));
    }

    @Test
    public void testInvalidTransitions() {
        NFA nfa = createSimpleNFA();
        assertFalse(nfa.addTransition("q0", Set.of("q2"), 'a'));
        assertFalse(nfa.addTransition("q2", Set.of("q1"), 'b'));
        assertFalse(nfa.addTransition("q0", Set.of("q1"), 'c'));
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

    @Test
    public void testIsDFAWithSingleStateAndEpsilonTransition() {
        NFA nfa = new NFA();
        nfa.addSigma('a');
        nfa.addState("s1");
        nfa.setStart("s1");
        nfa.setFinal("s1");
        nfa.addTransition("s1", Set.of("s1"), 'e');  // self-loop epsilon transition
        assertFalse(nfa.isDFA());  // Presence of epsilon transitions makes it not a DFA
    }

    @Test
    public void testIsDFAWithSingleStateAndValidTransitions() {
        NFA nfa = new NFA();
        nfa.addSigma('a');
        nfa.addState("s1");
        nfa.setStart("s1");
        nfa.setFinal("s1");
        nfa.addTransition("s1", Set.of("s1"), 'a');  // self-loop on 'a'
        assertTrue(nfa.isDFA());  // It behaves like a DFA
    }

    @Test
    public void testIsDFAWithAnExistingDFA() {
        NFA dfaLike = dfa1();
        assertTrue(dfaLike.isDFA());
    }
}
