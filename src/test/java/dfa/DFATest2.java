package dfa;

import java.util.Set;

import fa.dfa.DFA;
import fa.dfa.DFAInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DFATest2 {

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

    // Test the toString methods


}
