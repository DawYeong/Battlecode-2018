import bc.MapLocation;
import bc.Planet;
import bc.Team;
import bc.Unit;

public class Cell implements Comparable<Cell> {
    private MapLocation location;
    private boolean isPassable;
    boolean isTarget;
    private Unit unit;
    public boolean marked = false, built = false, blueprinted = false;
    public int distance;
    public String value;

    public Cell(int x, int y, boolean isPassable) {
        this.location = new MapLocation(Planet.Earth, x, y);
        this.isPassable = isPassable;
        this.isTarget = false;
        if (this.isPassable) {
            this.value = "++";
        } else {
            this.value = "--";
        }
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit, Team myTeam) {
        this.unit = unit;
        if (this.unit == null) {
            this.value = "  ";
        }
        if (unit.team() == myTeam) {
            switch (unit.unitType()) {
                case Worker:
                    this.value = "WW";
                    break;
                default:
                    this.value = "OT";
                    break;
            }
        } else {
            this.value = "EE";
        }
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

    public void markSpot() {
        marked = true;
    }


    @Override
    public int compareTo(Cell o) {
        // return in descending order
        return this.distance - o.distance;
    }
}
