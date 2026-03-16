package file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandler {

    public static String[] readFile() {
        String[] textLines;
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
        return textLines;
    }
}
