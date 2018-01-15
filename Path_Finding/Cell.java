import bc.MapLocation;
import bc.Planet;

public class Cell implements Comparable<Cell> {
    private MapLocation location;
    private boolean isPassable;
    private Cell ParentCell;

    private int nH, nG, nF;
    boolean isTarget;
    private int value;

    public Cell(int x, int y, boolean isPassable, int value) {
        this.location = new MapLocation(Planet.Earth, x, y);
        this.isPassable = isPassable;
        this.ParentCell = null;
        this.isTarget = false;
        this.value = value;
        nH = 0;
        nG = 0;
        nF = 0;
        ParentCell = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public Cell getParentCell() {
        return ParentCell;
    }

    public void setParentCell(Cell parentCell) {
        ParentCell = parentCell;
    }

    public int getnH() {
        return nH;
    }

    public void setnH(int nH) {
        this.nH = nH;
        this.nF = this.nH + this.nG;
    }

    public int getnG() {
        return nG;
    }

    public void setnG(int nG) {
        this.nG = nG;
        this.nF = this.nH + this.nG;
    }

    public int getnF () {
        return nF;
    }

    @Override
    public int compareTo(Cell o) {
        // return in descending order
        return this.nF - o.nF;
    }
}
