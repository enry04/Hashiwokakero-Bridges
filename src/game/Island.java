package game;

public class Island {
    private int degree;
    private int x, y;

    public Island(int degree, int x, int y) {
        this.degree = degree;
        this.x = x;
        this.y = y;
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
}
