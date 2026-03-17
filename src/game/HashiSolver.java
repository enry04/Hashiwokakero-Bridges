package game;

import com.microsoft.z3.*;

import java.util.ArrayList;
import java.util.List;

public class HashiSolver {

    private Solver solver;
    private Context context;
    private List<IntExpr> variables;

    public HashiSolver() {
        context = new Context();
        solver = context.mkSolver();
        variables = new ArrayList<>();
    }

    public void addVariable(String varName) {
        variables.add(context.mkIntConst(varName));
    }

    public void addBridgesConstraint() {
        for (IntExpr variable : variables) {
            solver.add(context.mkGe(variable, context.mkInt(0)));
            solver.add(context.mkLe(variable, context.mkInt(2)));
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
        solver.add(context.mkEq(context.mkAdd(arr), context.mkInt(degree)));

    }

    public void addCrossConstraint(String var1, String var2) {
        IntExpr variable1 = context.mkIntConst((var1));
        IntExpr variable2 = context.mkIntConst((var2));
        solver.add(context.mkOr(
                context.mkEq(variable1, context.mkInt(0)),
                context.mkEq(variable2, context.mkInt(0))
        ));
    }

    public void printConstraints() {
        System.out.println("Constraints:");
        for (BoolExpr c : solver.getAssertions()) {
            System.out.println(c);
        }
    }

}
