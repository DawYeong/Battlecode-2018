import bc.*;

public class Cell implements Comparable<Cell> {
    private MapLocation location;
    private boolean isPassable;
    private Cell ParentCell;
    int nH, nG, nF;
    boolean isTarget;
    private String value;
    private boolean marked = false, built = false;//built means has been blueprinted
    public int distance;

    public Cell(int x, int y, boolean isPassable, String value) {
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

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public Cell getParentCell() {
        return ParentCell;
    }

    public void setParentCell(Cell parentCell) {
        ParentCell = parentCell;
    }

    public void markSpot(){
        marked = true;
    }

    public void markBuilt(){
        built = true;
    }



    @Override
    public int compareTo(Cell o) {
        // return in descending order
        return this.nF - o.nF;
    }
}
