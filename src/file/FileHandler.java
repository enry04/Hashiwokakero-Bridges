package file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandler {

    public static int[][] getPuzzleConfig() {
        InputStream inputStream = FileHandler.class.getResourceAsStream("/puzzle.txt");
        int[][] puzzleConfig;

        if (inputStream == null) {
            System.err.println("Error loading puzzle.txt file");
            System.exit(1);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String text = reader.readAllAsString();
            puzzleConfig = parseText(text);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return puzzleConfig;
    }

    private int[][] parseText(String text) {
        boolean isGridDimensionsAlreadyReaded = false;
        int[][] puzzleConfig;
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.startsWith("#")) continue;
            String[] words = line.split("\\s+");
            if (!isGridDimensionsAlreadyReaded) {
                puzzleConfig = new int[Integer.parseInt(words[0])][Integer.parseInt(words[1])];
                isGridDimensionsAlreadyReaded = true;
            }


        }
        return puzzleConfig;
    }
}
