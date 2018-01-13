import bc.*;
import java.util.*;
import java.io.*;

public class Factory {
    public Unit unit;
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public VecUnitID unitsInStructure;
    Factory(Unit unit){
        this.unit = unit;
        init();
    }

    public void init(){

    }

    public void run(){
        this.unit = Player.unit;//Need to update this every round
        unitsInStructure = unit.structureGarrison();
        if (gc.canProduceRobot(unit.id(), UnitType.Knight)) {
            gc.produceRobot(unit.id(), UnitType.Knight );
            System.out.println("Produced a knight.\t" + unitsInStructure.size());
        }
    }
}
