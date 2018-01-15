
import bc.*;
import java.util.*;
import java.io.*;

public class Ranger {

    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit nearbyEnemies;
    public static VecUnit Erangers, Emages, Eknights, Ehealers, Eworkers, Efactories, Erockets;
    public static VecUnit Mrangers, Mmages, Mknights, Mhealers, Mworkers, Mfactories, Mrockets;
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
        try {
            if (myTeam == Team.Blue) { //if team is blue then sense for the red team
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
            } else if (myTeam == Team.Red) { //vice versa
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
            }
            Erangers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Ranger);
            Emages = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Mage);
            Eknights = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Knight);
            Ehealers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Healer);
            Eworkers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Worker);
            Efactories = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Factory);
            Erockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Rocket);

            if (unit.attackHeat() < 10) {  // can only attack once attack heat is less than 10, 20 is added for every attack and 10 is taken away for every round
                attackEarth();
            }
        } catch (Exception e) {

        }
    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round
        try {
            if (myTeam == Team.Blue) { //if team is blue then sense for the red team
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
            } else if (myTeam == Team.Red) { //vice versa
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
            }
            Mrangers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Ranger);
            Mmages = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Mage);
            Mknights = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Knight);
            Mhealers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Healer);
            Mworkers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Worker);
            Mfactories = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Factory);
            Mrockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Rocket);

            if(unit.attackHeat() < 10) {
                attackMars();
            }
        } catch (Exception e) {

        }
    }

    public void attackEarth() {
        try {
            for (int i = 0; i < nearbyEnemies.size(); i++) {
                if (gc.canAttack(unit.id(), nearbyEnemies.get(i).id())) {
                    if (unit.rangerCannotAttackRange() == 0) { //makes sure that the enemy units is not in the cannot attack range
                        if (gc.canSenseUnit(Emages.get(i).id()) && Emages.get(i).team() != myTeam) {  // prioritizes certain units
                            gc.attack(unit.id(), Emages.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Erangers.get(i).id()) && Erangers.get(i).team() != myTeam && Erangers.get(i).rangerIsSniping() == 1) { //when the enemy ranger is charging up their active ability
                            gc.attack(unit.id(), Erangers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Erangers.get(i).id()) && Erangers.get(i).team() != myTeam && Erangers.get(i).rangerIsSniping() == 0) { // when they are not
                            gc.attack(unit.id(), Erangers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Eknights.get(i).id()) && Eknights.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Eknights.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Erockets.get(i).id()) && Erockets.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Erockets.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Efactories.get(i).id()) && Efactories.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Efactories.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Ehealers.get(i).id()) && Ehealers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Ehealers.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Eworkers.get(i).id()) && Eworkers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Eworkers.get(i).id());
                            return;
                        }
                    }
                }
            }

        } catch (Exception e) {

        }

    }

    public void attackMars() {
        try {
            for(int i = 0; i < nearbyEnemies.size(); i++) {
                if(gc.canAttack(unit.id(), nearbyEnemies.get(i).id())) {
                    if(unit.rangerCannotAttackRange() == 0) {
                        if(gc.canSenseUnit(Mrockets.get(i).id()) && Mrockets.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mrockets.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Mmages.get(i).id()) && Mmages.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mmages.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Mrangers.get(i).id()) && Mrangers.get(i).team() != myTeam) {
                            if(Mrangers.get(i).rangerIsSniping() == 1) {
                                gc.attack(unit.id(), Mrangers.get(i).id());
                                return;
                            } else {
                                gc.attack(unit.id(), Mrangers.get(i).id());
                                return;
                            }
                        } else if(gc.canSenseUnit(Mknights.get(i).id()) && Mknights.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mknights.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Mhealers.get(i).id()) && Mhealers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mhealers.get(i).id());
                            return;
                        } else if(gc.canSenseUnit(Mworkers.get(i).id()) && Mworkers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mworkers.get(i).id());
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

    }
}
