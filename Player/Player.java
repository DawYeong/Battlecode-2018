import bc.*;

import java.util.*;
import java.io.*;


public class Player {
    public static GameController gc = new GameController();
    public static MapLocation ml = new MapLocation(Planet.Earth, 10, 20);
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
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

    public static void main(String[] args) {
        gc.queueResearch(UnitType.Rocket);
        for (int i = 0; i < gc.myUnits().size(); i++) {
            workers.add(new Worker(gc.myUnits().get(i)));
        }
        while (true) {
            try {
                if(latestArrayIndex==99) {
                    latestArrayIndex = 0;
                }
                units = gc.myUnits();
                communications = gc.getTeamArray((gc.planet() == Planet.Earth) ? Planet.Mars : Planet.Earth);//Set comms to teamArray of other planets
                System.out.println("Current round: " + gc.round() + "\tKarbonite: " + gc.karbonite() + "\tTotal Units: " + units.size() + "\tPlanet: " + gc.planet());
                for (int i = 0; i < units.size(); i++) {
                    unit = units.get(i);
                    if (unit.location().isInGarrison()) {//Skip over these units
                        continue;
                    }
                    switch (unit.unitType()) {
                        case Worker: //We don't need UnitType.Worker since it is an enum
                            for (int j = 0; j < workers.size(); j++) {
                                if (workers.get(j).unit.id() == unit.id()) {
                                    if (gc.planet() == Planet.Earth) {
                                        workers.get(j).runEarth();
                                    } else {
                                        workers.get(j).runMars();
                                    }
                                    break;
                                }
                            }

                            break;
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
                            } else {
                                for (int j = 0; j < communications.size(); j++) {
                                    if (communications.get(j) == unit.id()) {
                                        System.out.println("BEFORE");
                                        rockets.get(j).runMars();
                                        System.out.println("AFTER");
                                        break;
                                    }
                                }
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
        return creator;//Shouldn't ever reach here
    }
}


