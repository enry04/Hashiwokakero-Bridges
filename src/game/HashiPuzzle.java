package game;

import java.util.ArrayList;
import java.util.List;

public class HashiPuzzle {

    private Island[][] grid;
    private List<Island> islands;
    private int rows, cols;
    private List<Bridge> bridges;

    public HashiPuzzle(String[] input) {
        bridges = new ArrayList<>();
        islands = new ArrayList<>();
        initIslands(input);
        validateDegrees();
        addBridges();

    }

    private void initIslands(String[] input) {
        for (String line : input) {
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] cells = line.split("\\s+");
            if (grid == null) {
                rows = Integer.parseInt(cells[0]);
                cols = Integer.parseInt(cells[1]);
                grid = new Island[rows][cols];
                continue;
            }
            int currentX = Integer.parseInt(cells[0]);
            int currentY = Integer.parseInt(cells[1]);
            int currentDegree = Integer.parseInt(cells[2]);
            if (!isIslandCorrect(currentX, currentY, currentDegree)) {
                throw new IllegalArgumentException("Invalid island at (" + currentX + "," + currentY + ") with degree " + currentDegree);
            }
            grid[currentX][currentY] = new Island(currentDegree, currentX, currentY);
            islands.add(grid[currentX][currentY]);
        }
    }

    private boolean isIslandCorrect(int x, int y, int degree) {
        if (x < 0 || y < 0 || x >= rows || y >= cols || degree <= 0 || degree > 8)
            return false;
        return grid[x][y] == null;
    }


    private void validateDegrees() {
        for (Island island : islands) {
            island.setNeighbors(addNeighbors(island));
            int maxBridges = island.getNeighbors().size() * 2;
            if (island.getDegree() > maxBridges) {
                throw new RuntimeException("Invalid degrees, impossible to solve");
            }
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
}