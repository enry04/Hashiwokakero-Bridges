package game;

public class Bridge {

    private Island IslandA, islandB;
    private boolean isHorizontal;

    public Bridge(Island islandA, Island islandB, boolean isHorizontal) {
        IslandA = islandA;
        this.islandB = islandB;
        this.isHorizontal = isHorizontal;
    }

    public Island getIslandA() {
        return IslandA;
    }

    public void setIslandA(Island islandA) {
        IslandA = islandA;
    }

    public Island getIslandB() {
        return islandB;
    }

    public void setIslandB(Island islandB) {
        this.islandB = islandB;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }
}
