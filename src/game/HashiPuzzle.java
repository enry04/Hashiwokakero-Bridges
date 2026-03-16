package game;

import java.util.ArrayList;
import java.util.List;

public class HashiPuzzle {

    private Island[][] islands;
    private int rows, cols;
    private List<Bridge> bridges;

    public HashiPuzzle(String[] input) {
        bridges = new ArrayList<>();
        parseInput(input);
        //validateInput();
    }

    private void parseInput(String[] input) {
        boolean isDimensionsAlreadyReaded = false;
        for (String line : input) {
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] cells = line.split("\\s+");
            if (!isDimensionsAlreadyReaded) {
                rows = Integer.parseInt(cells[0]);
                cols = Integer.parseInt(cells[1]);
                islands = new Island[rows][cols];
                isDimensionsAlreadyReaded = true;
            } else {
                int currentX = Integer.parseInt(cells[0]);
                int currentY = Integer.parseInt(cells[1]);
                int currentDegree = Integer.parseInt(cells[2]);

                if (isIslandCorrect(currentX, currentY, currentDegree)) {
                    islands[Integer.parseInt(cells[0])][Integer.parseInt(cells[1])] = new Island(Integer.parseInt(cells[2]));
                }

            }
        }
    }

    private boolean isIslandCorrect(int currentX, int currentY, int currentDegree) {


        if (currentX <= 0 || currentY <= 0 || currentX >= rows || currentY >= cols) {
            System.err.println("Error: Island coordinates out of bounds!");
            System.exit(1);
        }

        if (currentDegree <= 0 || currentDegree > 8) {
            System.err.println("Error: degrees out of range.");
            System.exit(1);
        }

        if (islands[currentX][currentY] != null) {
            System.err.println("Error: two islands with the same coordinates!");
            System.exit(1);
        }
        return false;
    }


}
