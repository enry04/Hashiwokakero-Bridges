package game;

public class Bridge {

    private final Island IslandA;
    private final Island islandB;
    private final boolean isHorizontal;

    public Bridge(Island islandA, Island islandB, boolean isHorizontal) {
        IslandA = islandA;
        this.islandB = islandB;
        this.isHorizontal = isHorizontal;
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

}
