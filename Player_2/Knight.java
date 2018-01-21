import bc.*;
import java.util.*;
import java.io.*;

public class Knight {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit nearbyEnemies, startingUnits;
    public static PlanetMap EarthMap = gc.startingMap(Planet.Earth);
    public Finder finder;
    Knight (Unit unit){
        this.unit = unit;
        init();
    }

    public void init(){

    }

    public void runEarth(){
        this.unit = Player.unit;//Need to update this every round
        try {
            if(unit.team() == Team.Blue) {
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Red);
            } else if (unit.team() == Team.Red) {
                nearbyEnemies = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, Team.Blue);
            }
            attack();
            move();
        } catch (Exception e) {

        }

    }

    public void runMars(){
        this.unit = Player.unit;//Need to update this every round

    }

    public void attack() {
        try {
            for(int i = 0; i < nearbyEnemies.size(); i++) {
                if(nearbyEnemies.get(i).location().mapLocation().isWithinRange(unit.attackRange(), unit.location().mapLocation())) {
                    if (nearbyEnemies.get(i).damage() > 0) {
                        if (gc.canAttack(unit.id(), nearbyEnemies.get(i).id())) { //attacks nearby enemies that do damage
                            gc.attack(unit.id(), nearbyEnemies.get(i).id());
                            return;
                        }
                    } else {
                        if (gc.canAttack(unit.id(), nearbyEnemies.get(i).id())) { //attacks units that don't do damage
                            gc.attack(unit.id(), nearbyEnemies.get(i).id());
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
            startingUnits = EarthMap.getInitial_units();
            if(nearbyEnemies.size() == 0) { //just to b-line to the enemy, then once they spot a nearby enemy that can path find
                if(gc.canMove(unit.id(), unit.location().mapLocation().directionTo(startingUnits.get(0).location().mapLocation()))) {
                    gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(startingUnits.get(0).location().mapLocation()));
                    return;
                }
            } else {
                for(int i = 0; i < nearbyEnemies.size(); i++) {
                    if(nearbyEnemies.get(i).damage() > 0) {
                        finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                                Player.GridEarth[nearbyEnemies.get(i).location().mapLocation().getY()][nearbyEnemies.get(i).location().mapLocation().getX()],
                                Player.GridEarth);
                        finder.findPath();
                        if(finder.bPathFound && finder.getPath().size() > 0) {
                            if(gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()))) {
                                gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()));
                                return;
                            }
                        }
                    } else {
                        finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                                Player.GridEarth[nearbyEnemies.get(i).location().mapLocation().getY()][nearbyEnemies.get(i).location().mapLocation().getX()],
                                Player.GridEarth);
                        finder.findPath();
                        if(finder.bPathFound && finder.getPath().size() > 0) {
                            if(gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()))) {
                                gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()));
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
