# NFA / DFA Simulator in Java

By: Max Monciardini and Chase Whipple

## File Structure

```bash
.
├── fa
│   └── FAInterface.java
│   └── State.java
└── fa.test
    ├── dfa
    │   ├── DFA.java
    │   ├── DFAState.java
    │   └── DFAInterface.java
    └── nfa
        ├── NFA.java
        ├── NFAState.java
        └── NFAInterface.java
```
```
- `DFAState.java`: Represents a DFA state with functionality to track whether the state is an initial state or a final (accepting) state for the DFA.
- `FAInterface.java`: Interface for Finite Automaton functionality.
- `State.java`: Represents an abstract state, subclassed by `DFAState.java`.
- `DFA.java`: The main entity for representing a DFA. It implements `DFAInterface.java`.
- `NFAState.java`: Represents a NFA state with functionality to track whether the state is an initial state or a final (accepting) state for the NFA.
- `DFAInterface.java`: Interface containing methods specific to DFAs.
- `NFAInterface.java`: Interface containing methods specific to NFAs.
```

## DFA Project

The Deterministic Finite Automaton (DFA) Simulator is a simple, yet powerful Java application that allows you to simulate the behavior of DFAs. In the theory of computation, a DFA, as formalized by the 7-tuple (Q, Σ, δ, q0, F), is a model of computation for a machine that changes its states based on the given inputs according to the deterministic transition function, δ.

Motivated by the need to simulate and test DFAs easily, our DFA Simulator creates and allows for the manipulation of such automatons, ultimately checking whether a particular string is accepted by the DFA.

Understandability and extensibility were the guiding principles for the design of this project. The application was written and structured with clear logic and modularity in mind, enabling future enhancements with minimal effort.

# NFA Project

The non-deterministic finite automaton (NFA) Simulator, like its counterpart in deterministic finite automaton simulation, is a dynamic Java application designed to simulate the operation of NFAs. As defined in the theory of computation, an NFA, formalized by the 7-tuple (Q, Σ, δ, q0, F), allows for non-deterministic transitions, meaning that there can be multiple next states for the same current state and input symbol, or even transitions without input symbols (ε-transitions).

Our NFA Simulator offers a convenient platform to create, manipulate, and analyze NFAs. Its key objective is determining whether a specific string can be accepted by the NFA. Despite the inherent complexity of non-deterministic systems, the simulator is built to efficiently handle such processes.

## How to compile the projects

Assuming that Java is installed, follow the instructions below:

1. Navigate to your project directory.
2. Compile the Java files:
   ```
   javac fa/*.java fa/nfa/*.java fa/dfa/*.java 
   javac -cp .:/usr/share/java/junit.jar ./test/nfa/NFATest.java ./test/dfa/DFATest.java
   ```

## Running Tests

For running tests, this project uses the Gradle build system.

To run the entire test suite, navigate to the project root and execute:

   ```
   java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar org.junit.runner.JUnitCore test.dfa.DFATest
   java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar org.junit.runner.JUnitCore test.nfa.NFATest
   ```

This will run all tests and provide a summary of the results.