package game;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private int degree;
    private final int x, y;
    private List<Island> neighbors;
    private int id;

    public Island(int degree, int x, int y) {
        this.degree = degree;
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<>();
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Island> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Island> neighbors) {
        this.neighbors = neighbors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
