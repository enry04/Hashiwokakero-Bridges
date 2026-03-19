package gui;

import game.HashiPuzzle;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class HashiPuzzleGUI extends Application {

    private Pane root;
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private double cellSize;
    private static HashiPuzzle hashiPuzzle;

    @Override
    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Hashiwokakero");
        stage.setScene(scene);
        stage.show();

    }

    public void drawGrid() {
        cellSize = Math.min(WINDOW_WIDTH / (double) hashiPuzzle.getCols(), WINDOW_HEIGHT / (double) hashiPuzzle.getRows());
        for (int i = 0; i < hashiPuzzle.getRows(); i++) {
            double x = i * cellSize;
            double startY = 0;
            double endY = WINDOW_HEIGHT;
            Line verticalLine = new Line(x, startY, x, endY);
            verticalLine.setStroke(Color.BLACK);
            verticalLine.setStrokeWidth(3);
            root.getChildren().add(verticalLine);
        }
    }

    private void addIsland(int row, int col, double cellSize) {
        double x = col * cellSize + cellSize / 2;
        double y = row * cellSize + cellSize / 2;
        Circle island = new Circle(x, y, cellSize / 4);
        island.setFill(Color.LIGHTBLUE);
        island.setStroke(Color.BLUE);
        root.getChildren().add(island);
    }

    private void addBridge(int row1, int col1, int row2, int col2, int bridges, double cellSize) {
        double x1 = col1 * cellSize + cellSize / 2;
        double y1 = row1 * cellSize + cellSize / 2;
        double x2 = col2 * cellSize + cellSize / 2;
        double y2 = row2 * cellSize + cellSize / 2;

        if (bridges >= 1) {
            Line line1 = new Line(x1, y1, x2, y2);
            line1.setStrokeWidth(3);
            line1.setStroke(Color.GRAY);
            root.getChildren().add(line1);
        }

        if (bridges == 2) {
            double offsetX = (y2 - y1) / 20;
            double offsetY = (x1 - x2) / 20;
            Line line2 = new Line(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
            line2.setStrokeWidth(3);
            line2.setStroke(Color.GRAY);
            root.getChildren().add(line2);
        }
    }

    public static void setHashiPuzzle(HashiPuzzle p) {
        hashiPuzzle = p;
    }
}