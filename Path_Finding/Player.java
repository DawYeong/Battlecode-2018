// import the API.
// See xxx for the javadocs.

import bc.*;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    static GameController gc = new GameController();
    static Team myTeam = gc.team();

    public static Cell Grid[][];

    public static void main(String[] args) throws InterruptedException {

//        Thread.sleep(100000);

        for (int x = 0; x < 10; x++) {
            System.out.println("============================================================================================================================================================");
        }

        System.out.println("Starting Abdullah BOT.");
        System.out.printf("My Team: %b \n", myTeam);

        Direction[] directions = Direction.values();
        Random random = new Random();


        PlanetMap EarthMap = gc.startingMap(Planet.Earth);
        System.out.println(EarthMap.getWidth() + " , " + EarthMap.getHeight());

        Grid = new Cell[(int) EarthMap.getHeight()][(int) EarthMap.getWidth()];

        MapLocation tempLocation = new MapLocation(Planet.Earth, 0, 0);

        for (int y = 0; y < Grid[0].length; y++) {
            for (int x = 0; x < Grid[1].length; x++) {
                tempLocation.setX(x);
                tempLocation.setY(y);
                Cell c;
                if (EarthMap.isPassableTerrainAt(tempLocation) != 0) {
                    c = new Cell(x, y, true, 0);
                    Grid[y][x] = c;
                } else {
                    c = new Cell(x, y, false, -1);
                    Grid[y][x] = c;
                }
            }
        }

        System.out.println("Directions:");
        for (int x = 0; x < directions.length; x++) {
            System.out.println(x + " : " + directions[x]);
        }

        printMap();

//        Finder finder;
//
//        finder = new Finder(Grid[10][0], Grid[6][19], Grid);
//
//        printMap();
//
//        finder.findPath();
//
//        if (finder.bPathFound) {
//            System.out.println("Path Found.");
//
//            finder.reconstruct_path();
//        } else {
//            System.out.println("Path not Found.");
//        }

        Finder2 finder2;

        finder2 = new Finder2(Grid[1][1], Grid[29][28], Grid);

        finder2.findPath();

        ArrayList<Cell> Path;

        if (finder2.bPathFound) {
            System.out.println("Path Found.");
            Path = finder2.getPath();
            for (Cell c : Path) {
                System.out.println("( " + c.getLocation().getX() + " , " + c.getLocation().getY() + " )");
            }
            printMap();
        } else {
            System.out.println("Path not found.");
        }

        Thread.sleep(100000);


        while (true) {
            System.out.println("Round # " + gc.round());
//            System.out.println("My Karbonite " + gc.karbonite());
            VecUnit alUnits = gc.myUnits();

            MapLocation _l;

            for (int u = 0; u < alUnits.size(); u++) {
                Unit unit = alUnits.get(u);
                System.out.println(unit.id());

                _l = unit.location().mapLocation();

                if (unit.unitType() == UnitType.Worker) {
//                    Grid[_l.getY()][_l.getX()].setValue("W");
                }

            }
            printMap();
            gc.nextTurn();
            Thread.sleep(5000);
        }
    }

    static void printMap() {
        for (int y = 0; y < Grid[0].length; y++) {
            for (int x = 0; x < Grid[1].length; x++) {

                if (!Grid[y][x].isPassable()) {
                    System.out.print("-- ");
                    continue;
                }

                if (Grid[y][x].getValue() == 0) {
                    System.out.print("   ");
                    continue;
                }

                if (Grid[y][x].getValue() < 0) {
                    System.out.print(Grid[y][x].getValue() + " ");
                } else {
                    if (Grid[y][x].getValue() < 10) {
                        System.out.print("0");
                    }
                    System.out.print(Grid[y][x].getValue() + " ");
                }
            }
            System.out.println(",");
        }
        System.out.println();
    }
}