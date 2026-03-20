package game;

import main.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashiPuzzle {

    private final Island[][] grid;
    private final List<Island> islands;
    private final int rows;
    private final int cols;
    private List<Bridge> bridges;
    private HashiSolver hashiSolver;
    private HashiGraph hashiGraph;
    private final List<Bridge> solutionBridges;

    public HashiPuzzle(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        islands = new ArrayList<>();
        solutionBridges = new ArrayList<>();
        grid = new Island[rows][cols];
    }

    public void solve() {

        hashiSolver = new HashiSolver();
        bridges = new ArrayList<>();
        solutionBridges.clear();
        hashiGraph = new HashiGraph(islands);
        assignIds();
        if (validateNeighbors()) {
            addBridges();
            findSolution();
        }
    }

    private boolean validateNeighbors() {
        for (Island island : islands) {
            island.setNeighbors(addNeighbors(island));
            if (island.getNeighbors().isEmpty()) {
                Logger.logError("Island in position x: " + island.getY() + " y: " + island.getX() + " has no neighbors, impossible to solve");
                return false;
            }
        }
        return true;
    }

    private List<Island> addNeighbors(Island island) {

        List<Island> neighbors = new ArrayList<>();

        for (int j = island.getY() + 1; j < cols; j++) {
            if (grid[island.getX()][j] != null) {
                neighbors.add(grid[island.getX()][j]);
                break;
            }
        }

        for (int j = island.getY() - 1; j >= 0; j--) {
            if (grid[island.getX()][j] != null) {
                neighbors.add(grid[island.getX()][j]);
                break;
            }
        }

        for (int i = island.getX() + 1; i < rows; i++) {
            if (grid[i][island.getY()] != null) {
                neighbors.add(grid[i][island.getY()]);
                break;
            }
        }

        for (int i = island.getX() - 1; i >= 0; i--) {
            if (grid[i][island.getY()] != null) {
                neighbors.add(grid[i][island.getY()]);
                break;
            }
        }
        return neighbors;
    }

    private void assignIds() {
        int currentId = 0;

        for (Island island : islands) {
            island.setId(currentId);
            grid[island.getX()][island.getY()] = island;
            currentId++;
        }

    }

    private void addBridges() {

        for (Island island : islands) {
            for (Island neighbor : island.getNeighbors()) {
                if (island.getX() < neighbor.getX() && island.getY() == neighbor.getY()) {
                    bridges.add(new Bridge(island, neighbor, false));
                } else if (island.getY() < neighbor.getY() && island.getX() == neighbor.getX()) {
                    bridges.add(new Bridge(island, neighbor, true));
                }
            }
        }
    }

    private void findSolution() {
        hashiSolver.setTimeout();
        initVariables();
        hashiSolver.addBridgesConstraint();
        addDegreeConstraint();
        addCrossConstraint();
        hashiSolver.printConstraints();

        while (hashiSolver.hasSolution()) {
            Map<String, Integer> bridgeValues = hashiSolver.getBridgeValues();
            hashiGraph.buildGraph(bridgeValues);
            hashiSolver.printSolution(); //fare LOgger.sendmsg(hashiSolver.getSOlution())

            if (!hashiGraph.isGraphConnected()) {
                Logger.logWarning("Graph not connected, searching another solution with connectivity constraint...");
                hashiSolver.generateAnotherSolution(hashiGraph.getComponents());
            } else {
                buildSolutionBridges(bridgeValues);
                return;
            }
        }
        //hashiSolver.printConstraintsInConflict(); //fare Logger.sendmsg(hashisolver.getProblem?)
    }

    private void initVariables() {
        for (Bridge bridge : bridges) {
            hashiSolver.addVariable("bridge_" + bridge.getIslandA().getId() + "_" + bridge.getIslandB().getId());
        }
    }

    private void addDegreeConstraint() {
        for (Island island : islands) {
            hashiSolver.addDegreeConstraint(island.getId(), island.getDegree());
        }
    }

    private void addCrossConstraint() {
        for (int i = 0; i < bridges.size(); i++) {
            for (int j = i + 1; j < bridges.size(); j++) {
                if (isCrossing(bridges.get(i), bridges.get(j))) {
                    hashiSolver.addCrossConstraint("bridge_" + bridges.get(i).getIslandA().getId() + "_" + bridges.get(i).getIslandB().getId(), "bridge_" + bridges.get(j).getIslandA().getId() + "_" + bridges.get(j).getIslandB().getId());
                }
            }
        }
    }

    private boolean isCrossing(Bridge bridgeA, Bridge bridgeB) {

        if (bridgeA.isHorizontal() == bridgeB.isHorizontal()) return false;

        Bridge horizontal = bridgeA.isHorizontal() ? bridgeA : bridgeB;
        Bridge vertical = bridgeA.isHorizontal() ? bridgeB : bridgeA;

        int intersectionX = horizontal.getIslandA().getX();
        int intersectionY = vertical.getIslandA().getY();

        boolean cond1 = isBetween(intersectionX, vertical.getIslandA().getX(), vertical.getIslandB().getX());
        boolean cond2 = isBetween(intersectionY, horizontal.getIslandA().getY(), horizontal.getIslandB().getY());

        return cond1 && cond2;
    }

    private boolean isBetween(int val, int a, int b) {
        return val > Math.min(a, b) && val < Math.max(a, b);
    }

    private void buildSolutionBridges(Map<String, Integer> bridgeValues) {
        for (Map.Entry<String, Integer> entry : bridgeValues.entrySet()) {
            if (entry.getValue() > 0) {
                String[] parts = entry.getKey().split("_");
                int islandAId = Integer.parseInt(parts[1]);
                int islandBId = Integer.parseInt(parts[2]);
                solutionBridges.add(new Bridge(islands.get(islandAId), islands.get(islandBId), entry.getValue()));
            }
        }
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public List<Bridge> getSolutionBridges() {
        return solutionBridges;
    }

    public void addOrUpdateIsland(int row, int col, int degree) {
        if (grid[row][col] == null) {
            islands.add(new Island(degree, row, col));
            grid[row][col] = new Island(degree, row, col);
        } else {
            grid[row][col].setDegree(degree);
            for (Island island : islands) {
                if (island.getX() == row && island.getY() == col) {
                    island.setDegree(degree);
                    break;
                }
            }
        }


    }

    public void removeIsland(int row, int col) {
        islands.removeIf(island -> island.getX() == row && island.getY() == col);
        grid[row][col] = null;
    }
}