package gui;

import game.Bridge;
import game.HashiPuzzle;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.Optional;

public class HashiPuzzleGUI extends Application {

    private Pane root;
    private final int WINDOW_WIDTH = 900;
    private final int WINDOW_HEIGHT = 800;
    private int rows, cols;
    private double cellSize;
    private HashiPuzzle hashiPuzzle;

    @Override
    public void start(Stage stage) {

        rows = askGridSize("Numero di righe:");
        cols = askGridSize("Numero di colonne:");
        root = new Pane();
        drawGrid();
        addSolveButton();
        root.setOnMouseClicked(e -> handleGridClick(e.getX(), e.getY()));
        try {
            hashiPuzzle = new HashiPuzzle(rows, cols);
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Hashiwokakero");
        stage.setScene(scene);
        stage.show();
    }

    private int askGridSize(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dimensione griglia");
        dialog.setHeaderText(message);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    private void addSolveButton() {
        Button solveButton = new Button("Solve");
        double buttonWidth = 100;
        double buttonHeight = 40;
        solveButton.setPrefSize(buttonWidth, buttonHeight);

        double gridWidth = cols * cellSize;
        double gridHeight = rows * cellSize;
        double offsetX = (WINDOW_WIDTH - gridWidth) / 2;
        double offsetY = (WINDOW_HEIGHT - gridHeight) / 2;

        // posiziona il bottone centrato orizzontalmente
        solveButton.setLayoutX((WINDOW_WIDTH - buttonWidth) / 2);

        // posiziona il bottone subito sotto la griglia, senza uscire dalla finestra
        double yPos = offsetY + gridHeight + 10; // 10 px di margine
        if (yPos + buttonHeight > WINDOW_HEIGHT - 10) {
            yPos = WINDOW_HEIGHT - buttonHeight - 10; // non uscire dalla finestra
        }
        solveButton.setLayoutY(yPos);

        solveButton.setOnAction(e -> {
            hashiPuzzle.solve();
            redraw();
            drawBridges();
        });

        root.getChildren().add(solveButton);
    }

    private void handleGridClick(double mouseX, double mouseY) {

        double gridWidth = cols * cellSize;
        double gridHeight = rows * cellSize;
        double offsetX = (WINDOW_WIDTH - gridWidth) / 2;
        double offsetY = (WINDOW_HEIGHT - gridHeight) / 2;

        if (mouseX < offsetX || mouseY < offsetY ||
                mouseX > offsetX + gridWidth || mouseY > offsetY + gridHeight) {
            return;
        }

        int col = (int) ((mouseX - offsetX) / cellSize);
        int row = (int) ((mouseY - offsetY) / cellSize);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Grado isola (vuoto = cancella)");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(val -> {
            if (val.isEmpty()) {
                hashiPuzzle.removeIsland(row, col);
            } else {
                int degree = Integer.parseInt(val);
                hashiPuzzle.addOrUpdateIsland(row, col, degree);
            }

            redraw();
        });
    }

    private void redraw() {
        root.getChildren().clear();
        addSolveButton();
        drawGrid();
        drawIslands();
    }

    private void showError(String message) {
        Text errorText = new Text(50, 50, message);
        errorText.setFill(Color.RED);
        root.getChildren().add(errorText);
    }

    public void drawGrid() {
        double padding = 40;

        cellSize = Math.min((WINDOW_WIDTH - 2 * padding) / (double) cols, (WINDOW_HEIGHT - 2 * padding) / (double) rows);

        double gridWidth = cols * cellSize;
        double gridHeight = rows * cellSize;
        double offsetX = (WINDOW_WIDTH - gridWidth) / 2;
        double offsetY = (WINDOW_HEIGHT - gridHeight) / 2;

        for (int j = 0; j <= cols; j++) {
            double x = offsetX + j * cellSize;
            Line verticalLine = new Line(x, offsetY, x, offsetY + rows * cellSize);
            verticalLine.setStroke(Color.BLACK);
            verticalLine.setStrokeWidth(2);
            root.getChildren().add(verticalLine);
        }

        for (int i = 0; i <= rows; i++) {
            double y = offsetY + i * cellSize;
            Line horizontalLine = new Line(offsetX, y, offsetX + cols * cellSize, y);
            horizontalLine.setStroke(Color.BLACK);
            horizontalLine.setStrokeWidth(2);
            root.getChildren().add(horizontalLine);
        }
    }

    private void drawIslands() {

        double gridWidth = cols * cellSize;
        double gridHeight = rows * cellSize;
        double offsetX = (WINDOW_WIDTH - gridWidth) / 2;
        double offsetY = (WINDOW_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < hashiPuzzle.getIslands().size(); i++) {
            int row = hashiPuzzle.getIslands().get(i).getX();
            int col = hashiPuzzle.getIslands().get(i).getY();
            int degree = hashiPuzzle.getIslands().get(i).getDegree();

            double centerX = offsetX + col * cellSize + cellSize / 2;
            double centerY = offsetY + row * cellSize + cellSize / 2;

            StackPane stack = new StackPane();
            stack.setLayoutX(centerX - cellSize / 2);
            stack.setLayoutY(centerY - cellSize / 2);
            stack.setPrefSize(cellSize, cellSize);

            Circle island = new Circle(cellSize / 3);
            island.setFill(Color.LIGHTBLUE);
            island.setStroke(Color.BLUE);
            island.setStrokeWidth(2);

            Text text = new Text(String.valueOf(degree));
            text.setFill(Color.BLACK);
            text.setStyle("-fx-font-size: " + (cellSize * 0.5) + "px; -fx-font-weight: bold;");

            stack.getChildren().addAll(island, text);
            root.getChildren().add(stack);
        }
    }

    private void drawBridges() {
        double offsetX = (WINDOW_WIDTH - hashiPuzzle.getCols() * cellSize) / 2;
        double offsetY = (WINDOW_HEIGHT - hashiPuzzle.getRows() * cellSize) / 2;

        for (Bridge bridge : hashiPuzzle.getSolutionBridges()) {

            int row1 = bridge.getIslandA().getX();
            int col1 = bridge.getIslandA().getY();
            int row2 = bridge.getIslandB().getX();
            int col2 = bridge.getIslandB().getY();
            int numBridges = bridge.getValue();

            double centerX1 = offsetX + col1 * cellSize + cellSize / 2;
            double centerY1 = offsetY + row1 * cellSize + cellSize / 2;

            double centerX2 = offsetX + col2 * cellSize + cellSize / 2;
            double centerY2 = offsetY + row2 * cellSize + cellSize / 2;

            // direzione
            double dx = centerX2 - centerX1;
            double dy = centerY2 - centerY1;
            double length = Math.sqrt(dx * dx + dy * dy);

            dx /= length;
            dy /= length;

            // raggio cerchio
            double radius = cellSize / 3 + 1;

            // punti sui bordi dei cerchi
            double x1 = centerX1 + dx * radius;
            double y1 = centerY1 + dy * radius;

            double x2 = centerX2 - dx * radius;
            double y2 = centerY2 - dy * radius;

            // ponte singolo
            if (numBridges >= 1) {
                Line line1 = new Line(x1, y1, x2, y2);
                line1.setStroke(Color.RED);
                line1.setStrokeWidth(3);
                root.getChildren().add(line1);
            }

            // ponte doppio
            if (numBridges == 2) {
                double offset = 6;

                // vettore perpendicolare
                double px = -dy;
                double py = dx;

                double norm = Math.sqrt(px * px + py * py);
                px /= norm;
                py /= norm;

                px *= offset;
                py *= offset;

                Line line2 = new Line(x1 + px, y1 + py, x2 + px, y2 + py);
                line2.setStroke(Color.RED);
                line2.setStrokeWidth(3);

                root.getChildren().add(line2);
            }
        }
    }

}