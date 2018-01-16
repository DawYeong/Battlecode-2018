import bc.*;

import java.util.*;
import java.io.*;

public class Worker {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public UnitType project; //project is for storing the Structure the worker is going to blueprint next
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public String job; //harvest, blueprint, build, repair, replicate
    public VecUnit nearbyStructures;
    public boolean shouldMove = true;
    public Finder finder;
    public Cell projectCell;

    Worker(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {
    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        try {
            if (gc.researchInfo().getLevel(UnitType.Rocket) > 0) {
                project = UnitType.Rocket;
            } else {
                project = UnitType.Factory;
            }
            project = UnitType.Factory;
            if (!gc.canBuild(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id()) || gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).structureIsBuilt() == 1) {
                shouldMove = true;
                findProject();
            }
            findJob();

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
                case "replicate":
                    replicate();
                    break;
                default:
                    break;
            }
            if (gc.isMoveReady(unit.id()) && shouldMove) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round
        try {
            nearbyStructures = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, project);//Need to change so it only checks for our team
            findJob();

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
                case "replicate":
                    replicate();
                    break;
                default:
                    break;
            }
            if (gc.isMoveReady(unit.id()) && shouldMove) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public void move() {
        if (gc.planet() == Planet.Earth) {
            if (projectCell == null) {
                Direction oppositeMiddle = unit.location().mapLocation().directionTo(unit.location().mapLocation().subtract(unit.location().mapLocation().directionTo(gc.unit(Player.firstFactoryId).location().mapLocation())));
                int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
                for (int i = 0; i < 5; i++) {
                    int di1 = directionIndex + i;
                    int di2 = directionIndex - i;
                    if (di1 > 7) di1 -= 8;
                    if (di2 < 0) di2 += 8;
                    if (gc.canMove(unit.id(), directions[di1])) {
                        gc.moveRobot(unit.id(), directions[di1]);
                        break;
                    } else if (gc.canMove(unit.id(), directions[di2])) {
                        gc.moveRobot(unit.id(), directions[di2]);
                        break;
                    }
                }
            } else {
                finder = new Finder(
                        Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                        Player.GridEarth[gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getY()][gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getX()],
                        Player.GridEarth);
                finder.findPath();
                if (finder.bPathFound) {
                    finder.reconstructPath();
                    if (gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
                        gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
                    } else if (gc.canBuild(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id())) {
                        shouldMove = false;
                    }
                }
            }
        } else {
//                finder = new Finder(
//                        Player.GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
//                        Player.GridMars[gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getY()][gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).location().mapLocation().getX()],
//                        Player.GridMars);
//                finder.findPath();
//                if(finder.bPathFound){
//                    finder.reconstructPath();
//                    if(gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
//                        gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
//                    }
//                }
        }
    }

    public void findJob() {
        if (gc.planet() == Planet.Earth) {
            if (unit.abilityHeat() == 0 && Player.workers.size() < Player.maxWorkerAmount) {//Check if can use ability
                if (canReplicate()) {
                    job = "replicate";
                    return;
                }
            }
            if (gc.senseUnitAtLocation(projectCell.getLocation()).id() != -1) {
                if (gc.canBuild(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id())) {
                    job = "build";
                    return;
                }
            }
            nearbyStructures = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, project);//Need to change so it only checks for our team
            if (hasRepairable(nearbyStructures)) {
                job = "repair";
                return;
            }
            if (gc.karbonite() >= bc.bcUnitTypeBlueprintCost(project)) {
                if (canBlueprint()) {
                    job = "blueprint";
                    return;
                }
                if (job == "skip turn") {
                    return;
                }

            }
            job = "harvest";
        } else {

        }
    }

    public void harvest() {
        try {
            int[] karboniteAmounts = new int[9];
            int indexOfMaxAmount = 0, maxAmount = 0;
            for (int i = 0; i < 9; i++) {//Sensing all around Worker for Karbonite
                try { //TryCatch incase MapLocation is off the map and throws an error
                    karboniteAmounts[i] = (int) gc.karboniteAt(unit.location().mapLocation().add(directions[i]));
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
            int i = Arrays.asList(directions).indexOf(unit.location().mapLocation().directionTo(gc.unit(Player.firstFactoryId).location().mapLocation()));
            for (int j = 0; j < 8; j++) {
                if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                    if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getX() - gc.unit(Player.firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                        if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getY() - gc.unit(Player.firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                            gc.blueprint(unit.id(), project, directions[(j + i) % 8]);
                            switch (project) {
                                case Rocket:
                                    Player.rockets.add(new Rocket(Player.findUnit(project)));
                                    break;
                                case Factory:
                                    Player.factories.add(new Factory(Player.findUnit(project)));
                                    break;
                            }
                            project = null;
                            return;
                        }
                    }
                }

            }
        } catch (Exception e) {

        }
    }

    public void build() {
        try {
            gc.build(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id());
            if (gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).structureIsBuilt() == 1) {
                projectCell = null;
            }
        } catch (Exception e) {
        }
    }

    public void repair() {
        try {
            int lowestHealth = (int) nearbyStructures.get(0).health(), structureId = nearbyStructures.get(0).id();
            for (int i = 0; i < nearbyStructures.size(); i++) {
                if (nearbyStructures.get(i).health() < lowestHealth) {
                    lowestHealth = (int) nearbyStructures.get(i).health();
                    structureId = nearbyStructures.get(i).id();
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
            for (int i = 0; i < 8; i++) {
                if (gc.canReplicate(unit.id(), directions[i])) {
                    gc.replicate(unit.id(), directions[i]);
                    Player.workers.add(new Worker(Player.findUnit(UnitType.Worker)));
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean canReplicate() {
        try {
            for (int i = 0; i < 8; i++) {
                if (gc.canReplicate(unit.id(), directions[i])) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean canBlueprint() {
        try {
            if (Player.initializedGrid) {
                int i = Arrays.asList(directions).indexOf(unit.location().mapLocation().directionTo(gc.unit(Player.firstFactoryId).location().mapLocation()));
                for (int j = 0; j < 8; j++) {
                    if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                        if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getX() - gc.unit(Player.firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                            if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getY() - gc.unit(Player.firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                MapLocation centerOfMap = new MapLocation(Planet.Earth, (int) Player.EarthMap.getWidth() / 2, (int) Player.EarthMap.getHeight() / 2);
                Direction oppositeMiddle = unit.location().mapLocation().directionTo(unit.location().mapLocation().subtract(unit.location().mapLocation().directionTo(centerOfMap)));
                int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
                for (int i = 0; i < 5; i++) {
                    int di1 = directionIndex + i;
                    int di2 = directionIndex - i;
                    if (di1 > 7) di1 -= 8;
                    if (di2 < 0) di2 += 8;
                    if (gc.canBlueprint(unit.id(), project, directions[di1])) {
                        gc.blueprint(unit.id(), project, directions[di1]);
                        Player.firstFactoryId = Player.findUnit(project).id();
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(gc.unit(Player.firstFactoryId)));
                                break;
                            case Factory:
                                Player.factories.add(new Factory(gc.unit(Player.firstFactoryId)));
                                break;
                        }
                        job = "skip turn";
                    } else if (gc.canBlueprint(unit.id(), project, directions[di2])) {
                        gc.blueprint(unit.id(), project, directions[di2]);
                        Player.firstFactoryId = Player.findUnit(project).id();
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(gc.unit(Player.firstFactoryId)));
                                break;
                            case Factory:
                                Player.factories.add(new Factory(gc.unit(Player.firstFactoryId)));
                                break;
                        }
                        Player.constructLayout();
                        job = "skip turn";
                        return false;
                    }
                }
            }
            return false;

        } catch (Exception e) {

        }
        return false;
    }

    public void findProject() {
        try {
            projectCell = Player.distances.first();
//            VecUnit nearby;
//            for (int i = 2; i < Player.EarthMap.getWidth(); i++) {
//                nearby = gc.senseNearbyUnitsByType(unit.location().mapLocation(), i, UnitType.Factory);
//                for (int j = 0; j < nearby.size(); j++) {
//                    if (nearby.get(j).structureIsBuilt() == 0 && nearby.get(j).team() == Player.myTeam) {
//                        if (gc.senseNearbyUnits(nearby.get(j).location().mapLocation(), 2).size() < 8) {
//                            projectCell = new Cell(nearby.get(j).location().mapLocation().getX(), nearby.get(j).location().mapLocation().getY(), true, "Fa");
//                            return;
//                        }
//                    }
//                }
//                nearby = gc.senseNearbyUnitsByType(unit.location().mapLocation(), i, UnitType.Rocket);
//                for (int j = 0; j < nearby.size(); j++) {
//                    if (nearby.get(j).id() == 0 && nearby.get(j).team() == Player.myTeam) {
//                        if (gc.senseNearbyUnits(nearby.get(j).location().mapLocation(), 2).size() < 8) {
//                            projectCell = new Cell(nearby.get(j).location().mapLocation().getX(), nearby.get(j).location().mapLocation().getY(), true, "ROa");
//                            return;
//                        }
//                    }
//                }
//            }
//            projectCell = null;
        } catch (Exception e) {

        }
    }

    public boolean hasRepairable(VecUnit structures) {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (structures.get(i).health() < structures.get(i).maxHealth() && structures.get(i).team() == Player.myTeam) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }
}