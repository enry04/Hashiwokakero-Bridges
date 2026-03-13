package game;

public class Island {

    private int row, col;
    private int degree;

    public Island(int row, int col, int degree) {
        this.row = row;
        this.col = col;
        this.degree = degree;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDegree() {
        return degree;
    }
}
