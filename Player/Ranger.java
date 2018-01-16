
import bc.*;

import java.util.*;
import java.io.*;

public class Ranger {

    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit nearbyEnemiesA;
    public static VecUnit Erangers, Emages, Eknights, Ehealers, Eworkers, Efactories, Erockets;
    public static VecUnit Mrangers, Mmages, Mknights, Mhealers, Mworkers, Mfactories, Mrockets;
    public static VecUnit vRangers, vKnights, vMages, nearbyEnemiesV;
    public static UnitType Typeofenemies;
    public Finder finder;
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
                nearbyEnemiesA = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
            } else if (myTeam == Team.Red) { //vice versa
                nearbyEnemiesA = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
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
            move();
        } catch (Exception e) {

        }
    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round
        try {
            if (myTeam == Team.Blue) { //if team is blue then sense for the red team
                nearbyEnemiesA = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
            } else if (myTeam == Team.Red) { //vice versa
                nearbyEnemiesA = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
            }
            Mrangers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Ranger);
            Mmages = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Mage);
            Mknights = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Knight);
            Mhealers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Healer);
            Mworkers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Worker);
            Mfactories = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Factory);
            Mrockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 7, UnitType.Rocket);

            if (unit.attackHeat() < 10) {
                attackMars();
            }
        } catch (Exception e) {

        }
    }

    public void attackEarth() {
        try {
            for (int i = 0; i < nearbyEnemiesA.size(); i++) {
                if (gc.canAttack(unit.id(), nearbyEnemiesA.get(i).id())) {
                    if (!Erangers.get(i).location().mapLocation().isWithinRange(unit.rangerCannotAttackRange(), unit.location().mapLocation())) { //makes sure that the enemy units is not in the cannot attack range
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
                        } else if (gc.canSenseUnit(Efactories.get(i).id()) && Efactories.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Efactories.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Ehealers.get(i).id()) && Ehealers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Ehealers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Eworkers.get(i).id()) && Eworkers.get(i).team() != myTeam) {
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
            for (int i = 0; i < nearbyEnemiesA.size(); i++) {
                if (gc.canAttack(unit.id(), nearbyEnemiesA.get(i).id())) {
                    if (!Mrangers.get(i).location().mapLocation().isWithinRange(unit.rangerCannotAttackRange(), unit.location().mapLocation())) {
                        if (gc.canSenseUnit(Mrockets.get(i).id()) && Mrockets.get(i).team() != myTeam) { //don't know how to target recently landed rockets
                            gc.attack(unit.id(), Mrockets.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Mmages.get(i).id()) && Mmages.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mmages.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Mrangers.get(i).id()) && Mrangers.get(i).team() != myTeam) {
                            if (Mrangers.get(i).rangerIsSniping() == 1) {
                                gc.attack(unit.id(), Mrangers.get(i).id());
                                return;
                            } else {
                                gc.attack(unit.id(), Mrangers.get(i).id());
                                return;
                            }
                        } else if (gc.canSenseUnit(Mknights.get(i).id()) && Mknights.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mknights.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Mhealers.get(i).id()) && Mhealers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mhealers.get(i).id());
                            return;
                        } else if (gc.canSenseUnit(Mworkers.get(i).id()) && Mworkers.get(i).team() != myTeam) {
                            gc.attack(unit.id(), Mworkers.get(i).id());
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    public void move() {
        try {
            if (myTeam == Team.Blue) {
                nearbyEnemiesV = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 8, Team.Red);
            } else if (myTeam == Team.Red) {
                nearbyEnemiesV = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 8, Team.Blue);
            }
            vRangers = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 8, UnitType.Ranger);
            vKnights = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 8, UnitType.Knight);
            vMages = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 8, UnitType.Mage);
            for (int i = 0; i < nearbyEnemiesV.size(); i++) {
                if (gc.canSenseUnit(vRangers.get(i).id()) && vRangers.get(i).team() != myTeam) {
                    if (!unit.location().mapLocation().isWithinRange(vRangers.get(i).attackRange(), vRangers.get(i).location().mapLocation())) {
                        finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                                Player.GridEarth[vRangers.get(i).location().mapLocation().getY()][vRangers.get(i).location().mapLocation().getX()],
                                Player.GridEarth);
                        finder.findPath();
                        if (finder.bPathFound) {
                            finder.reconstructPath();
                            if (gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
                                gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
                            }
                        }
                        return;
                    } else if (unit.location().mapLocation().isWithinRange(vRangers.get(i).attackRange(), vRangers.get(i).location().mapLocation())) {
                        if (gc.canAttack(unit.id(), vRangers.get(i).id())) {
                            gc.attack(unit.id(), vRangers.get(i).id());
                        }
                        if (vRangers.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX() && vRangers.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                            // if your robot is in the '3rd quadrant' with respect to the the enemies attack range
                            if (gc.canMove(unit.id(), directions[5])) { //move southwest
                                gc.moveRobot(unit.id(), directions[5]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[4])) { //move south
                                gc.moveRobot(unit.id(), directions[4]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[6])) {  //move west
                                gc.moveRobot(unit.id(), directions[6]);
                                return;
                            }
                        } else if (vRangers.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX() && vRangers.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                            // if your robot is in the '2nd quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[7])) { //move Northwest
                                gc.moveRobot(unit.id(), directions[7]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[0])) { //move north
                                gc.moveRobot(unit.id(), directions[0]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[6])) {  //move west
                                gc.moveRobot(unit.id(), directions[6]);
                                return;
                            }
                        } else if (vRangers.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX() && vRangers.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                            // if your robot is in the '1st quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[1])) { //move Northeast
                                gc.moveRobot(unit.id(), directions[1]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[0])) { //move north
                                gc.moveRobot(unit.id(), directions[0]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[2])) {  //move east
                                gc.moveRobot(unit.id(), directions[2]);
                                return;
                            }
                        } else if (vRangers.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX() && vRangers.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                            // if your robot is in the '4th quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[3])) { //move Southeast
                                gc.moveRobot(unit.id(), directions[3]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[4])) { //move south
                                gc.moveRobot(unit.id(), directions[4]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[2])) {  //move east
                                gc.moveRobot(unit.id(), directions[2]);
                                return;
                            }
                        } else if (vRangers.get(i).location().mapLocation().getX() == unit.location().mapLocation().getX()) {
                            if (vRangers.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                                // is on the 270 degree line
                                if (gc.canMove(unit.id(), directions[4])) { //move south
                                    gc.moveRobot(unit.id(), directions[4]);
                                    return;
                                }
                            } else if (vRangers.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                                // is on the 90 degree line
                                if (gc.canMove(unit.id(), directions[0])) { //move north
                                    gc.moveRobot(unit.id(), directions[0]);
                                    return;
                                }
                            }
                        } else if (vRangers.get(i).location().mapLocation().getY() == unit.location().mapLocation().getY()) {
                            if (vRangers.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX()) {
                                // is on the 180 degree line
                                if (gc.canMove(unit.id(), directions[6])) { //move west
                                    gc.moveRobot(unit.id(), directions[6]);
                                    return;
                                }
                            } else if (vRangers.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX()) {
                                // is on the 360 degree line
                                if (gc.canMove(unit.id(), directions[2])) { //move east
                                    gc.moveRobot(unit.id(), directions[2]);
                                    return;
                                }

                            }
                        }

                    } else if (!unit.location().mapLocation().isWithinRange(vKnights.get(i).attackRange(), vKnights.get(i).location().mapLocation())) {
                        finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                                Player.GridEarth[vKnights.get(i).location().mapLocation().getY()][vKnights.get(i).location().mapLocation().getX()],
                                Player.GridEarth);
                        finder.findPath();
                        if (finder.bPathFound) {
                            finder.reconstructPath();
                            if (gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
                                gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
                            }
                        }
                        return;
                    } else if (unit.location().mapLocation().isWithinRange(vKnights.get(i).attackRange(), vKnights.get(i).location().mapLocation())) {
                        for (int j = 0; j < 8; j += 2) {
                            if (gc.canMove(unit.id(), directions[j])) { //since the knights only have attack range 1 they should move away from them
                                gc.moveRobot(unit.id(), directions[j]);
                            }
                        }
                    } else if (!unit.location().mapLocation().isWithinRange(vMages.get(i).attackRange(), vMages.get(i).location().mapLocation())) {
                        finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                                Player.GridEarth[vMages.get(i).location().mapLocation().getY()][vMages.get(i).location().mapLocation().getX()],
                                Player.GridEarth);
                        finder.findPath();
                        if (finder.bPathFound) {
                            finder.reconstructPath();
                            if (gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()))) {
                                gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.nextMove()));
                            }
                        }
                        return;
                    } else if (unit.location().mapLocation().isWithinRange(vMages.get(i).attackRange(), vMages.get(i).location().mapLocation())) {
                        if (vMages.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX() && vMages.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                            // if your robot is in the '3rd quadrant' with respect to the the enemies attack range
                            if (gc.canMove(unit.id(), directions[5])) { //move southwest
                                gc.moveRobot(unit.id(), directions[5]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[4])) { //move south
                                gc.moveRobot(unit.id(), directions[4]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[6])) {  //move west
                                gc.moveRobot(unit.id(), directions[6]);
                                return;
                            }
                        } else if (vMages.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX() && vMages.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                            // if your robot is in the '2nd quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[7])) { //move Northwest
                                gc.moveRobot(unit.id(), directions[7]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[0])) { //move north
                                gc.moveRobot(unit.id(), directions[0]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[6])) {  //move west
                                gc.moveRobot(unit.id(), directions[6]);
                                return;
                            }
                        } else if (vMages.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX() && vMages.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                            // if your robot is in the '1st quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[1])) { //move Northeast
                                gc.moveRobot(unit.id(), directions[1]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[0])) { //move north
                                gc.moveRobot(unit.id(), directions[0]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[2])) {  //move east
                                gc.moveRobot(unit.id(), directions[2]);
                                return;
                            }
                        } else if (vMages.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX() && vMages.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                            // if your robot is in the '4th quadrant' with respect to the enemies attack range
                            if (gc.canMove(unit.id(), directions[3])) { //move Southeast
                                gc.moveRobot(unit.id(), directions[3]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[4])) { //move south
                                gc.moveRobot(unit.id(), directions[4]);
                                return;
                            } else if (gc.canMove(unit.id(), directions[2])) {  //move east
                                gc.moveRobot(unit.id(), directions[2]);
                                return;
                            }
                        } else if (vMages.get(i).location().mapLocation().getX() == unit.location().mapLocation().getX()) {
                            if (vMages.get(i).location().mapLocation().getY() > unit.location().mapLocation().getY()) {
                                // is on the 270 degree line
                                if (gc.canMove(unit.id(), directions[4])) { //move south
                                    gc.moveRobot(unit.id(), directions[4]);
                                    return;
                                }
                            } else if (vMages.get(i).location().mapLocation().getY() < unit.location().mapLocation().getY()) {
                                // is on the 90 degree line
                                if (gc.canMove(unit.id(), directions[0])) { //move north
                                    gc.moveRobot(unit.id(), directions[0]);
                                    return;
                                }
                            }
                        } else if (vMages.get(i).location().mapLocation().getY() == unit.location().mapLocation().getY()) {
                            if (vMages.get(i).location().mapLocation().getX() > unit.location().mapLocation().getX()) {
                                // is on the 180 degree line
                                if (gc.canMove(unit.id(), directions[6])) { //move west
                                    gc.moveRobot(unit.id(), directions[6]);
                                    return;
                                }
                            } else if (vMages.get(i).location().mapLocation().getX() < unit.location().mapLocation().getX()) {
                                // is on the 360 degree line
                                if (gc.canMove(unit.id(), directions[2])) { //move east
                                    gc.moveRobot(unit.id(), directions[2]);
                                    return;
                                }

                            }
                        }
                    }
                }

            }
        } catch (Exception e) {

        }
    }
}
