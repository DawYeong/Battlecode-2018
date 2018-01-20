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
        if (unitsInStructure.size() >= 5) {
            Random rand = new Random();
            int nX = rand.nextInt(10) + 5;
            int nY = rand.nextInt(10) + 5;
            if(gc.canLaunchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY))) {
                gc.writeTeamArray(0, gc.getTeamArray(Planet.Earth).get(0)+1);
                for(int i = 0; i < unitsInStructure.size(); i++){
                    switch (gc.unit(unitsInStructure.get(i)).unitType()){
                        case Worker:
                            gc.writeTeamArray(1, gc.getTeamArray(Planet.Earth).get(1)+1);
                            for(int j = 0; j < Player.workers.size(); j++){
                                if(Player.workers.get(j).unit.id()==unitsInStructure.get(i)){
                                    Player.workers.remove(j);
                                    break;
                                }
                            }
                            break;
                        case Ranger:
                            gc.writeTeamArray(2, gc.getTeamArray(Planet.Earth).get(2)+1);
                            break;
                        case Mage:
                            gc.writeTeamArray(3, gc.getTeamArray(Planet.Earth).get(3)+1);
                            break;
                        case Healer:
                            gc.writeTeamArray(4, gc.getTeamArray(Planet.Earth).get(4)+1);
                            break;
                        case Knight:
                            gc.writeTeamArray(5, gc.getTeamArray(Planet.Earth).get(5)+1);
                            break;
                    }
                }
                gc.launchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY));
                Player.rockets.remove(this);
            }
        } else {
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
        if(unitsInStructure.size()>0){
            for(int i = 0; i < 8; i++){
                if(gc.canUnload(unit.id(), directions[i])){
                    gc.unload(unit.id(), directions[i]);
                    break;
                }
            }
        }
    }
}
