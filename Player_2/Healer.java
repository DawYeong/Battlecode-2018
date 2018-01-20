import bc.*;

import java.util.*;
import java.io.*;

public class Healer {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    public static VecUnit ownteam;
    public Finder finder;

    Healer(Unit unit) {
        this.unit = unit;
        init();
    }

    public void init() {

    }

    public void runEarth() {
        this.unit = Player.unit;//Need to update this every round
        try {
            ownteam = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), 7, unit.team()); // within vision range
            moveToLowestHealth();
            healing();
        } catch (Exception e) {

        }

    }

    public void runMars() {
        this.unit = Player.unit;//Need to update this every round

    }

    public void moveToLowestHealth() {
        try {
            long lowestHealthV = 250; // V represents vision range
            for(int i = 0; i < ownteam.size(); i++) {
                if(ownteam.get(i).location().mapLocation().isWithinRange(unit.visionRange(), unit.location().mapLocation())) {
                    if(ownteam.get(i).health() < lowestHealthV) {
                        lowestHealthV = ownteam.get(i).health();
                    }
                }
            }
            for(int i = 0; i <ownteam.size(); i++) {
                if(lowestHealthV == ownteam.get(i).health()) {
                    finder = new Finder(Player.GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()],
                            Player.GridEarth[ownteam.get(i).location().mapLocation().getY()][ownteam.get(i).location().mapLocation().getX()],
                            Player.GridEarth);
                    finder.findPath();
                    if(finder.bPathFound && finder.getPath().size() > 0) {
                        if(gc.canMove(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()))) {
                            gc.moveRobot(unit.id(), unit.location().mapLocation().directionTo(finder.getPath().get(0).getLocation()));
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void healing() {
        try {
            long lowestHealthA = 250; // maxhealth of knights who are the tankiest robot, A represents attack range
            for (int i = 0; i < ownteam.size(); i++) {
                if(ownteam.get(i).location().mapLocation().isWithinRange(unit.attackRange(), unit.location().mapLocation())) {
                    if(ownteam.get(i).health() < lowestHealthA) {
                        lowestHealthA = ownteam.get(i).health();
                    }
                }
            }
            for (int i = 0; i < ownteam.size(); i++) {
                if (ownteam.get(i).health() == lowestHealthA) {
                    if (ownteam.get(i).location().mapLocation().isWithinRange(unit.attackRange(), unit.location().mapLocation())) {
                        if (gc.canHeal(unit.id(), ownteam.get(i).id())) {
                            gc.heal(unit.id(), ownteam.get(i).id());
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
