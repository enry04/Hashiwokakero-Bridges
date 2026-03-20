package game;

import main.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashiPuzzle {

    private Island[][] grid;
    private final List<Island> islands;
    private int rows, cols;
    private final List<Bridge> bridges;
    private final HashiSolver hashiSolver;
    private HashiGraph hashiGraph;
    private List<Bridge> solutionBridges;
    private int currentId = 0;

//    public HashiPuzzle(String[] input) {
//        bridges = new ArrayList<>();
//        islands = new ArrayList<>();
//        hashiSolver = new HashiSolver();
//        solutionBridges = new ArrayList<>();
//        initIslands(input);
//        validateDegrees();
//        addBridges();
//        solve();
//    }

    public HashiPuzzle(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        bridges = new ArrayList<>();
        islands = new ArrayList<>();
        hashiSolver = new HashiSolver();
        solutionBridges = new ArrayList<>();
        grid = new Island[rows][cols];
    }

    public void solve() {
        hashiGraph = new HashiGraph(islands);
        validateDegrees();
        addBridges();
        findSolution();
    }

//    private void initIslands(String[] input) {
//        int counter = 0;
//        for (String line : input) {
//            if (line.isEmpty() || line.startsWith("#") || line.startsWith("\r")) continue;
//            String[] cells = line.split("\\s+");
//            if (grid == null) {
//                rows = Integer.parseInt(cells[0]);
//                cols = Integer.parseInt(cells[1]);
//                grid = new Island[rows][cols];
//                continue;
//            }
//            int currentX = Integer.parseInt(cells[0]);
//            int currentY = Integer.parseInt(cells[1]);
//            int currentDegree = Integer.parseInt(cells[2]);
//            if (!isIslandCorrect(currentX, currentY, currentDegree)) {
//                Logger.logError("Invalid island at (" + currentX + "," + currentY + ") with degree " + currentDegree);
//            }
//            grid[currentX][currentY] = new Island(currentDegree, currentX, currentY, counter);
//            islands.add(grid[currentX][currentY]);
//            counter++;
//        }
//        if (islands.isEmpty()) Logger.logError("No islands found");
//        hashiGraph = new HashiGraph(islands);
//    }
//
//    private boolean isIslandCorrect(int x, int y, int degree) {
//        if (x < 0 || y < 0 || x >= rows || y >= cols || degree <= 0 || degree > 8) return false;
//        return grid[x][y] == null;
//    }

    private void validateDegrees() {
        for (Island island : islands) {
            island.setNeighbors(addNeighbors(island));
            if (island.getNeighbors().isEmpty())
                Logger.logError("Island " + island.getId() + " has no neighbors, impossible to solve");
        }
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
        solutionBridges.clear();
        hashiSolver.setTimeout();
        initVariables();
        hashiSolver.addBridgesConstraint();
        addDegreeConstraint();
        addCrossConstraint();
        hashiSolver.printConstraints();

        while (hashiSolver.hasSolution()) {
            Map<String, Integer> bridgeValues = hashiSolver.getBridgeValues();
            hashiGraph.buildGraph(bridgeValues);
            hashiSolver.printSolution();

            if (!hashiGraph.isGraphConnected()) {
                Logger.logWarning("Graph not connected, searching another solution with connectivity constraint...");
                hashiSolver.generateAnotherSolution(hashiGraph.getComponents());
            } else {
                buildSolutionBridges(bridgeValues);
                return;
            }
        }
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
        islands.add(new Island(degree, row, col, currentId));
        grid[row][col] = new Island(degree, row, col, currentId);
        currentId++;
    }

    public void removeIsland(int row, int col) {
        islands.removeIf(island -> island.getX() == row && island.getY() == col);
    }
}