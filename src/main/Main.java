package main;

import file.FileHandler;
import game.HashiPuzzle;
import gui.HashiPuzzleGUI;

public class Main {
    static void main(String[] args) {
        HashiPuzzle hashiPuzzle = new HashiPuzzle(FileHandler.readFile());
        HashiPuzzleGUI.launch(HashiPuzzleGUI.class);
    }
}
