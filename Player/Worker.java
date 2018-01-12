import bc.*;

import java.util.*;
import java.io.*;

public class Worker {
    public static Unit unit;
    public static UnitType project; //project is for storing the Structure the worker is going to blueprint next
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static String job; //harvest, blueprint, build, repair, replicate
    public static VecUnit nearbyRockets;

    Worker(Unit unit) {
        this.unit = unit;
        init();
    }

    public static void init() {
    }

    public static void run() {
        try {
            nearbyRockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 1, UnitType.Rocket);
            if(unit.abilityHeat()==0){//Check if can use ability
                job = "replicate";
            } else if(hasBuildable(nearbyRockets)){
                job = "build";
            } else if(hasRepairable(nearbyRockets)){
                job = "repair";
            } else if(gc.karbonite()>=bc.bcUnitTypeBlueprintCost(project)/* && shouldBuild==true*/){
                job = "blueprint";
            } else {
                job = "harvest";
            }

            project = UnitType.Rocket;

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
            if (gc.isMoveReady(unit.id())) {
                move();
            }
        } catch (Exception e) {
        }
    }

    public static void move() {
        try {
            for (int i = 0; i < 8; i++) {
                if (gc.canMove(unit.id(), directions[i])) {
                    gc.moveRobot(unit.id(), directions[i]);
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static void harvest() {
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

    public static void blueprint() {
        try {
            for (int i = 0; i < 8; i++) {
                if (gc.canBlueprint(unit.id(), UnitType.Rocket, directions[i]) && gc.karbonite()>=bc.bcUnitTypeBlueprintCost(UnitType.Rocket)) {
                    gc.blueprint(unit.id(), UnitType.Rocket, directions[i]);
                    Player.rockets.add(new Rocket(gc.myUnits().get(gc.myUnits().size() - 1)));
                    return;
                }
            }
        } catch (Exception e) {

        }
    }

    public static void build() {
        try {
            for (int i = 0; i < nearbyRockets.size(); i++) {
                if (nearbyRockets.get(i).structureIsBuilt() == 0) {//0 false, 1 true?
                    if (gc.canBuild(unit.id(), nearbyRockets.get(i).id())) {
                        gc.build(unit.id(), nearbyRockets.get(i).id());
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void repair() {
        try{
            for(int i = 0; i < nearbyRockets.size(); i++){
                if (gc.canRepair(unit.id(), nearbyRockets.get(i).id())) {
                    gc.repair(unit.id(), nearbyRockets.get(i).id());
                    return;
                }
            }
        } catch (Exception e) {

        }
    }

    public static void replicate() {
        try {
            for (int i = 0; i < 8; i++) {
                if (gc.canReplicate(unit.id(), directions[i])) {
                    gc.replicate(unit.id(), directions[i]);
                    Player.workers.add(new Worker(gc.myUnits().get(gc.myUnits().size() - 1)));
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static boolean hasBuildable(VecUnit structures){
        try{
            for(int i = 0; i < structures.size(); i++){
                if(structures.get(i).structureIsBuilt()==0){
                    return true;
                }
            }
        } catch (Exception e){

        }
        return false;
    }

    public static boolean hasRepairable(VecUnit structures){
        try{
            for(int i = 0; i < structures.size(); i++){
                if(structures.get(i).health()<structures.get(i).maxHealth()){
                    return true;
                }
            }
        } catch (Exception e){

        }
        return false;
    }
}
