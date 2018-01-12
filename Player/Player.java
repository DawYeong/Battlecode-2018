import bc.*;
import java.util.*;
import java.io.*;


public class Player {
    public static GameController gc = new GameController();
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
    public static VecUnit units;
    public static ArrayList<Rocket> rockets;
    public static ArrayList<Factory> factories;
    public static ArrayList<Worker> workers;
    public static ArrayList<Ranger> rangers;
    public static ArrayList<Mage> mages;
    public static ArrayList<Healer> healers;


    public static void main(String[] args) {
        System.out.println(myTeam);
        gc.queueResearch(UnitType.Rocket);
        for (int i = 0; i < gc.myUnits().size(); i++) {
            workers.add(new Worker(gc.myUnits().get(i)));
        }
        while (true) {
            try {
                System.out.println("Current round: " + gc.round());
                units = gc.myUnits();

                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);
                    switch (unit.unitType()) {
                        case Worker: //We don't need UnitType.Worker since it is an enum
                            workers.get(i).run();
                            break;
                        case Rocket:
                            rockets.get(i).run();
                            break;
                    }
                    units = gc.myUnits();
                }
            } catch (Exception e) {
            }
            gc.nextTurn();
        }
    }
}
