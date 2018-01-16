import bc.Direction;
import bc.MapLocation;

import java.util.ArrayList;
import java.util.Collections;

public class Finder2 {

    // set of currently discovered nodes that are not evaluated yet
    private ArrayList<Cell> OpenList = new ArrayList<>();
    // set of nodes already evaluated
    private ArrayList<Cell> ClosedList = new ArrayList<>();
    // for each node, stores the node that it can most efficiently be reached from
    private Cell[][] cameFrom;
    // for each node, the cost of going from the start to that node
    private int[][] gScore;
    // for each node, the total cost of getting from the start node the goal
    private int[][] fScore;
    // the final path (if found)
    private ArrayList<Cell> Path = new ArrayList<>();

    private Direction[] directions;

    private Cell start, goal;
    private Cell[][] Grid;
    private int width, height;
    boolean bPathFound = false;

    public Finder2(Cell start, Cell goal, Cell[][] Grid) {
        this.start = start;
        this.goal = goal;
        this.Grid = Grid;

        height = Grid[0].length;
        width = Grid[1].length;

        init();
    }

    private void init() {
        cameFrom = new Cell[height][width];
        gScore = new int[height][width];
        fScore = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cameFrom[y][x] = null;
                gScore[y][x] = 99;
                fScore[y][x] = getDist(Grid[y][x], goal);

            }
        }

        gScore[start.getLocation().getY()][start.getLocation().getX()] = 0;
        fScore[start.getLocation().getY()][start.getLocation().getX()] = getDist(start, goal);
        OpenList.add(start);

        directions = Direction.values();

        start.setValue(-1);
        goal.setValue(-9);
        Player.printMap();

        System.out.println();
    }

    public void findPath() throws InterruptedException {
//        Thread.sleep(5000);

        int tempG;
        int nX, nY;
        MapLocation tempLocation;

        while (OpenList.size() != 0 && !bPathFound) {
//            Collections.sort(OpenList);

            try {

//                System.out.println("OpenList:");
//                for (int x = 0; x < OpenList.size(); x++) {
//                    Cell c = OpenList.get(x);
//                    System.out.println("x: " + c.getLocation().getX() + ", y: " + c.getLocation().getY() + ", nF: " + fScore[c.getLocation().getY()][c.getLocation().getX()]);
//                }
//                System.out.println();

                Cell current = OpenList.get(0);

                for (int x = 0; x < OpenList.size(); x++) {
                    Cell other = OpenList.get(x);
                    if (fScore[other.getLocation().getY()][other.getLocation().getX()] <= fScore[current.getLocation().getY()][current.getLocation().getX()]) {
                        current = other;
                    }
                }

                ClosedList.add(current);
                OpenList.remove(current);

                System.out.println("Current:");
                System.out.println("x: " + current.getLocation().getX() + ", y: " + current.getLocation().getY());

                if (current == goal) {
                    reconstruct_path(current);
                    bPathFound = true;
                    break;
                }

                for (int d = 0; d < directions.length - 1; d++) {
                    tempLocation = current.getLocation().add(directions[d]);
                    nX = tempLocation.getX();
                    nY = tempLocation.getY();

                    System.out.println("Direction:" + directions[d]);

                    Cell neighbour = Grid[nY][nX];

                    if (!neighbour.isPassable()) {
                        System.out.println("Impassable Neighbour.");
                        continue;
                    }

                    System.out.println(nX + " , " + nY);

                    if (ClosedList.contains(neighbour)) {
                        continue;
                    }

                    if (!OpenList.contains(neighbour)) {
                        OpenList.add(neighbour);
                        //neighbour.setValue(4);
                        System.out.println("Added a new neighbour.");
                    }

                    if (d % 2 == 0) {
                        tempG = gScore[current.getLocation().getY()][current.getLocation().getX()] + 2;
                    } else {
                        tempG = gScore[current.getLocation().getY()][current.getLocation().getX()] + 3;
                    }
                    if (tempG >= gScore[neighbour.getLocation().getY()][neighbour.getLocation().getX()]) {
                        continue;
                    }

                    cameFrom[nY][nX] = current;
                    gScore[nY][nX] = tempG;
                    fScore[nY][nX] = gScore[nY][nX] + getDist(neighbour, goal);

                }
//                Player.printMap();
//                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error Occured:" + e);
            }

        }
    }

    public int getDist(Cell c1, Cell c2) {
        int vertical, horizontal;

        vertical = Math.abs(c1.getLocation().getY() - c2.getLocation().getY());
        horizontal = Math.abs(c1.getLocation().getX() - c2.getLocation().getX());

        return (vertical + horizontal);
    }

    public void reconstruct_path(Cell current) throws InterruptedException {
        System.out.println("Building Path:");
        Path.add(current);
//        System.out.println("x: " + current.getLocation().getX() + ", y: " + current.getLocation().getY());
        Thread.sleep(5000);
        while (cameFrom[current.getLocation().getY()][current.getLocation().getX()] != null && current != start) {
//            System.out.println("( " + current.getLocation().getX() + " , " + current.getLocation().getY() + " )");
            current.setValue(1);
            current = cameFrom[current.getLocation().getY()][current.getLocation().getX()];
            Path.add(current);
        }
        Path.remove(goal);
        Path.remove(start);
        start.setValue(-1);
        goal.setValue(-9);
        Collections.reverse(Path);
    }

    public ArrayList<Cell> getPath() {
        return Path;
    }
}
