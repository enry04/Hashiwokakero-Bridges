🧩 Hashiwokakero Solver (Java + Z3 SMT)
This project implements a solution to the Hashiwokakero logic puzzle using Java and an SMT solver (Z3).

🔹 Problem Description
Hashiwokakero is a grid-based puzzle in which certain cells contain islands with a number indicating their required degree (number of bridges).
The goal is to connect all islands with bridges while satisfying the following constraints:
- Each island must have a number of bridges equal to its specified degree.
- Bridges can only be horizontal or vertical.
- Bridges cannot cross each other.
- All islands must be connected into a single connected component.

🔹 Implementation Approach
The problem is modeled as a Satisfiability Modulo Theories (SMT) problem and solved using the Z3 solver.

🧠 Modeling
- Each possible bridge between two islands is represented by a variable whose value is 0, 1, or 2, indicating the number of bridges.
- Islands are identified by their coordinates (row, col) and a unique ID.
- The puzzle constraints are translated into logical and arithmetic constraints.

📌 Main Constraints
- Degree constraint: The sum of the bridges incident to each island must equal its specified degree.
- Non-crossing constraint: Bridges cannot cross each other.
- Global connectivity constraint: All islands must belong to a single connected component.

🔹 Complexity
- SMT encoding: Polynomial time with respect to the number of islands and possible bridges.
- Solving: Exponential in the worst case, since the problem is NP-complete.
