package game;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final int degree;
    private final int x, y;
    private List<Island> neighbors;
    private int id;

    public Island(int degree, int x, int y, int id) {
        this.degree = degree;
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<>();
        this.id = id;
    }

    public int getDegree() {
        return degree;
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
}
