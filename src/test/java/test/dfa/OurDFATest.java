package test.dfa;

import fa.dfa.DFA;
import fa.dfa.DFAInterface;
import org.junit.Test;

import static org.junit.Assert.*;

public class OurDFATest {

    private DFA[] dfa1() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        dfa.addSigma('1');

        dfa.addState("q0");
        dfa.addState("q1");

        dfa.setStart("q0");
        dfa.setFinal("q0");
        dfa.setFinal("q1");

        dfa.addTransition("q0", "q0", '0');
        dfa.addTransition("q0", "q1", '1');

        DFA swappedDfa = dfa.swap('0', '1');

        return new DFA[]{dfa, swappedDfa};
    }

    private DFA dfa2() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        dfa.addSigma('1');

        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.setFinal("b"));

        assertFalse(dfa.addState("a"));
        assertFalse(dfa.setStart("c"));
        assertFalse(dfa.setFinal("c"));

        assertTrue(dfa.addTransition("a", "a", '0'));
        assertTrue(dfa.addTransition("a", "b", '1'));
        assertTrue(dfa.addTransition("b", "a", '0'));
        assertTrue(dfa.addTransition("b", "b", '1'));

        assertFalse(dfa.addTransition("c", "b", '1'));
        assertFalse(dfa.addTransition("a", "c", '1'));
        assertFalse(dfa.addTransition("a", "b", '2'));

        return dfa;
    }

    @Test
    public void testSwappedDfaGetterMethods() {
        DFA[] dfas = dfa1();
        DFA dfa = dfas[0];
        DFA swappedDfa = dfas[1];

        assertNotSame(dfa.getSigma(), swappedDfa.getSigma());
        assertNotSame(dfa.getState("q0"), swappedDfa.getState("q0"));
        assertNotSame(dfa.getState("q1"), swappedDfa.getState("q1"));
    }

    @Test
    public void testEmptyDFA() {
        DFA dfa = new DFA();
        assertFalse(dfa.accepts(""));
        assertFalse(dfa.accepts("0"));
    }

    @Test
    public void testDFAWithoutSigma() {
        DFA dfa = new DFA();
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));

        assertFalse(dfa.accepts("0"));
    }

    @Test
    public void testDFAWithUnreachableStates() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.setFinal("a"));

        assertTrue(dfa.accepts(""));  // "a" is a final state, so it should accept empty strings
        assertFalse(dfa.accepts("0"));  // No transitions defined, so any non-empty string should be rejected
    }

    @Test
    public void testSingleStateDFA() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.setStart("a"));

        // Test for acceptance with a DFA that only has one state and no transitions.
        assertFalse(dfa.accepts(""));
        assertFalse(dfa.accepts("0"));

        System.out.println("singleStateDFA test pass");
    }

    @Test
    public void testSwapNonexistentSymbols() {
        DFA dfa = dfa2();
        DFAInterface swappedDFA = dfa.swap('x', 'y');
        assertNull(swappedDFA);
    }

    @Test
    public void testLongString() {
        DFA dfa = dfa2();
        String longStr = "01010101010101010101010101010101010101010101";

        // Test for acceptance with a long string.
        assertTrue(dfa.accepts(longStr));
    }

    @Test
    public void testMultipleInitialStates() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.setStart("b"));

        // Test to ensure 'b' is now the initial state, not 'a'.
        assertEquals("b", dfa.getState("b").getName());
        assertTrue(dfa.isStart("b"));
        assertFalse(dfa.isStart("a"));
    }

    @Test
    public void testTransitionsForNonExistentStates() {
        DFA dfa = new DFA();
        dfa.addSigma('0');

        assertFalse(dfa.addTransition("x", "y", '0'));

        assertTrue(dfa.addState("a"));
        assertFalse(dfa.addTransition("a", "y", '0'));
        assertFalse(dfa.addTransition("x", "a", '0'));
    }

    @Test
    public void testAddSigmaAfterTransitions() {
        DFA dfa = new DFA();
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertFalse(dfa.addTransition("a", "b", '0'));  // Should fail because '0' not in Sigma yet.

        dfa.addSigma('0');
        assertTrue(dfa.addTransition("a", "b", '0'));  // Now it should pass.
    }

    @Test
    public void testDFAWithoutFinalStates() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.setStart("a"));

        assertFalse(dfa.accepts(""));
        assertFalse(dfa.accepts("0"));
    }

    @Test
    public void testSettingNonExistentStateAsInitialOrFinal() {
        DFA dfa = new DFA();
        assertFalse(dfa.setStart("z"));
        assertFalse(dfa.setFinal("z"));
    }


    @Test
    public void testDFAWithMissingTransitions() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        dfa.addSigma('1');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.addTransition("a", "b", '0'));

        assertFalse(dfa.accepts("1"));
    }

    @Test
    public void testInvalidDFA() {
        DFA dfa = new DFA();
        dfa.addSigma('a');
        dfa.addSigma('b');

        assertTrue(dfa.addState("q0"));
        assertTrue(dfa.addState("q1"));
        assertTrue(dfa.addState("q2"));

        assertFalse(dfa.addState("q0"));
        assertFalse(dfa.addState("q1"));
        assertFalse(dfa.addState("q2"));

        assertTrue(dfa.setStart("q0"));
        assertTrue(dfa.setStart("q0"));
        assertFalse(dfa.setStart("invalid"));

        assertTrue(dfa.setFinal("q2"));
        assertTrue(dfa.setFinal("q2"));
        assertTrue(dfa.setFinal("q1"));
        assertFalse(dfa.setFinal("invalid"));

        assertTrue(dfa.addTransition("q0", "q1", 'a'));
        assertTrue(dfa.addTransition("q0", "q1", 'b'));

        assertFalse(dfa.addTransition("q0", "q1", 'c'));
        assertFalse(dfa.addTransition("q8", "q9", 'c'));

        assertFalse(dfa.addTransition("q0", "q0", 'a'));
    }

    @Test
    public void testDFAAcceptsOnlyEmptyString() {
        DFA dfa = new DFA();
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.setStart("a"));
        assertTrue(dfa.setFinal("a"));

        assertTrue(dfa.accepts(""));
        assertFalse(dfa.accepts("0"));
        assertFalse(dfa.accepts("1"));
    }

    @Test
    public void testDFARejectsAll() {
        DFA dfa = new DFA();
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.setStart("a"));

        assertFalse(dfa.accepts(""));
        assertFalse(dfa.accepts("0"));
        assertFalse(dfa.accepts("1"));
    }

    @Test
    public void testNonDeterministicBehavior() {
        DFA dfa = new DFA();
        dfa.addSigma('0');
        dfa.addSigma('1');
        assertTrue(dfa.addState("a"));
        assertTrue(dfa.addState("b"));
        assertTrue(dfa.addState("c"));
        assertTrue(dfa.setStart("a"));

        assertTrue(dfa.addTransition("a", "b", '0'));
        assertFalse(dfa.addTransition("a", "c", '0'));  // This is an NFA behavior. The DFA should not allow this.

        assertFalse(dfa.accepts(""));  // No final states, so even the empty string should be rejected.
        assertFalse(dfa.accepts("0"));
    }
}
