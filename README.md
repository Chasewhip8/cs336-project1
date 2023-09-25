# DFA Simulator in Java

By: Max Monciardini and Chase Whipple

## Overview

The Deterministic Finite Automaton (DFA) Simulator is a simple, yet powerful Java application that allows you to simulate the behavior of DFAs. In the theory of computation, a DFA, as formalized by the 7-tuple (Q, Σ, δ, q0, F), is a model of computation for a machine that changes its states based on the given inputs according to the deterministic transition function, δ.

Motivated by the need to simulate and test DFAs easily, our DFA Simulator creates and allows for the manipulation of such automatons, ultimately checking whether a particular string is accepted by the DFA.

Understandability and extensibility were the guiding principles for the design of this project. The application was written and structured with clear logic and modularity in mind, enabling future enhancements with minimal effort.

## File Structure

The codebase is logically separated into various packages and classes:

```bash
.
├── fa
│   ├── DFAState.java
│   └── FAInterface.java
│   └── State.java
└── fa.test.dfa
    ├── DFA.java
    ├── DFAInterface.java
    └── Transition.java
```
- `DFAState.java`: Represents a DFA state with functionality to track whether the state is an initial state or a final (accepting) state for the DFA.
- `FAInterface.java`: Interface for Finite Automaton functionality.
- `State.java`: Represents an abstract state, subclassed by `DFAState.java`.
- `DFA.java`: The main entity for representing a DFA. It implements `DFAInterface.java`.
- `DFAInterface.java`: Interface containing methods specific to DFAs.
- `Transition.java`: Represents the transition in a DFA, defined by a starting state and a character symbol.

## How to Run

Assuming that Java is installed, follow the instructions below:

1. Navigate to your project directory.
2. Compile the Java files:
   ```
   javac src/main/java/fa/test.dfa/*.java src/main/java/fa/*.java
   ```
3. Run your preferred main class using the Java VM:
   ```
   java fa.test.dfa.DFA
   ```


## Running Tests

For running tests, this project uses the Gradle build system.

To run the entire test suite, navigate to the project root and execute:

   ```
   ./gradlew test
   ```

This will run all tests and provide a summary of the results.

## Challenges Faced

The DFA Simulator project, while straightforward conceptually, presented some challenges during development:

- **Transition handling:** Accounting for every possible transition proved to be challenging, especially while handling "trap" states.
- **State cloning:** As states in our DFA are mutable, it was essential to implement deep cloning to prevent unintentional state mutations.
- **Maintaining determinism:** Given the user's freedom to add transitions, ensuring the deterministic nature of the DFA posed some challenges.

## Conclusion

The DFA Simulator offers an intuitive and simplified way to construct, manipulate, and simulate deterministic finite automata. While it is simple in its current form, it is designed to be modular and extensible, allowing for easy extensions such as adding nondeterministic finite automata (NFAs) later on. It can be a useful learning tool for students studying automata theory, as well as a rapid testing tool for theoretical computation projects.