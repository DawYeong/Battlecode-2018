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
    public int projectId = -1, firstFactoryId;

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
            if (!gc.canBuild(unit.id(), projectId) || gc.unit(projectId).structureIsBuilt() == 1) {
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
            System.out.println("HERE3");
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
            System.out.println("HERE2");
            if (gc.isMoveReady(unit.id()) && shouldMove) {
                System.out.println("HERE");
                move();
            }
        } catch (Exception e) {
        }
    }

    public void move() {
        try {
            if (gc.planet() == Planet.Earth) {
                if (projectId == -1) {
                    Direction oppositeMiddle = unit.location().mapLocation().directionTo(unit.location().mapLocation().subtract(unit.location().mapLocation().directionTo(gc.unit(firstFactoryId).location().mapLocation())));
                    int directionIndex = Arrays.asList(directions).indexOf(oppositeMiddle);
                    for (int i = 0; i < 5; i++) {
                        int di1 = directionIndex + i;
                        int di2 = directionIndex - i;
                        if (di1 > 8) di1 -= 8;
                        if (di2 < 0) di2 += 8;
                        MapLocation nextSpot = unit.location().mapLocation().add(directions[di1]);
                        Location loc = new Location();
                        loc.mapLocation().setX(nextSpot.getX());
                        loc.mapLocation().setY(nextSpot.getY());
                        System.out.println(loc.mapLocation().getX() + " " + loc.mapLocation().getY());
                        if (nextSpot.getX()>=0 && nextSpot.getX() <=Player.EarthMap.getWidth()-1 && nextSpot.getY()>=0 && nextSpot.getY()<=Player.EarthMap.getHeight()-1 && gc.canMove(unit.id(), directions[di1])) {
                            gc.moveRobot(unit.id(), directions[di1]);
                        } else {
                            nextSpot = unit.location().mapLocation().add(directions[di2]);
                            if(nextSpot.getX()>=0 && nextSpot.getX() <=Player.EarthMap.getWidth()-1 && nextSpot.getY()>=0 && nextSpot.getY()<=Player.EarthMap.getHeight()-1 && gc.canMove(unit.id(), directions[di2])) {
                                gc.moveRobot(unit.id(), directions[di2]);
                            }
                        }

                    }
                } else {
                    finder = new Finder(
                            Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                            Player.GridEarth[gc.unit(projectId).location().mapLocation().getY()][gc.unit(projectId).location().mapLocation().getX()],
                            Player.GridEarth);
                    finder.findPath();
                    if (finder.bPathFound) {
                        finder.reconstructPath();
                        if (gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
                            gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
                        } else if (gc.canBuild(unit.id(), projectId)) {
                            shouldMove = false;
                        }
                    }
                }
            } else {
//                finder = new Finder(
//                        Player.GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
//                        Player.GridMars[gc.unit(projectId).location().mapLocation().getY()][gc.unit(projectId).location().mapLocation().getX()],
//                        Player.GridMars);
//                finder.findPath();
//                if(finder.bPathFound){
//                    finder.reconstructPath();
//                    if(gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
//                        gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
//                    }
//                }
            }
        } catch (Exception e) {
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
            if (projectId != -1) {
                if (gc.canBuild(unit.id(), projectId)) {
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
//            if (unit.abilityHeat() == 0 && Player.workers.size()<Player.maxWorkerAmount) {//Check if can use ability
//                job = "replicate";
//            } else if (hasBuildable(nearbyStructures)) {
//                job = "build";
//            } else if (hasRepairable(nearbyStructures)) {
//                job = "repair";
//            } else if (gc.karbonite() >= bc.bcUnitTypeBlueprintCost(project) && shouldBlueprint) {
//                job = "blueprint";
//            } else {
//                job = "harvest";
//            }
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
            int i = Arrays.asList(directions).indexOf(unit.location().mapLocation().directionTo(gc.unit(firstFactoryId).location().mapLocation()));
            for (int j = 0; j < 8; j++) {
                if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                    if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getX() - gc.unit(firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                        if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getY() - gc.unit(firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                            gc.blueprint(unit.id(), project, directions[(j + i) % 8]);
                            switch (project) {
                                case Rocket:
                                    Player.rockets.add(new Rocket(Player.findUnit(project)));
                                    break;
                                case Factory:
                                    Player.factories.add(new Factory(Player.findUnit(project)));
                                    break;
                            }
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
            gc.build(unit.id(), projectId);
            if (gc.unit(projectId).structureIsBuilt() == 1) {
                projectId = -1;
            }
//            if(projectId==-1) {
//                for (int i = 0; i < Player.rockets.size(); i++) {
//                    if (Player.rockets.get(i).unit.structureIsBuilt() == 1) {
//                        projectId = Player.rockets.get(i).unit.id();
//                        return;
//                    }
//                }
//            }
//            if(projectId==-1) {
//                for (int i = 0; i < Player.factories.size(); i++) {
//                    if (Player.factories.get(i).unit.structureIsBuilt() == 1) {
//                        projectId = Player.factories.get(i).unit.id();
//                    }
//                }
//            }
//            if(projectId!=-1) {
//                if (gc.canBuild(unit.id(), projectId)) {
//                    gc.build(unit.id(), projectId);
//                }
//                if(gc.unit(projectId).structureIsBuilt()==1){
//                    projectId=-1;
//                }
//            }
//            for (int i = 0; i < nearbyStructures.size(); i++) {
//                if (nearbyStructures.get(i).structureIsBuilt() == 0) {//0 false, 1 true?
//                    if (gc.canBuild(unit.id(), nearbyStructures.get(i).id())) {
//                        gc.build(unit.id(), nearbyStructures.get(i).id());
//                        return;
//                    }
//                } else {
//                    loadReady = true;
//                }
//            }
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
            if (Player.factories.size() > 0) {
                int i = Arrays.asList(directions).indexOf(unit.location().mapLocation().directionTo(gc.unit(firstFactoryId).location().mapLocation()));
                for (int j = 0; j < 8; j++) {
                    if (gc.canBlueprint(unit.id(), project, directions[(j + i) % 8])) {
                        if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getX() - gc.unit(firstFactoryId).location().mapLocation().getX()) % 2 == 0) {
                            if (Math.abs(unit.location().mapLocation().add(directions[(j + i) % 8]).getY() - gc.unit(firstFactoryId).location().mapLocation().getY()) % 2 == 0) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    if (gc.canBlueprint(unit.id(), project, directions[i])) {
                        gc.blueprint(unit.id(), project, directions[i]);
                        firstFactoryId = Player.findUnit(project).id();
                        switch (project) {
                            case Rocket:
                                Player.rockets.add(new Rocket(gc.unit(firstFactoryId)));
                                break;
                            case Factory:
                                Player.factories.add(new Factory(gc.unit(firstFactoryId)));
                                break;
                        }
                        job = "skip turn";
                    }
                }
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public void findProject() {
        try {
            VecUnit nearby;
            for (int i = 2; i < Player.EarthMap.getWidth() / 2; i++) {
                nearby = gc.senseNearbyUnitsByType(unit.location().mapLocation(), i, UnitType.Factory);
                for (int j = 0; j < nearby.size(); j++) {
                    if (nearby.get(j).structureIsBuilt() == 0 && nearby.get(j).team() == Player.myTeam) {
                        if (gc.senseNearbyUnits(nearby.get(j).location().mapLocation(), 2).size() < 8) {
                            projectId = nearby.get(j).id();
                            return;
                        }
                    }
                }
                nearby = gc.senseNearbyUnitsByType(unit.location().mapLocation(), i, UnitType.Rocket);
                for (int j = 0; j < nearby.size(); j++) {
                    if (nearby.get(j).id() == 0 && nearby.get(j).team() == Player.myTeam) {
                        if (gc.senseNearbyUnits(nearby.get(j).location().mapLocation(), 2).size() < 8) {
                            projectId = nearby.get(j).id();
                            return;
                        }
                    }
                }
            }
            projectId = -1;
            return;
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



