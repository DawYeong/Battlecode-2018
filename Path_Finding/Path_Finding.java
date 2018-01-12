// import the API.
// See xxx for the javadocs.

import bc.*;

import java.util.Random;

public class Path_Finding {
    static GameController gc = new GameController();
    static Team myTeam = gc.team();

    public static int arisPassableEarth[][];

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting Abdullah BOT.");
        System.out.printf("My Team: %b \n", myTeam);

        Direction[] directions = Direction.values();

        Random random = new Random();

        PlanetMap EarthMap = gc.startingMap(Planet.Earth);
        System.out.println(EarthMap.getWidth() + " , " + EarthMap.getHeight());

        arisPassableEarth = new int[(int) EarthMap.getHeight()][(int) EarthMap.getWidth()];

        MapLocation tempLocation = new MapLocation(Planet.Earth, 0, 0);

        for (int y = 0; y < arisPassableEarth[0].length; y++) {
            for (int x = 0; x < arisPassableEarth[1].length; x++) {
                tempLocation.setX(x);
                tempLocation.setY(y);
                arisPassableEarth[y][x] = EarthMap.isPassableTerrainAt(tempLocation);
            }
        }

        System.out.print("[");

        for (int y = 0; y < arisPassableEarth[0].length; y++) {
            for (int x = 0; x < arisPassableEarth[1].length; x++) {
                System.out.print(" " + arisPassableEarth[y][x] + ", ");
            }
            System.out.println();
        }

        System.out.print("] \n");

        while (true) {
            System.out.println("Round # " + gc.round());
            System.out.println("My Karbonite " + gc.karbonite());
            VecUnit alUnits = gc.units();

            gc.nextTurn();
            Thread.sleep(1000);
        }
    }
}