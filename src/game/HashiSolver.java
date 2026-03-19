package game;

import com.microsoft.z3.*;
import main.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashiSolver {

    private final Solver solver;
    private final Context context;
    private final List<IntExpr> variables;
    private final List<BoolExpr> costraints;
    private final List<BoolExpr> assumptions;
    private final Map<String, Integer> bridgeValues;

    public HashiSolver() {
        context = new Context();
        solver = context.mkSolver();
        variables = new ArrayList<>();
        costraints = new ArrayList<>();
        assumptions = new ArrayList<>();
        bridgeValues = new HashMap<>();
    }

    public void setTimeout() {
        Params params = context.mkParams();
        params.add("timeout", 5000);
        solver.setParameters(params);
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

    public boolean hasSolution() {

        Status status = solver.check(assumptions.toArray(new BoolExpr[0]));

        if (status == Status.SATISFIABLE) {
            createSolutionMap();
            return true;
        } else if (status == Status.UNSATISFIABLE) {
            printConstraintsInConflict();
        } else {
            Logger.logInfo("Unknown solution, timeout reached...");
        }

        return false;
    }

    private void createSolutionMap() {
        Model model = solver.getModel();
        for (IntExpr variable : variables) {
            int val = Integer.parseInt(model.evaluate(variable, false).toString());
            bridgeValues.put(variable.toString(), val);
        }
    }

    public void generateAnotherSolution(List<List<Integer>> components) {

        for (int i = 0; i < components.size(); i++) {
            for (int j = i + 1; j < components.size(); j++) {
                List<Integer> componentA = components.get(i);
                List<Integer> componentB = components.get(j);
                List<BoolExpr> possibleConnectionsBetweenComponents = new ArrayList<>();

                for (int nodeA : componentA) {
                    for (int nodeB : componentB) {
                        String varName1 = "bridge_" + nodeA + "_" + nodeB;
                        String varName2 = "bridge_" + nodeB + "_" + nodeA;
                        if (bridgeValues.containsKey(varName1)) {
                            possibleConnectionsBetweenComponents.add(context.mkGt(context.mkIntConst(varName1), context.mkInt(0)));
                        } else if (bridgeValues.containsKey(varName2)) {
                            possibleConnectionsBetweenComponents.add(context.mkGt(context.mkIntConst(varName2), context.mkInt(0)));
                        }
                    }
                }
                if (!possibleConnectionsBetweenComponents.isEmpty()) {
                    BoolExpr componentLinkConstraint = context.mkOr(possibleConnectionsBetweenComponents.toArray(new BoolExpr[0]));
                    solver.add(componentLinkConstraint);
                    assumptions.add(componentLinkConstraint);
                    costraints.add(componentLinkConstraint);
                }
            }
        }
    }

    public void printSolution() {
        Model model = solver.getModel();
        Logger.logInfo("Feasible solution:");
        for (IntExpr variable : variables) {
            int val = Integer.parseInt(model.evaluate(variable, false).toString());
            Logger.logInfo(variable.toString() + " = " + val);
        }
    }

    private void printConstraintsInConflict() {
        BoolExpr[] constraintsInConflict = solver.getUnsatCore();
        Logger.logInfo("Unsatisfiable, constraints impossible to satisfy:");
        for (BoolExpr constraint : constraintsInConflict) {
            Logger.logInfo(constraint.toString());
        }
    }

    public void printConstraints() {
        Logger.logInfo("\n" + variables.size() + " variables:");
        for (IntExpr variable : variables) {
            Logger.logInfo(variable.toString());
        }

        Logger.logInfo("\nConstraints:\n");
        for (int i = 0; i < costraints.size(); i++) {
            Logger.logInfo(i + 1 + ") " + costraints.get(i) + "\n");
        }
    }

    public Map<String, Integer> getBridgeValues() {
        return bridgeValues;
    }
}
