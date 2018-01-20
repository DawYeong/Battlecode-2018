import bc.*;

import java.util.*;
import java.io.*;

public class Worker {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public UnitType project = UnitType.Factory; //project is for storing the Structure the worker is going to blueprint next
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public String job; //harvest, blueprint, build, repair, replicate
    public VecUnit nearbyStructures;
    public boolean shouldMove = true;
    public Finder finder;
    public Cell projectCell = null;
    public MapLocation location;

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
            /*if (gc.researchInfo().getLevel(UnitType.Rocket) > 0) {
                project = UnitType.Rocket;
            }*/
            project = UnitType.Factory;
            if (Player.initializedGrid) {
                if (projectCell == null) {
                    shouldMove = true;
                    findProject();
                } else if (gc.hasUnitAtLocation(projectCell.getLocation())) {
                    if (gc.senseUnitAtLocation(projectCell.getLocation()).structureIsBuilt() == 1) {
                        shouldMove = true;
                        findProject();
                    }
                }
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
                if (projectCell == null) {
                    Direction oppositeFactory = location.directionTo(location.subtract(location.directionTo(gc.unit(Player.firstFactoryId).location().mapLocation())));
                    int directionIndex = Arrays.asList(directions).indexOf(oppositeFactory);
                    for (int i = 0; i < 5; i++) {
                        int di1 = directionIndex + i;
                        int di2 = directionIndex - i;
                        if (di1 > 7) di1 -= 8;
                        if (di2 < 0) di2 += 8;
                        if (gc.canMove(unit.id(), directions[di1])) {
                            Player.GridEarth[location.getY()][location.getX()].setValue(null);
                            gc.moveRobot(unit.id(), directions[di1]);
                            Player.GridEarth[location.getY()][location.getX()].setValue("--");
                            break;
                        } else if (gc.canMove(unit.id(), directions[di2])) {
                            Player.GridEarth[location.getY()][location.getX()].setValue(null);
                            gc.moveRobot(unit.id(), directions[di2]);
                            Player.GridEarth[location.getY()][location.getX()].setValue("--");
                            break;
                        }
                    }
                } else {
                    finder = new Finder(
<<<<<<< Updated upstream
                            Player.GridEarth[location.getY()][location.getX()],
=======
                            Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
>>>>>>> Stashed changes
                            Player.GridEarth[projectCell.getLocation().getY()][projectCell.getLocation().getX()],
                            Player.GridEarth);
                    finder.findPath();
                    if (finder.bPathFound && finder.getPath().size() > 1) {
                        if (gc.canMove(unit.id(), location.directionTo(finder.getPath().get(0).getLocation()))) {
                            Player.GridEarth[location.getY()][location.getX()].setValue(null);
                            gc.moveRobot(unit.id(), location.directionTo(finder.getPath().get(0).getLocation()));
                            Player.GridEarth[location.getY()][location.getX()].setValue("--");
                        } else {
                            shouldMove = false;
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
                if (unit.abilityHeat() == 0 && Player.workers.size() < Player.maxWorkerAmount + 1) {//Check if can use ability
                    if (canReplicate()) {
                        job = "replicate";
                        return;
                    }
<<<<<<< Updated upstream
                }
                nearbyStructures = gc.senseNearbyUnitsByType(location, 2, project);//Need to change so it only checks for our team
                if (hasBuildable(nearbyStructures)) {
                    job = "build";
                    return;
=======
                }*/
                if (projectCell != null) {
                    if (projectCell.blueprinted && !projectCell.built) {
                        System.out.println("Project Cell Blueprinted.");
                        //if (gc.canBuild(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id())) {
                        job = "build";
                        return;
                        //}
                    }
                    if (projectCell.built) projectCell = null;
                } else {
                    System.out.println("Project cell is null.   ");
>>>>>>> Stashed changes
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
                if (hasRepairable(nearbyStructures)) {
                    job = "repair";
                    return;
                }

                job = "harvest";
            } else {

            }
        } catch (
                Exception e)

        {
        }

    }

    public void harvest() {
        try {
            int[] karboniteAmounts = new int[9];
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
            int i = Arrays.asList(directions).indexOf(location.directionTo(gc.unit(Player.firstFactoryId).location().mapLocation()));
            for (int j = 0; j < 8; j++) {
                if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                    if (Math.abs(location.add(directions[(j + i) % 8]).getX() - gc.unit(Player.firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                        if (Math.abs(location.add(directions[(j + i) % 8]).getY() - gc.unit(Player.firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                            gc.blueprint(unit.id(), project, directions[(j + i) % 8]);
                            shouldMove = false;
                            switch (project) {
                                case Rocket:
                                    Player.rockets.add(new Rocket(Player.findUnit(project)));
                                    break;
                                case Factory:
                                    Player.factories.add(new Factory(Player.findUnit(project)));
                                    break;
                            }
                            projectCell.blueprinted = true;
                            projectCell = null;
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
            if (gc.hasUnitAtLocation(projectCell.getLocation())) {
                gc.build(unit.id(), gc.senseUnitAtLocation(projectCell.getLocation()).id());

                if (gc.unit(gc.senseUnitAtLocation(projectCell.getLocation()).id()).structureIsBuilt() == 1) {
                    Player.distances.remove(projectCell);
                    projectCell.built = true;
                    projectCell = null;

                }
            } else {
                projectCell.blueprinted = false;
                projectCell.built = false;
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
                    if (Player.initializedGrid) {
                        if (Math.abs(location.add(directions[i]).getX() - gc.unit(Player.firstFactoryId).location().mapLocation().getX()) % 2 == 0
                                && Math.abs(location.add(directions[i]).getY() - gc.unit(Player.firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                            continue;
                        }
                    }
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
                int i = Arrays.asList(directions).indexOf(location.directionTo(gc.unit(Player.firstFactoryId).location().mapLocation()));
                for (int j = 0; j < 8; j++) {
                    if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                        if (Math.abs(location.add(directions[(j + i) % 8]).getX() - gc.unit(Player.firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                            if (Math.abs(location.add(directions[(j + i) % 8]).getY() - gc.unit(Player.firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                MapLocation centerOfMap = new MapLocation(Planet.Earth, (int) Player.EarthMap.getWidth() / 2, (int) Player.EarthMap.getHeight() / 2);
                Direction oppositeMiddle = location.directionTo(location.subtract(location.directionTo(centerOfMap)));
                int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
                for (int i = 0; i < 5; i++) {
                    int di1 = directionIndex + i;
                    int di2 = directionIndex - i;
                    if (di1 > 7) di1 -= 8;
                    if (di2 < 0) di2 += 8;
                    if (gc.canBlueprint(unit.id(), project, directions[di1])) {
                        gc.blueprint(unit.id(), project, directions[di1]);
                        shouldMove = false;
                        Player.firstFactoryId = Player.findUnit(project).id();
                        Cell firstFactory = new Cell(gc.unit(Player.firstFactoryId).location().mapLocation().getX(), gc.unit(Player.firstFactoryId).location().mapLocation().getY(), true, "Fa");
                        Player.distances.add(firstFactory);
                        Player.distances.get(0).blueprinted = true;
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
                        shouldMove = false;
                        Player.firstFactoryId = Player.findUnit(project).id();
                        Cell firstFactory = new Cell(gc.unit(Player.firstFactoryId).location().mapLocation().getX(), gc.unit(Player.firstFactoryId).location().mapLocation().getY(), true, "Fa");
                        Player.distances.add(firstFactory);
                        Player.distances.get(0).blueprinted = true;
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(gc.unit(Player.firstFactoryId)));
                                break;
                            case Factory:
                                Player.factories.add(new Factory(gc.unit(Player.firstFactoryId)));
                                break;
                        }
                        job = "skip turn";
                    }
                    Player.constructLayout();
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
        }
        return false;
    }

    public void findProject() {
        try {
            int i = 0;
            do {
                projectCell = (Player.distances.size() > i) ? Player.distances.get(i) : null;
                i++;
            } while (location == projectCell.getLocation() || projectCell.built);
        } catch (Exception e) {
            projectCell = null;
        }
    }

    public boolean hasRepairable(VecUnit structures) {
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

    public boolean hasBuildable(VecUnit structures) {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (gc.canBuild(unit.id(), structures.get(i).id()) && structures.get(i).team() == Player.myTeam && structures.get(i).structureIsBuilt() == 1) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }
}