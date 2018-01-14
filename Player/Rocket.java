import bc.*;

import java.util.*;
import java.io.*;

public class Rocket {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static Team myTeam = Player.myTeam;
    public VecUnitID unitsInStructure;

    Rocket(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {

    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        unitsInStructure = unit.structureGarrison();
        if (unitsInStructure.size() > 0) {
            Random rand = new Random();
            int nX = rand.nextInt(10) + 5;
            int nY = rand.nextInt(10) + 5;
            if(gc.canLaunchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY))) {
                gc.writeTeamArray(Player.latestArrayIndex++, unit.id());
                gc.launchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY));
            }
        } else if (unitsInStructure.size() == 0) {
            VecUnit nearbyWorkers = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 1, myTeam);
            for (int i = 0; i < nearbyWorkers.size(); i++) {
                if (nearbyWorkers.get(i).unitType() == UnitType.Worker) {
                    if (gc.canLoad(unit.id(), nearbyWorkers.get(i).id())) {
                        gc.load(unit.id(), nearbyWorkers.get(i).id());
                    }
                }
            }
        }
    }

    public void runMars(){
        this.unit = Player.unit;//Need to update this every round
        unitsInStructure = unit.structureGarrison();
        System.out.println("ON MARS");
        if(unitsInStructure.size()>0){
            for(int i = 0; i < 8; i++){
                System.out.println("TRYING TO UNLOAD");
                if(gc.canUnload(unit.id(), directions[i])){
                    gc.unload(unit.id(), directions[i]);
                    break;
                }
            }
        } else {
            //Player.rockets.remove(this);//No reason to check the rocket anymore
        }
    }
}
