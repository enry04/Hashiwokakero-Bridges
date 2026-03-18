package game;

import com.microsoft.z3.*;

import java.util.ArrayList;
import java.util.List;

public class HashiSolver {

    private final Solver solver;
    private final Context context;
    private List<IntExpr> variables;
    private List<BoolExpr> costraints;
    private List<BoolExpr> assumptions;

    public HashiSolver() {
        context = new Context();
        solver = context.mkSolver();
        variables = new ArrayList<>();
        costraints = new ArrayList<>();
        assumptions = new ArrayList<>();
    }

    public void addVariable(String varName) {
        variables.add(context.mkIntConst(varName));
    }

    public void addBridgesConstraint() {
        for (IntExpr variable : variables) {
            BoolExpr costraint = context.mkAnd(context.mkGe(variable, context.mkInt(0)), context.mkLe(variable, context.mkInt(2)));
            costraints.add(costraint);
            BoolExpr label = context.mkBoolConst(" " + variable.toString() + " domain constraint ");
            assumptions.add(label);
            solver.add(context.mkImplies(label, costraint));
        }
    }

    public void addDegreeConstraint(int id, int degree) {
        List<IntExpr> variablesLinked = new ArrayList<>();
        for (IntExpr variable : variables) {
            String name = variable.toString();
            if (name.contains("_" + id + "_") || name.endsWith("_" + id)) {
                variablesLinked.add(variable);
            }
        }

        IntExpr[] arr = variablesLinked.toArray(new IntExpr[0]);
        BoolExpr constraint = context.mkEq(context.mkAdd(arr), context.mkInt(degree));
        costraints.add(constraint);

        String labelName = " ";

        for (IntExpr variable : variablesLinked) {
            labelName = labelName.concat(variable.toString() + " ");
        }

        BoolExpr label = context.mkBoolConst(labelName + " degree constraint with island " + id + " (degree = " + degree + ") ");
        assumptions.add(label);
        solver.add(context.mkImplies(label, constraint));
    }

    public void addCrossConstraint(String var1, String var2) {
        IntExpr variable1 = context.mkIntConst((var1));
        IntExpr variable2 = context.mkIntConst((var2));
        BoolExpr constraint = context.mkOr(context.mkEq(variable1, context.mkInt(0)), context.mkEq(variable2, context.mkInt(0)));
        costraints.add(constraint);
        BoolExpr label = context.mkBoolConst(" " + var1 + " " + var2 + " cross constraint ");
        assumptions.add(label);
        solver.add(context.mkImplies(label, constraint));
    }

    public void solvePuzzle() {

        Status status = solver.check(assumptions.toArray(new BoolExpr[0]));

        if (status == Status.SATISFIABLE) {
            printSolution();
        } else if (status == Status.UNSATISFIABLE) {
            printConstraintsInConflict();
        } else {
            System.out.println("Solver does not know how to solve...");
        }
    }

    private void printSolution() {
        Model model = solver.getModel();
        System.out.println("Feasible solution:");
        for (IntExpr variable : variables) {
            int val = Integer.parseInt(model.evaluate(variable, false).toString());
            System.out.println(variable.toString() + " = " + val);
        }
    }

    private void printConstraintsInConflict() {
        BoolExpr[] constraintsInConflict = solver.getUnsatCore();
        System.out.println("Unsatisfiable, constraints impossible to satisfy:");
        for (BoolExpr constraint : constraintsInConflict) {
            System.out.println(constraint);
        }
    }

    public void printConstraints() {
        System.out.println(variables.size() + " variables:");
        for (IntExpr variable : variables) {
            System.out.println(variable.toString());
        }

        System.out.println("Constraints:\n");
        for (int i = 0; i < costraints.size(); i++) {
            System.out.println(i + 1 + ") " + costraints.get(i) + "\n");
        }
    }
}
