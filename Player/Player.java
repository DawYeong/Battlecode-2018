import bc.*;

import java.util.*;
import java.io.*;

/************************************************************************************************
 * How Team Array Is Stored:
 *              EARTH
 * 0 - Number of Rockets launched to Mars
 * 1 - Number of Workers launched to Mars
 * 2 - Number of Rangers launched to Mars
 * 3 - Number of Mages   launched to Mars
 * 4 - Number of Healers launched to Mars
 * 5 - Number of Knights launched to Mars
 *
 *              MARS
 * 0 - Number of Rockets added
 * 1 - Number of Workers added
 * 2 - Number of Rangers added
 * 3 - Number of Mages   added
 * 4 - Number of Healers added
 * 5 - Number of Knights added
 *
 */

public class Player {
    public static GameController gc = new GameController();
    public static MapLocation ml = new MapLocation(Planet.Earth, 10, 20);
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
    public static Cell GridEarth[][];
    public static Cell GridMars[][];
    public static VecUnit units;
    public static int maxWorkerAmount = 10;
    public static ArrayList<Rocket> rockets = new ArrayList<>();
    public static ArrayList<Factory> factories = new ArrayList<>();
    public static ArrayList<Worker> workers = new ArrayList<>();
    public static ArrayList<Ranger> rangers = new ArrayList<>();
    public static ArrayList<Mage> mages = new ArrayList<>();
    public static ArrayList<Healer> healers = new ArrayList<>();
    public static ArrayList<Knight> knights = new ArrayList<>();
    public static Unit unit;
    public static Veci32 communications;//TEAM ARRAY to comm. between Earth and Mars
    public static long latestArrayIndex = 0;
    public static PlanetMap EarthMap = gc.startingMap(Planet.Earth);
    public static PlanetMap MarsMap = gc.startingMap(Planet.Mars);

    public static void main(String[] args) {
        gc.queueResearch(UnitType.Rocket);
        GridEarth = new Cell[(int) EarthMap.getHeight()][(int) EarthMap.getWidth()];
        GridMars = new Cell[(int) MarsMap.getHeight()][(int) MarsMap.getWidth()];
        constructGrid();
        for (int i = 0; i < gc.myUnits().size(); i++) {
            workers.add(new Worker(gc.myUnits().get(i)));
        }
        while (true) {
            try {
//                if(latestArrayIndex==99)latestArrayIndex = 0;
                units = gc.myUnits();
                communications = gc.getTeamArray((gc.planet() == Planet.Earth) ? Planet.Mars : Planet.Earth);//Set comms to teamArray of other planets
                if (gc.planet() == Planet.Mars) checkArrayLists(units);
                maxWorkerAmount = rockets.size() + factories.size() + 1;
                System.out.println("Current round: " + gc.round() + "\tKarbonite: " + gc.karbonite() + "\tTotal Units: " + units.size() + "\tPlanet: " + gc.planet());
                for (int i = 0; i < units.size(); i++) {
                    unit = units.get(i);
                    if (unit.location().isInGarrison()) {//Skip over these units
                        continue;
                    }
                    switch (unit.unitType()) {
                        case Rocket:
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < rockets.size(); j++) {
                                    if (rockets.get(j).unit.id() == unit.id()) {
                                        if (unit.structureIsBuilt() == 1) {
                                            rockets.get(j).runEarth();
                                        }
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("RO");
                            } else {
                                for (int j = 0; j < rockets.size(); j++) {
                                    if (rockets.get(j).unit.id() == unit.id()) {
                                        rockets.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("RO");
                            }
                            break;
                        case Factory:
                            for (int j = 0; j < factories.size(); j++) {
                                if (factories.get(j).unit.id() == unit.id()) {
                                    if (unit.structureIsBuilt() == 1) {
                                        factories.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("F");
                            }
                            break;
                        case Worker: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < workers.size(); j++) {
                                    if (workers.get(j).unit.id() == unit.id()) {
                                        workers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("W");
                            } else {
                                for (int j = 0; j < workers.size(); j++) {
                                    if (workers.get(j).unit.id() == unit.id()) {
                                        workers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("W");
                            }
                            break;
                        case Ranger: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < rangers.size(); j++) {
                                    if (rangers.get(j).unit.id() == unit.id()) {
                                        rangers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("RA");
                            } else {
                                for (int j = 0; j < rangers.size(); j++) {
                                    if (rangers.get(j).unit.id() == unit.id()) {
                                        rangers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("RA");
                            }
                            break;
                        case Mage: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < mages.size(); j++) {
                                    if (mages.get(j).unit.id() == unit.id()) {
                                        mages.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("M");
                            } else {
                                for (int j = 0; j < mages.size(); j++) {
                                    if (mages.get(j).unit.id() == unit.id()) {
                                        mages.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("M");
                            }
                            break;
                        case Healer: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < healers.size(); j++) {
                                    if (healers.get(j).unit.id() == unit.id()) {
                                        healers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("H");
                            } else {
                                for (int j = 0; j < healers.size(); j++) {
                                    if (healers.get(j).unit.id() == unit.id()) {
                                        healers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("H");
                            }
                            break;
                        case Knight: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < knights.size(); j++) {
                                    if (knights.get(j).unit.id() == unit.id()) {
                                        knights.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("K");
                            } else {
                                for (int j = 0; j < knights.size(); j++) {
                                    if (knights.get(j).unit.id() == unit.id()) {
                                        knights.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("K");
                            }
                            break;
                    }
                    units = gc.myUnits();
                }
                /*for (int i = 0; i < workers.size(); i++) {
                    for(int j = 0; j < gc.myUnits().size(); j++){
                        if(workers.get(i).unit.id()==gc.myUnits().get(j).id()){
                            unit = gc.myUnits().get(j);
                            break;
                        }
                    }
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    workers.get(i).run();
                }
                for (int i = 0; i < rangers.size(); i++) {
                    unit = rangers.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    rangers.get(i).run();
                }
                for (int i = 0; i < mages.size(); i++) {
                    unit = mages.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    mages.get(i).run();
                }
                for (int i = 0; i < healers.size(); i++) {
                    unit = healers.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    healers.get(i).run();
                }
                for (int i = 0; i < knights.size(); i++) {
                    unit = knights.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    knights.get(i).run();
                }
                for (int i = 0; i < factories.size(); i++) {
                    unit = factories.get(i).unit;
                    if (unit.structureIsBuilt() == 0 || unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    factories.get(i).run();
                }
                for (int i = 0; i < rockets.size(); i++) {
                    for(int j = 0; j < gc.myUnits().size(); j++){
                        if(rockets.get(i).unit.id()==gc.myUnits().get(j).id()){
                            unit = gc.myUnits().get(j);
                            break;
                        }
                    }
                    if (unit.structureIsBuilt() == 0 || unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    rockets.get(i).run();
                }*/

            } catch (Exception e) {
            }
            gc.nextTurn();
        }
    }

    /*public static void callRun(ArrayList alUnits){
        for(int i = 0; i < alUnits.size(); i++){
            alUnits.get(i).run();
        }
    }*/
    public static void checkArrayLists(VecUnit units) {
        if (communications.get(0) > gc.getTeamArray(Planet.Mars).get(0)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = rockets.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < rockets.size(); k++) {
                        if (units.get(j).id() == rockets.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        rockets.add(new Rocket(units.get(j)));
                    }
                }
                if (startArraySize == rockets.size()) {
                    break;
                }
            }
        }
        if (communications.get(1) > gc.getTeamArray(Planet.Mars).get(1)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = workers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < workers.size(); k++) {
                        if (units.get(j).id() == workers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        workers.add(new Worker(units.get(j)));
                    }
                }
                if (startArraySize == workers.size()) {
                    break;
                }
            }
        }
        if (communications.get(2) > gc.getTeamArray(Planet.Mars).get(2)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = rangers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < rangers.size(); k++) {
                        if (units.get(j).id() == rangers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        rangers.add(new Ranger(units.get(j)));
                    }
                }
                if (startArraySize == rangers.size()) {
                    break;
                }
            }
        }
        if (communications.get(3) > gc.getTeamArray(Planet.Mars).get(3)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = mages.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < mages.size(); k++) {
                        if (units.get(j).id() == mages.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        mages.add(new Mage(units.get(j)));
                    }
                }
                if (startArraySize == mages.size()) {
                    break;
                }
            }
        }
        if (communications.get(4) > gc.getTeamArray(Planet.Mars).get(4)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = healers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < healers.size(); k++) {
                        if (units.get(j).id() == healers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        healers.add(new Healer(units.get(j)));
                    }
                }
                if (startArraySize == healers.size()) {
                    break;
                }
            }
        }
        if (communications.get(5) > gc.getTeamArray(Planet.Mars).get(5)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = knights.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < knights.size(); k++) {
                        if (units.get(j).id() == knights.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        knights.add(new Knight(units.get(j)));
                    }
                }
                if (startArraySize == knights.size()) {
                    break;
                }
            }
        }
    }

    public static Unit findUnit(Unit creator, UnitType type) {
        VecUnit nearby = gc.myUnits();//gc.senseNearbyUnitsByTeam(creator.location().mapLocation(), 1, myTeam);
        for (int i = 0; i < nearby.size(); i++) {
            boolean found = false;
            if (nearby.get(i).unitType() == type) {
                switch (type) {
                    case Worker:
                        for (int j = 0; j < Player.workers.size(); j++) {
                            if (Player.workers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Ranger:
                        for (int j = 0; j < Player.rangers.size(); j++) {
                            if (Player.rangers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Mage:
                        for (int j = 0; j < Player.mages.size(); j++) {
                            if (Player.mages.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Healer:
                        for (int j = 0; j < Player.healers.size(); j++) {
                            if (Player.healers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Knight:
                        for (int j = 0; j < Player.knights.size(); j++) {
                            if (Player.knights.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Factory:
                        for (int j = 0; j < Player.factories.size(); j++) {
                            if (Player.factories.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Rocket:
                        for (int j = 0; j < Player.rockets.size(); j++) {
                            if (Player.rockets.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                }
                if (!found) {
                    return nearby.get(i);
                }
            }
        }
        return null;//Shouldn't ever reach here
    }

    public static void constructGrid(){
        try {
            MapLocation tempLocationEarth = new MapLocation(Planet.Earth, 0, 0);
            MapLocation tempLocationMars = new MapLocation(Planet.Earth, 0, 0);
            for (int y = 0; y < GridEarth[0].length; y++) {
                for (int x = 0; x < GridEarth[1].length; x++) {
                    tempLocationEarth.setX(x);
                    tempLocationEarth.setY(y);
                    Cell c;
                    if (EarthMap.isPassableTerrainAt(tempLocationEarth) != 0) {
                        c = new Cell(x, y, true, " ");
                        GridEarth[y][x] = c;
                    } else {
                        c = new Cell(x, y, false, "--");
                        GridEarth[y][x] = c;
                    }
                }
            }
            for (int y = 0; y < GridMars[0].length; y++) {
                for (int x = 0; x < GridMars[1].length; x++) {
                    tempLocationMars.setX(x);
                    tempLocationMars.setY(y);
                    Cell c;
                    if (MarsMap.isPassableTerrainAt(tempLocationMars) != 0) {
                        c = new Cell(x, y, true, " ");
                        GridMars[y][x] = c;
                    } else {
                        c = new Cell(x, y, false, "--");
                        GridMars[y][x] = c;
                    }
                }
            }
        } catch (Exception e){

        }
    }
}


