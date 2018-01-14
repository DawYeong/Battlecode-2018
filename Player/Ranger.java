import bc.*;
import java.util.*;
import java.io.*;

public class Ranger {
    public Unit unit;//Use static if always going to be the same, public if we want them different for each instance
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    Ranger (Unit unit){
        this.unit = unit;
        init();
    }

    public void init(){

    }

    public void runEarth(){
        this.unit = Player.unit;//Need to update this every round

    }

    public void runMars(){
        this.unit = Player.unit;//Need to update this every round

    }
}
