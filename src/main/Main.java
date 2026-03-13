package main;

import file.FileHandler;
import game.HashiPuzzle;

public class Main {
    static void main(String[] args) {
        new HashiPuzzle(FileHandler.getPuzzleConfig());
    }
}
