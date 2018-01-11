import bc.*;

public class Player {
    static GameController gc = new GameController();
    static Team myTeam = gc.team();
    public static void main(String[] args) {
        MapLocation loc = new MapLocation(Planet.Earth, 10, 20);
        Direction[] directions = Direction.values();
        boolean builtRocket = false;
        System.out.println(myTeam);
        gc.queueResearch(UnitType.Rocket);
        while (true) {
            System.out.println("Current round: "+gc.round());
            VecUnit units = gc.myUnits();
            for (int i = 0; i < units.size(); i++) {
                Unit unit = units.get(i);
//                if(gc.canBlueprint(unit.id(), UnitType.Rocket, directions[0]) && !builtRocket){
//                    gc.blueprint(unit.id(), UnitType.Rocket, directions[0]);
//                    builtRocket = true;
//                } else if(gc.canBuild(unit.id(), UnitType.Rocket) && builtRocket){
//                    gc.build(unit.id(), BLUEPRINT_ID);//need to find that
//                }
                if(unit.unitType()==UnitType.Worker){
                    for(int j = 0; j < 8; j++){
                        if(gc.canBlueprint(unit.id(), UnitType.Factory, directions[j])){
                            gc.blueprint(unit.id(), UnitType.Factory, directions[j]);
                            continue;
                        }
                        if(gc.canReplicate(unit.id(), directions[j])){
                            gc.replicate(unit.id(), directions[j]);
                            continue;
                        }
                        if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), directions[j])) {
                            gc.moveRobot(unit.id(), directions[j]);
                        }
                    }
                }
                units = gc.myUnits();
            }
            gc.nextTurn();
        }
    }
}