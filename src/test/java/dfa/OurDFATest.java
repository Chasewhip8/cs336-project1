package dfa;

import fa.DFAState;
import fa.dfa.DFA;
import fa.dfa.DFAInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OurDFATest {

// Test 1: Test Deep cloning

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

    // Test getter methods
    @Test
    public void test1_1() {
        DFA[] dfas = dfa1();
        DFA dfa = dfas[0];
        DFA swappedDfa = dfas[1];

        assertNotSame(dfa.getSigma(), swappedDfa.getSigma());
        assertNotSame(dfa.getState("q0"), swappedDfa.getState("q0"));
        assertNotSame(dfa.getState("q1"), swappedDfa.getState("q1"));

    }

    // Test toString methods
//    @Test
//    public void test1_2() {
//
//    }

    // Test 2: Ensure that the sigma characters are returned in the order that they were added
    private DFA dfa2() {
        DFA dfa = new DFA();
        dfa.addSigma('a');
        dfa.addSigma('b');

        dfa.addState("q0");
        dfa.addState("q1");
        dfa.addState("q2");

        dfa.setStart("q0");
        dfa.setFinal("q2");

        dfa.addTransition("q0", "q1", 'a');
        dfa.addTransition("q0", "q1", 'b');

        return dfa;
    }

    // Test the getter methods
    @Test
    public void test2_1() {

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
