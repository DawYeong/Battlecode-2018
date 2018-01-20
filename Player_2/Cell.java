import bc.*;

public class Cell implements Comparable<Cell> {
    private MapLocation location;
    private boolean isPassable;
    int nH, nG, nF;
    boolean isTarget;
    private String value;
    private int karbonite;
    public int distance;

    public Cell(int x, int y, boolean isPassable, String value) {
        this.location = new MapLocation(Planet.Earth, x, y);
        this.isPassable = isPassable;
        this.isTarget = false;
        this.value = value;
        nH = 0;
        nG = 0;
        nF = 0;
    }

    public int getKarbonite() {
        return karbonite;
    }

    public void setKarbonite(int karbonite) {
        this.karbonite = karbonite;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MapLocation getLocation() {
        return location;
    }

    public void setLocation(MapLocation location) {
        this.location = location;
    }

    public boolean isPassable() {
        return isPassable;
    }

    @Override
    public int compareTo(Cell o) {
        // return in descending order
        return this.distance - o.distance;
    }
}
