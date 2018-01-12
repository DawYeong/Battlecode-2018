import bc.*;
import java.util.*;
import java.io.*;


public class Player {
    public static GameController gc = new GameController();
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
    public static VecUnit units;
    public static ArrayList<Rocket> rockets = new ArrayList<>();
    public static ArrayList<Factory> factories = new ArrayList<>();
    public static ArrayList<Worker> workers = new ArrayList<>();
    public static ArrayList<Ranger> rangers = new ArrayList<>();
    public static ArrayList<Mage> mages = new ArrayList<>();
    public static ArrayList<Healer> healers = new ArrayList<>();
    public static ArrayList<Knight> knights = new ArrayList<>();


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
                            for(int j = 0; j < workers.size(); j++){
                                if(workers.get(j).unit.id()==unit.id()) {
                                    workers.get(j).run();
                                    break;
                                }
                            }
                            break;
                        case Rocket:
                            for(int j = 0; j < rockets.size(); j++){
                                if(rockets.get(j).unit.id()==unit.id()) {
                                    rockets.get(j).run();
                                    break;
                                }
                            }
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
