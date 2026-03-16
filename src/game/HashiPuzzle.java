package game;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.List;

public class HashiPuzzle {

    private Island[][] islands;
    private int rows, cols;
    private List<Bridge> bridges;

    public HashiPuzzle(String[] input) {
        bridges = new ArrayList<>();
        initIslands(input);
        checkNeighbors();

    }

    private void initIslands(String[] input) {
        for (String line : input) {
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] cells = line.split("\\s+");
            if (islands == null) {
                rows = Integer.parseInt(cells[0]);
                cols = Integer.parseInt(cells[1]);
                islands = new Island[rows][cols];
                continue;
            }
            int currentX = Integer.parseInt(cells[0]);
            int currentY = Integer.parseInt(cells[1]);
            int currentDegree = Integer.parseInt(cells[2]);
            islands[currentX][currentY] = new Island(currentDegree, currentX, currentY);
            if (isIslandCorrect(islands[currentX][currentY])) {
                throw new IllegalArgumentException("Invalid island at (" + currentX + "," + currentY + ") with degree " + currentDegree);
            }
        }
    }

    private boolean isIslandCorrect(Island island) {
        if (island.getX() < 0 || island.getY() < 0 || island.getX() >= rows || island.getY() >= cols || island.getDegree() <= 0 || island.getDegree() > 8)
            return false;
        return islands[island.getX()][island.getY()] == null;
    }

    private void checkNeighbors() {

    }

}
