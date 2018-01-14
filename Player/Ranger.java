
import bc.*;
import java.util.*;
import java.io.*;

public class Ranger {

    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit nearbyEnemies;
    public static VecUnit rangers, mages, knights, healers, workers, factories, rockets;
    public static UnitType Typeofenemies;
    public static Team myTeam = Player.myTeam;

    Ranger(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {

    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        if (myTeam == Team.Blue) { //if team is blue then sense for the red team
            nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
        } else if (myTeam == Team.Red) { //vice versa
            nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
        }
        rangers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Ranger);
        mages = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Mage);
        knights = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Knight);
        healers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Healer);
        workers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Worker);
        factories = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Factory);
        rockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Rocket);

    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round

    }

    public void attackEarth() {
        try {
            for (int i = 0; i < nearbyEnemies.size(); i++) {
                if (gc.canAttack(unit.id(), nearbyEnemies.get(i).id())) {
                    if (unit.rangerCannotAttackRange() == 0) { //makes sure that the enemy units is not in the cannot attack range
                        if (gc.canSenseUnit(mages.get(i).id()) && mages.get(i).team() != myTeam) {  // prioritizes certain units
                            gc.attack(unit.id(), mages.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(rangers.get(i).id()) && rangers.get(i).team() != myTeam && rangers.get(i).rangerIsSniping() == 1) { //when the enemy ranger is charging up their active ability
                            gc.attack(unit.id(), rangers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(rangers.get(i).id()) && rangers.get(i).team() != myTeam && rangers.get(i).rangerIsSniping() == 0) { // when they are not
                            gc.attack(unit.id(), rangers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(knights.get(i).id()) && knights.get(i).team() != myTeam) {
                            gc.attack(unit.id(), knights.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(rockets.get(i).id()) && rockets.get(i).team() != myTeam) {
                            gc.attack(unit.id(), rockets.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(factories.get(i).id()) && factories.get(i).team() != myTeam) {
                            gc.attack(unit.id(), factories.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(healers.get(i).id()) && healers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), healers.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(workers.get(i).id()) && workers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), workers.get(i).id());
                            return;
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
    }
}
