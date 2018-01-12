import bc.*;

import java.util.*;
import java.io.*;

public class Player {
    public static GameController gc = new GameController();
    public static Team myTeam = gc.team();
    public static MapLocation loc = new MapLocation(Planet.Earth, 10, 20);
    public static Direction[] directions = Direction.values();
    public static VecUnit units;

    public static void main(String[] args) {
        System.out.println(myTeam);
        gc.queueResearch(UnitType.Rocket);
        while (true) {
            System.out.println("Current round: " + gc.round());
            units = gc.myUnits();
            for (int i = 0; i < units.size(); i++) {
                Unit unit = units.get(i);
                switch (unit.unitType()) {
                    case Worker: //We don't need UnitType.Worker since it is an enum
                        runWorker(unit);
                        break;
                    case Factory:
                        break;
                    case Rocket:
                        Random rand = new Random();
                        int nX = rand.nextInt(10) + 5;
                        int nY = rand.nextInt(10) + 5;
                        VecUnitID unitsInRocket = unit.structureGarrison();
                        if (gc.canLaunchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY)) && unitsInRocket.size()>0) {
                            gc.launchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY));
                            System.out.println("BLASTOFF!");
                        }
                        break;
                }
                units = gc.myUnits();
            }
            gc.nextTurn();
        }
    }

    public static void runWorker(Unit unit) {
        VecUnit nearbyRockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, UnitType.Rocket);
        if (gc.canBlueprint(unit.id(), UnitType.Rocket, directions[0])) {
            gc.blueprint(unit.id(), UnitType.Rocket, directions[0]);
        }
        for (int i = 0; i < nearbyRockets.size(); i++) {
            if (nearbyRockets.get(i).structureIsBuilt() == 0) {//0 false, 1 true?
                if (gc.canBuild(unit.id(), nearbyRockets.get(i).id())) {
                    gc.build(unit.id(), nearbyRockets.get(i).id());
                }
            } else if (nearbyRockets.get(i).structureIsBuilt() == 1) {
                if (gc.canLoad(nearbyRockets.get(i).id(), unit.id())) {
                    gc.load(nearbyRockets.get(i).id(), unit.id());
                }
            }

//            PlanetMap map = new PlanetMap();
            /*Random rand = new Random();
//            int nX = rand.nextInt((int) map.getWidth());
//            int nY = rand.nextInt((int) map.getWidth());
            int nX = rand.nextInt(20);
            int nY = rand.nextInt(20);
            VecUnitID unitsInRocket = nearbyRockets.get(i).structureGarrison();
            if (gc.canLaunchRocket(nearbyRockets.get(i).id(), new MapLocation(Planet.Mars, nX, nY)) && unitsInRocket.size()>0) {
                gc.launchRocket(nearbyRockets.get(i).id(), new MapLocation(Planet.Mars, nX, nY));
                System.out.println("BLASTOFF!");
            }*/
        }
    }
//        for (int i = 0; i < 8; i++) {
//            if (gc.canBlueprint(unit.id(), UnitType.Factory, directions[i])) {
//                gc.blueprint(unit.id(), UnitType.Factory, directions[i]);
//                continue;
//            }
//            if (gc.canReplicate(unit.id(), directions[i])) {
//                gc.replicate(unit.id(), directions[i]);
//                continue;
//            }
//            if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), directions[i])) {
//                gc.moveRobot(unit.id(), directions[i]);
//            }
//        }
}
