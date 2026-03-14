package file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandler {

    private String[] textLines;

    public void readFile() {
        InputStream inputStream = FileHandler.class.getResourceAsStream("/puzzle.txt");

        if (inputStream == null) {
            System.err.println("Error loading puzzle.txt file");
            System.exit(1);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            textLines = reader.readAllAsString().split("\n");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] getGridDimensions() {
        int[] gridDimensions = new int[2];
        String[] words = textLines[1].split("\\s+");
        gridDimensions[0] = Integer.parseInt(words[0]);
        gridDimensions[1] = Integer.parseInt(words[1]);

        return gridDimensions;
    }



}
