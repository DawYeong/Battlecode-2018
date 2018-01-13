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

    public static void main(String[] args) {
        gc.queueResearch(UnitType.Rocket);
        for (int i = 0; i < gc.myUnits().size(); i++) {
            workers.add(new Worker(gc.myUnits().get(i)));
        }
        while (true) {
            try {
                units = gc.myUnits();
                System.out.println("Current round: " + gc.round() + "\tKarbonite: " + gc.karbonite() + "\tTotal Units: " + units.size());
                for (int i = 0; i < units.size(); i++) {
                    unit = units.get(i);
                    switch (unit.unitType()) {
                        case Worker: //We don't need UnitType.Worker since it is an enum
                            for (int j = 0; j < workers.size(); j++) {
                                if (workers.get(j).unit.id() == unit.id()) {
                                    workers.get(j).run();
                                    break;
                                }
                            }

                            break;
                        case Rocket:
                            for (int j = 0; j < rockets.size(); j++) {
                                if (rockets.get(j).unit.id() == unit.id()) {
                                    if(unit.structureIsBuilt()==1) {
                                        rockets.get(j).run();
                                        break;
                                    }
                                }
                            }
                            break;
                        case Factory:
                            for (int j = 0; j < factories.size(); j++) {
                                if (factories.get(j).unit.id() == unit.id()){
                                    if(unit.structureIsBuilt()==1) {
                                        factories.get(j).run();
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                    units = gc.myUnits();
                }
            } catch (Exception e) {
            }
            gc.nextTurn();
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
        return creator;//Shouldn't ever reach here
    }
}


