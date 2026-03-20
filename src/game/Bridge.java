package game;

public class Bridge {

    private final Island IslandA;
    private final Island islandB;
    private boolean isHorizontal;
    private int value;

    public Bridge(Island islandA, Island islandB, boolean isHorizontal) {
        IslandA = islandA;
        this.islandB = islandB;
        this.isHorizontal = isHorizontal;
    }

    public Bridge(Island islandA, Island islandB, int value) {
        this.IslandA = islandA;
        this.islandB = islandB;
        this.value = value;
    }

    public Island getIslandA() {
        return IslandA;
    }

    public Island getIslandB() {
        return islandB;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public int getValue() {
        return value;
    }
}
