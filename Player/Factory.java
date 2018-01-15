import bc.*;

import java.util.*;
import java.io.*;

public class Factory {
    public Unit unit;
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public VecUnitID unitsInStructure;

    Factory(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {

    }

    public void runEarth() { //No runMars() since Factories can't be on Mars, left as runEarth() so everything is in unison
        this.unit = Player.unit;//Need to update this every round
        unitsInStructure = unit.structureGarrison();
        for (int i = 0; i < 8; i++) {
            if (unitsInStructure.size() < unit.structureMaxCapacity() && unit.isFactoryProducing() == 0) {
                gc.produceRobot(unit.id(), UnitType.Ranger);
                System.out.println("Produced a ranger");
                Player.rangers.add(new Ranger(Player.findUnit(UnitType.Ranger)));
                break;
            } else if (unitsInStructure.size() > 0 && gc.canUnload(unit.id(), directions[i])) {
                gc.unload(unit.id(), directions[i]);
            } else {
                break;
            }
        }
    }
}
