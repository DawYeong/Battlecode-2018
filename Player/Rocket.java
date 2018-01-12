import bc.*;
import java.util.*;
import java.io.*;

public class Rocket extends Unit {
    public static Unit unit;
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    Rocket(Unit unit){
        this.unit = unit;
        init();
    }

    public static void init(){

    }

    public static void run(){
        Random rand = new Random();
        int nX = rand.nextInt(10) + 5;
        int nY = rand.nextInt(10) + 5;
        VecUnitID unitsInRocket = unit.structureGarrison();
        if (gc.canLaunchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY)) && unitsInRocket.size() > 0) {
            gc.launchRocket(unit.id(), new MapLocation(Planet.Mars, nX, nY));
        } else if(unitsInRocket.size()==0){
            for(int i = 0; i < Player.workers.size(); i++) {
                if (gc.canLoad(unit.id(), Player.workers.get(i).id())){
                    gc.load(unit.id(), Player.workers.get(i).id());
                }
            }
        }
    }
}
