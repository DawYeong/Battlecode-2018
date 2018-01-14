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
    public boolean loadReady = false, shouldBlueprint = true;

    Worker(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {
    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        try {
            project = UnitType.Rocket;
            nearbyStructures = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, project);//Need to change so it only checks for our team
            if (unit.abilityHeat() == 0 && Player.workers.size()<Player.maxWorkerAmount) {//Check if can use ability
                job = "replicate";
            } else if (hasBuildable(nearbyStructures)) {
                job = "build";
            } else if (hasRepairable(nearbyStructures)) {
                job = "repair";
            } else if (gc.karbonite() >= bc.bcUnitTypeBlueprintCost(project) && shouldBlueprint) {
                job = "blueprint";
            } else {
                job = "harvest";
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
                case "replicate":
                    replicate();
                    break;
                default:
                    break;
            }
            if (gc.isMoveReady(unit.id()) && !loadReady) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public void runMars(){
        this.unit = Player.unit;//Need to update this every round
        try {
            project = UnitType.Rocket;
            nearbyStructures = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, project);//Need to change so it only checks for our team
            if (unit.abilityHeat() == 0) {//Check if can use ability
                job = "replicate";
            } else if (hasRepairable(nearbyStructures)) {
                job = "repair";
            } else {
                job = "harvest";
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
                case "replicate":
                    replicate();
                    break;
                default:
                    break;
            }
            if (gc.isMoveReady(unit.id()) && !loadReady) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public void move() {
        try {
//            for (int i = 0; i < 8; i++) {
//                if (gc.canMove(unit.id(), directions[i])) {
//                    gc.moveRobot(unit.id(), directions[i]);
//                    return;
//                }
//            }
            Random rand = new Random();
            int nRandom = rand.nextInt(8);
            if(gc.canMove(unit.id(), directions[nRandom])){
                gc.moveRobot(unit.id(), directions[nRandom]);
            }
        } catch (Exception e) {
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
            for (int i = 0; i < 8; i++) {
                if (gc.canBlueprint(unit.id(), project, directions[i])) {
                    gc.blueprint(unit.id(), project, directions[i]);
                    switch (project){
                        case Rocket:
                            Player.rockets.add(new Rocket(Player.findUnit(unit, project)));
                            break;
                        case Factory:
                            Player.factories.add(new Factory(Player.findUnit(unit, project)));
                            break;
                    }

                    shouldBlueprint = false;
                    return;
                }
            }
        } catch (Exception e) {

        }
    }

    public void build() {
        try {
            for (int i = 0; i < nearbyStructures.size(); i++) {
                if (nearbyStructures.get(i).structureIsBuilt() == 0) {//0 false, 1 true?
                    if (gc.canBuild(unit.id(), nearbyStructures.get(i).id())) {
                        gc.build(unit.id(), nearbyStructures.get(i).id());
                        return;
                    }
                } else {
                    loadReady = true;
                }
            }
        } catch (Exception e) {
        }
    }

    public void repair() {
        try {
            for (int i = 0; i < nearbyStructures.size(); i++) {
                if (gc.canRepair(unit.id(), nearbyStructures.get(i).id())) {
                    gc.repair(unit.id(), nearbyStructures.get(i).id());
                    return;
                }
            }
        } catch (Exception e) {

        }
    }

    public void replicate() {
        try {
            for (int i = 0; i < 8; i++) {
                if (gc.canReplicate(unit.id(), directions[i])) {
                    gc.replicate(unit.id(), directions[i]);
                    Player.workers.add(new Worker(Player.findUnit(unit, UnitType.Worker)));
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean hasBuildable(VecUnit structures) {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (structures.get(i).structureIsBuilt() == 0) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean hasRepairable(VecUnit structures) {
        try {
            for (int i = 0; i < structures.size(); i++) {
                if (structures.get(i).health() < structures.get(i).maxHealth()) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }
}
