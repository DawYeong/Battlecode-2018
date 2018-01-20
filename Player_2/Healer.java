import bc.*;

import java.util.*;
import java.io.*;

public class Healer {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit ownteam;

    Healer(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {

    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round

    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round

    }

    public void healing() {
        try {
            long lowestHealth = 0;
            ownteam = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 5, unit.team());
            for (int i = 0; i < ownteam.size(); i++) {
                if (i == 0) {
                    lowestHealth = ownteam.get(0).health();
                }
                if (ownteam.get(i).health() < lowestHealth) {
                    lowestHealth = ownteam.get(i).health();
                }
            }
            for (int j = 0; j < ownteam.size(); j++) {
                if (ownteam.get(j).health() == lowestHealth) {
                    if (ownteam.get(j).location().mapLocation().isWithinRange(unit.attackRange(), unit.location().mapLocation())) {
                        if (gc.canHeal(unit.id(), ownteam.get(j).id())) {
                            gc.heal(unit.id(), ownteam.get(j).id());
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
