import bc.*;
import java.util.*;
import java.io.*;

public class Factory {
    public static Unit unit;
    public static GameController gc = Player.gc;
    public static Direction[] directions = Player.directions;
    Factory(Unit unit){
        this.unit = unit;
        init();
    }

    public static void init(){

    }

    public static void run(){

    }
}
