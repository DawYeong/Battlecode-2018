// import the API.
// See xxx for the javadocs.

import bc.*;

import java.util.Random;

public class Player {
    static GameController gc = new GameController();
    static Team myTeam = gc.team();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting Abdullah BOT.");
        System.out.printf("My Team: %b \n", myTeam);

        Direction[] directions = Direction.values();

        Random random = new Random();

        Location _l;


        while (true) {
            System.out.println("Round # " + gc.round());
            System.out.println("My Karbonite " + gc.karbonite());
            VecUnit alUnits = gc.units();

            for (int i = 0; i < alUnits.size(); i++) {
                Unit unit = alUnits.get(i);

                if (unit.unitType() == UnitType.Factory) {
                    if (unit.structureGarrison().size() > 0) {
                        Direction d = directions[random.nextInt(directions.length)];

                        System.out.println("Factory Full.");

                        if (gc.canUnload(unit.id(), d)) {
                            gc.unload(unit.id(), d);
                            System.out.println("Unloaded a knight.");
                            continue;
                        }
                    } else if (gc.canProduceRobot(unit.id(), UnitType.Knight)) {
                        gc.produceRobot(unit.id(), UnitType.Factory);
                        System.out.println("Produced a knight.");
                        continue;
                    }
                }

                _l = unit.location();

                if (_l.isOnMap()) {
                    VecUnit nearby = gc.senseNearbyUnits(_l.mapLocation(), 2);
                    for (int x = 0; x < nearby.size(); x++) {
                        Unit other = nearby.get(x);

                        if (unit.unitType() == UnitType.Worker && gc.canBuild(unit.id(), other.id())) {
                            gc.build(unit.id(), other.id());
                            System.out.println("Built a factory.");
                            continue;
                        }
                        if (other.team() != myTeam && gc.isAttackReady(unit.id()) && gc.canAttack(unit.id(), other.id())) {
                            System.out.println("Attacked a thing.");
                            gc.attack(unit.id(), other.id());
                            continue;
                        }
                    }
                }

                Direction randDir = directions[random.nextInt(directions.length)];


                if (gc.karbonite() > bc.bcUnitTypeBlueprintCost(UnitType.Factory) &&
                        gc.canBlueprint(unit.id(), UnitType.Factory, randDir)) {
                    System.out.println("Created a factory.");
                    gc.blueprint(unit.id(), UnitType.Factory, randDir);
                } else if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), randDir)) {
                    gc.moveRobot(unit.id(), randDir);
                    System.out.println("Moved: " + randDir);
                }
            }

            gc.nextTurn();
            Thread.sleep(10);
        }
    }
}