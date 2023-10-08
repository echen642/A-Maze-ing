package solution;

import java.util.ArrayList;
import java.util.Arrays;

import maze.MazeGenerator;
import maze.Point;

/**
 * Solves the maze
 */
public class DFS {
    private ArrayList<Point> currStack;
    private ArrayList<Point> visited;
    private ArrayList<ArrayList<Integer>> mazeMatrix;
    private int grid_size_x;
    private int grid_size_y;

    public DFS() {
        currStack = new ArrayList<Point>();
        visited = new ArrayList<Point>();
        mazeMatrix = MazeGenerator.mazeMatrix;
    }

    /**
     * Travels down path if they are valid surrounding points. 
     * Returns what Window class should need to update the visual 
     * i.e triplets of x-coord, y-coord, and colorCode.
     */
    public ArrayList<Integer> update() {
        if (currStack.isEmpty()) return new ArrayList<Integer>();
        Point chosenPoint = currStack.get(currStack.size() - 1);

        ArrayList<Point> surroundingPoints = generateSurroundingPoints(chosenPoint);
        if (surroundingPoints.isEmpty()) {
            visited.add(chosenPoint);
            currStack.remove(chosenPoint);
            return new ArrayList<Integer>(Arrays.asList(chosenPoint.getX(), chosenPoint.getY(), 1));
        } else {
            Point selectedPoint = surroundingPoints.get(0);
            if (getValueAt(selectedPoint) == 3) {
                return new ArrayList<Integer>();
            } else {
                currStack.add(selectedPoint);
                return new ArrayList<Integer>(Arrays.asList(selectedPoint.getX(), selectedPoint.getY(), 5));
            }
        }

    }

    /**
     * Resets the variables and solution paths.
     * 
     * @param x size of grid
     * @param y size of grid
     * 
     * Returns all points to reset from the last solution.
     */
    public ArrayList<Integer> reset(int x, int y) {
        // Convert Currstack to list of points
        int path = 1;
        grid_size_x = x;
        grid_size_y = y;
        ArrayList<Integer> changestovisual = new ArrayList<>();
        currStack.forEach(point -> {
            changestovisual.add(point.getX());
            changestovisual.add(point.getY());
            changestovisual.add(path);
        });
        currStack.clear();
        currStack.add(new Point(0, 0));
        visited.clear();
        return changestovisual;
    }

    /**
     * Checks to see whether a point is within the bounds of the grid
     * 
     * @param point
     * @return
     */
    private boolean checkBounds(Point point) {
        if ((point.getX() < grid_size_x) && (point.getX() >= 0)) {
            if ((point.getY() < grid_size_y) && (point.getY() >= 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates 4 points immediately surrounding a given point:
     * the right point,
     * the left point,
     * the bottom point,
     * and the top point 
     * 
     * @param point
     * @return verifiedPoints
     */
    private ArrayList<Point> generateSurroundingPoints(Point point) {
        Point surRightPoint = new Point(point.getX() + 1, point.getY());
        Point surLeftPoint = new Point(point.getX() - 1, point.getY());
        Point surBottomPoint = new Point(point.getX(), point.getY() + 1);
        Point surTopPoint = new Point(point.getX(), point.getY() - 1);

        // To be a surrounding point, the point can't be in visited, can't be in currStack, has to be a path OR end

        ArrayList<Point> surroundingPoints = new ArrayList<Point>();

        surroundingPoints.add(surRightPoint);
        surroundingPoints.add(surLeftPoint);
        surroundingPoints.add(surBottomPoint);
        surroundingPoints.add(surTopPoint);

        ArrayList<Point> verifiedPoints = new ArrayList<>();

        // Only add to verfied points if valid neighbor
        surroundingPoints.forEach(surPoint -> {
            if (checkBounds(surPoint) && (getValueAt(surPoint) == 1 || getValueAt(surPoint) == 3) && !contains(currStack, surPoint) && !contains(visited, surPoint)) verifiedPoints.add(surPoint);
        });

        return verifiedPoints;
    }


    /**
     * Get the colorCode for the block at this point.
     * 
     * @param point
     * @return colorCode
     */
    private int getValueAt(Point point) {
        return mazeMatrix.get(point.getX()).get(point.getY());
    }

    /**
     * Checks to see if a point with the same x-coord and y-coord is in
     * the ArrayList of points.
     * @param points
     * @param p
     * @return boolean
     */
    private boolean contains(ArrayList<Point> points, Point p) {
        for (Point p1 : points) {
            if (p1.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
