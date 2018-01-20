import bc.*;


public class Player_2 {
    public static GameController gc = new GameController();
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
    public static VecUnit myUnits;

    public static PlanetMap EarthMap, MarsMap;
    public static Cell[][] GridEarth, GridMars;


    public static void main(String[] args) {

        EarthMap = gc.startingMap(Planet.Earth);
        MarsMap = gc.startingMap(Planet.Mars);

        GridEarth = new Cell[(int) EarthMap.getHeight()][(int) EarthMap.getWidth()];
        GridMars = new Cell[(int) MarsMap.getHeight()][(int) MarsMap.getWidth()];

        constructGrid();

        while (true) {
            try {
                myUnits = gc.myUnits();

                PrintMap();
                updateGrid(myUnits);

                System.out.println();

                Thread.sleep(10000);

                gc.nextTurn();
            } catch (Exception e) {
                System.out.println("Error occurred in main class: " + e);
            }
        }
    }


    public static void constructGrid() {
        MapLocation tempLocationEarth = new MapLocation(Planet.Earth, 0, 0);
        MapLocation tempLocationMars = new MapLocation(Planet.Mars, 0, 0);
        for (int y = 0; y < GridEarth[0].length; y++) {
            for (int x = 0; x < GridEarth[1].length; x++) {
                tempLocationEarth.setX(x);
                tempLocationEarth.setY(y);
                GridEarth[y][x] = new Cell(x, y, (EarthMap.isPassableTerrainAt(tempLocationEarth) != 0));
            }
        }
        for (int y = 0; y < GridMars[0].length; y++) {
            for (int x = 0; x < GridMars[1].length; x++) {
                tempLocationMars.setX(x);
                tempLocationMars.setY(y);
                GridMars[y][x] = new Cell(x, y, (MarsMap.isPassableTerrainAt(tempLocationMars) != 0));
            }
        }
//        for (int i = 0; i < initialUnits.size(); i++) {
//            Unit unit = initialUnits.get(i);
//            int X = unit.location().mapLocation().getX(), Y = unit.location().mapLocation().getY();
//            if (unit.team() == myTeam) {
//                GridEarth[Y][X].setValue("--");
//            } else {
//                GridEarth[Y][X].setValue("--");
//            }
//        }
    }

    public static void updateGrid(VecUnit units) {
        for (int y = 0; y < GridEarth[0].length; y++) {
            for (int x = 0; x < GridEarth[1].length; x++) {
                GridEarth[y][x].setUnit(null, myTeam);
            }
        }
        if (gc.planet() == Planet.Earth) {
            for (int i = 0; i < units.size(); i++) {
                Unit u = units.get(i);
                GridEarth[u.location().mapLocation().getY()][u.location().mapLocation().getX()].setUnit(u, myTeam);
            }
        } else {
            for (int i = 0; i < units.size(); i++) {
                Unit u = units.get(i);
                GridMars[u.location().mapLocation().getY()][u.location().mapLocation().getX()].setUnit(u, myTeam);
            }
        }
    }

    public static void PrintMap() {
        if (gc.planet() == Planet.Earth) {
            System.out.println("Earth Map:");
            for (int y = 0; y < GridEarth[0].length; y++) {
                for (int x = 0; x < GridEarth[1].length; x++) {
                    System.out.print(GridEarth[y][x].value + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Mars Map:");
            for (int y = 0; y < GridMars[0].length; y++) {
                for (int x = 0; x < GridMars[1].length; x++) {
                    System.out.print(GridMars[y][x].value + " ");
                }
                System.out.println();
            }
        }
    }
}
