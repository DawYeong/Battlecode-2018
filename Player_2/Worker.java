import bc.*;

import java.util.*;
import java.io.*;

public class Worker {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public UnitType project = UnitType.Factory; //project is for storing the Structure the worker is going to blueprint next
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public String job; //harvest, blueprint, build, repair, replicate
    public VecUnit structures;
    public boolean shouldMove = true;
    public Finder finder;
    public MapLocation location;
    public ArrayList<Cell> nearbyKarbonite = new ArrayList<>();

    Worker(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {
    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        location = unit.location().mapLocation();
        try {
            job = "";
            if (gc.researchInfo().getLevel(UnitType.Rocket) > 0) {
                project = UnitType.Rocket;
            } else {
                project = UnitType.Factory;
            }
            project = UnitType.Factory;
            findJob();
            if (unit.abilityHeat() == 0 && Player.workers.size() < Player.maxWorkerAmount + 1) {//Check if can use ability
                replicate();//Replicate isn't an action, it's an ability so the cooldown is seperate
            }
            switch (job) {
                case "harvest":
                    harvest();
                    break;
                case "blueprint":
                    blueprint();
                    break;
                case "build":
                    build();
                    break;
                case "repair":
                    repair();
                    break;
            }
            if (gc.isMoveReady(unit.id())/* && shouldMove*/) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public void runMars() {
//        this.unit = Player.unit;//Need to update this every round
//        try {
//            nearbyStructures = gc.senseNearbyUnitsByType(location, 1, project);//Need to change so it only checks for our team
//            findJob();
//
//            switch (job) {
//                case "harvest":
//                    harvest();
//                    break;
//                case "blueprint":
//                    blueprint();
//                    break;
//                case "build":
//                    build();
//                    break;
//                case "repair":
//                    repair();
//                    break;
//                case "replicate":
//                    replicate();
//                    break;
//                default:
//                    break;
//            }
//            if (gc.isMoveReady(unit.id()) && shouldMove) {
//                move();
//            }
//        } catch (Exception e) {
//
//        }
    }

    public void move() {
        try {
            if (gc.planet() == Planet.Earth) {
                if (job == "harvest") {
                    MapLocation karboniteLocation = findKarbonite();
                    finder = new Finder(
                            Player.GridEarth[location.getY()][location.getX()],
                            Player.GridEarth[karboniteLocation.getY()][karboniteLocation.getX()],
                            Player.GridEarth);
                    finder.findPath();
                    System.out.println(finder.bPathFound + "\t" + finder.getPath().size());
                    if (finder.bPathFound && finder.getPath().size() > 0) {
                        if (gc.canMove(unit.id(), location.directionTo(finder.getPath().get(0).getLocation()))) {
                            Player.GridEarth[location.getY()][location.getX()].setValue(null);
                            gc.moveRobot(unit.id(), location.directionTo(finder.getPath().get(0).getLocation()));
                            Player.GridEarth[location.getY()][location.getX()].setValue("--");
                        }
                    }
                }


            } else {
//                finder = new Finder(
//                        Player.GridMars[location.getY()][location.getX()],
//                        Player.GridMars[gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getY()][gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getX()],
//                        Player.GridMars);
//                finder.findPath();
//                if(finder.bPathFound){
//                    finder.reconstructPath();
//                    if(gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), location.directionTo(finder.nextMove()))) {
//                        gc.moveRobot(unit.id(), location.directionTo(finder.nextMove()));
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }

    public void findJob() {
        try {
            if (gc.planet() == Planet.Earth) {
                structures = gc.senseNearbyUnitsByType(location, 2, project);//Need to change so it only checks for our team

                //BUILD
                if (hasBuildable()) {
                    job = "build";
                    return;
                }

                //BLUEPRINT
                if (gc.karbonite() >= bc.bcUnitTypeBlueprintCost(project) && canBlueprint()) {
                    job = "blueprint";
                    return;
                }

                //REPAIR
                if (hasRepairable()) {
                    job = "repair";
                    return;
                }

                //HARVEST
                job = "harvest";
            } else {

            }
        } catch (Exception e) {

        }

    }

    public void harvest() {
        try {
            int[] karboniteAmounts = new int[9];//This is silly but I don't feel like changing it
            int indexOfMaxAmount = 0, maxAmount = 0;
            for (int i = 0; i < 9; i++) {//Sensing all around Worker for Karbonite
                try { //TryCatch incase MapLocation is off the map and throws an error
                    karboniteAmounts[i] = (int) gc.karboniteAt(location.add(directions[i]));
                    if (karboniteAmounts[i] > maxAmount) {
                        maxAmount = karboniteAmounts[i];
                        indexOfMaxAmount = i;
                    }
                } catch (Exception e) {
                }
            }
            if (gc.canHarvest(unit.id(), directions[indexOfMaxAmount])) {
                gc.harvest(unit.id(), directions[indexOfMaxAmount]);
            }
        } catch (Exception e) {

        }
    }

    public void blueprint() {
        try {
            MapLocation centerOfMap = new MapLocation(Planet.Earth, (int) Player.EarthMap.getWidth() / 2, (int) Player.EarthMap.getHeight() / 2);
            Direction oppositeMiddle = location.directionTo(location.subtract(location.directionTo(centerOfMap)));
            int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
            for (int i = 0; i < 5; i++) {
                try {
                    int di1 = directionIndex + i;
                    int di2 = directionIndex - i;
                    if (di1 > 7) di1 -= 8;
                    if (di2 < 0) di2 += 8;
                    if (gc.canBlueprint(unit.id(), project, directions[di1])) {
                        if (hasNearbyStructures(location.add(directions[di1]))) continue;
                        gc.blueprint(unit.id(), project, directions[di1]);
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(Player.findUnit(UnitType.Rocket)));
                                Player.GridEarth[Player.rockets.get(Player.rockets.size() - 1).unit.location().mapLocation().getY()][Player.rockets.get(Player.rockets.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                                break;
                            case Factory:
                                Player.factories.add(new Factory(Player.findUnit(UnitType.Factory)));
                                Player.GridEarth[Player.factories.get(Player.factories.size() - 1).unit.location().mapLocation().getY()][Player.factories.get(Player.factories.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                                break;
                        }
                    } else if (gc.canBlueprint(unit.id(), project, directions[di2])) {
                        if (hasNearbyStructures(location.add(directions[di2]))) continue;
                        gc.blueprint(unit.id(), project, directions[di2]);
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(Player.findUnit(UnitType.Rocket)));
                                Player.GridEarth[Player.rockets.get(Player.rockets.size() - 1).unit.location().mapLocation().getY()][Player.rockets.get(Player.rockets.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                                break;
                            case Factory:
                                Player.factories.add(new Factory(Player.findUnit(UnitType.Factory)));
                                Player.GridEarth[Player.factories.get(Player.factories.size() - 1).unit.location().mapLocation().getY()][Player.factories.get(Player.factories.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                                break;
                        }
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        }
    }

    public boolean hasNearbyStructures(MapLocation loc) {
        for (int i = 0; i < 8; i++) {
            if (gc.hasUnitAtLocation(loc.add(directions[i]))) {
                if (gc.senseUnitAtLocation(loc.add(directions[i])).unitType() == UnitType.Factory || gc.senseUnitAtLocation(loc.add(directions[i])).unitType() == UnitType.Rocket) {
                    return true;
                }
            }
        }
        return false;
    }

    public void build() {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (gc.canBuild(unit.id(), structures.get(i).id()) && structures.get(i).team() == Player.myTeam && structures.get(i).structureIsBuilt() == 0) {
                    gc.build(unit.id(), structures.get(i).id());
                    //shouldMove = (structures.get(i).structureIsBuilt() == 1) ? true : false;
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void repair() {
        try {
            int lowestHealth = (int) structures.get(0).health(), structureId = structures.get(0).id();
            for (int i = 0; i < structures.size(); i++) {
                if (structures.get(i).health() < lowestHealth) {
                    lowestHealth = (int) structures.get(i).health();
                    structureId = structures.get(i).id();
                }
            }
            if (gc.canRepair(unit.id(), structureId)) {
                gc.repair(unit.id(), structureId);
                return;
            }
        } catch (Exception e) {

        }
    }

    public void replicate() {
        try {
            MapLocation centerOfMap = new MapLocation(Planet.Earth, (int) Player.EarthMap.getWidth() / 2, (int) Player.EarthMap.getHeight() / 2);
            Direction oppositeMiddle = location.directionTo(location.subtract(location.directionTo(centerOfMap)));
            int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
            for (int i = 0; i < 5; i++) {
                try {
                    int di1 = directionIndex + i;
                    int di2 = directionIndex - i;
                    if (di1 > 7) di1 -= 8;
                    if (di2 < 0) di2 += 8;
                    if (gc.canReplicate(unit.id(), directions[di1])) {
                        gc.replicate(unit.id(), directions[di1]);
                        Player.workers.add(new Worker(Player.findUnit(UnitType.Worker)));
                        Player.GridEarth[Player.workers.get(Player.workers.size() - 1).unit.location().mapLocation().getY()][Player.workers.get(Player.workers.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                    } else if (gc.canBlueprint(unit.id(), project, directions[di2])) {
                        gc.replicate(unit.id(), directions[di2]);
                        Player.workers.add(new Worker(Player.findUnit(UnitType.Worker)));
                        Player.GridEarth[Player.workers.get(Player.workers.size() - 1).unit.location().mapLocation().getY()][Player.workers.get(Player.workers.size() - 1).unit.location().mapLocation().getX()].setValue("--");
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }


    public boolean hasRepairable() {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (structures.get(i).health() < structures.get(i).maxHealth() && structures.get(i).team() == Player.myTeam && structures.get(i).structureIsBuilt() == 1) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean hasBuildable() {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (gc.canBuild(unit.id(), structures.get(i).id()) && structures.get(i).team() == Player.myTeam && structures.get(i).structureIsBuilt() == 0) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean canBlueprint() {
        for (int i = 0; i < 8; i++) {
            if (gc.canBlueprint(unit.id(), project, directions[i])) {
                MapLocation loc = location.add(directions[i]);
                for (int j = 0; j < 8; j++) {
                    if (gc.hasUnitAtLocation(loc.add(directions[j]))) {
                        if (gc.senseUnitAtLocation(loc.add(directions[j])).unitType() == UnitType.Factory || gc.senseUnitAtLocation(loc.add(directions[j])).unitType() == UnitType.Rocket) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public MapLocation findKarbonite() {
        nearbyKarbonite.clear();
        try {
            if (gc.planet().equals(Planet.Earth)) {
                for (int y = 0; y < Player.GridEarth[0].length; y++) {
                    for (int x = 0; x < Player.GridEarth[1].length; x++) {
                        Cell cell = Player.GridEarth[y][x];
                        if (cell.getKarbonite() > 0) {
                            cell.distance = (int) Math.sqrt(location.distanceSquaredTo(cell.getLocation()));
                            nearbyKarbonite.add(cell);
                        }
                    }
                }
                if (nearbyKarbonite.size() > 0) {
                    Collections.sort(nearbyKarbonite);
                    return new MapLocation(gc.planet(), nearbyKarbonite.get(0).getLocation().getX(), nearbyKarbonite.get(0).getLocation().getY());
                }
            } else {

            }

        } catch (Exception e) {

        }
        return location;
    }
}