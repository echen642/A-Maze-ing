package maze;

import java.util.ArrayList;
import java.util.Random;

/*
 * Sets up the maze grid, generates the maze, and represents 
 * the maze as an array of an array of integers.
 */
public class MazeGenerator {
    public static ArrayList<ArrayList<Integer>> mazeMatrix;
    private int grid_size_x;
    private int grid_size_y;
    private Random randomGenerator;
    private ArrayList<Integer> visualPathArray;     // An array that carries triplets of integers at a time
    private ArrayList<Point> potentialPoints;       // Array of points that could be a potential path
    private ArrayList<Point> neighborPoints;        // Array of neighboring path points for the chosen point

    public MazeGenerator() {
        mazeMatrix = new ArrayList<ArrayList<Integer>>();
        randomGenerator = new Random();
        potentialPoints = new ArrayList<Point>();
        visualPathArray = new ArrayList<Integer>();
        neighborPoints = new ArrayList<Point>();
    }

    /**
     * Create a matrix of size x and y. All values set to zero at first because everything is a wall.
     * The matrix will have the coordinate system (x, y)
     * 
     * @param x
     * @param y
     * @return visualPathArray
     */
    public ArrayList<Integer> setMazeSize(int x, int y) {
        mazeMatrix.clear();
        visualPathArray.clear();
        potentialPoints.clear();
        neighborPoints.clear();
        
        grid_size_x = x;
        grid_size_y = y;
        for (int xCoord = 0; xCoord < x; xCoord++){
            mazeMatrix.add(new ArrayList<Integer>());
            for (int yCoord = 0; yCoord < y; yCoord++) {
                mazeMatrix.get(xCoord).add(0);
            }
        }

        // Update Starting Block
        setMatrix(new Point(0, 0), 2);
        addToVisual(new Point(0, 0), 2);
        addToPotentialPoints(new Point(1, 0));
        addToPotentialPoints(new Point(0, 1));

        // Update Ending Block 
        setMatrix(new Point(grid_size_x - 1, grid_size_y - 1), 3);
        addToVisual(new Point(grid_size_x - 1, grid_size_y - 1), 3);

        return visualPathArray;
    }

    /**
     * Travel down a potential path and provides block update for Window class 
     * 
     * The resulting visualPathArray will contain triplets:
     * The first value in the triplet is a x-value;
     * The second value is the triplet is a y-value;
     * The third value is the triplet is a Color Code.
     * 
     * @return visualPathArray
     */
    public ArrayList<Integer> update() {
        visualPathArray.clear();
        neighborPoints.clear();
        
        if (potentialPoints.size() == 0) {
            Point endPoint = new Point(grid_size_x - 1, grid_size_y - 1);
            Point endPointLeft = new Point(endPoint.getX() - 1, endPoint.getY());
            Point endPointUp = new Point(endPoint.getX(), endPoint.getY() - 1);

            if (getValueAt(endPointLeft) == 0 && getValueAt(endPointUp) == 0) {
                ArrayList<Point> endings = new ArrayList<Point>();
                endings.add(endPointLeft);
                endings.add(endPointUp);
    
                Point chosenEnding = endings.get(randomGenerator.nextInt(endings.size()));
                setMatrix(chosenEnding, 1);
                addToVisual(chosenEnding, 1);
            }

            return visualPathArray;
        }

        // Potential paths are 1 block away from a current block, should be in-bounds, and are walls.
        Point chosenPoint = potentialPoints.remove(randomGenerator.nextInt(potentialPoints.size()));       // random.nextInt()'s bound is exclusive        

        // Find the valid neighborPoints of chosenPoint
        for (Point p : generateSurroundingPoints(chosenPoint)) {
            addToNeighborPoints(p);
        }

        if (neighborPoints.size() == 2) {
            setMatrix(chosenPoint, 0);          // Set the chosenPoint to a wall because it was connected to 2+ paths
            addToVisual(chosenPoint, 0);
        } else {
            setMatrix(chosenPoint, 1);          // Set the chosenPoint to a path
            addToVisual(chosenPoint, 1);

            // Calculating where the next path will be placed based on previous path's direction.
            int nextPointX = 2*(chosenPoint.getX()) - neighborPoints.get(0).getX();
            int nextPointY = 2*(chosenPoint.getY()) - neighborPoints.get(0).getY();
            Point nextPoint = new Point(nextPointX, nextPointY);

            setMatrix(nextPoint, 1);
            addToVisual(nextPoint, 1);

            // Find the neighbor points for nextPoints
            for (Point p : generateSurroundingPoints(nextPoint)) {
                addToPotentialPoints(p);
            }
        }

        return visualPathArray;
    }

    /**
     * Set the colorCode for the current block at this point and add it to the visual
     * 
     * @param point
     * @param colorCode
     */
    private void addToVisual(Point point, int colorCode) {
        visualPathArray.add(point.getX());
        visualPathArray.add(point.getY());
        visualPathArray.add(colorCode);
    }

    /**
     * Set the colorCode for a block at the given point
     * 
     * @param point
     * @param colorCode
     */
    private void setMatrix(Point point, int colorCode) {
        // Add this potential point as a certain type of block in the mazeMatrix
        mazeMatrix.get(point.getX()).set(point.getY(), colorCode);
    }

    /**
     * Add to Potential Points List if the block is in bounds AND if at this point is a wall.
     * 
     * @param point
     */
    private void addToPotentialPoints(Point point) {
        if (checkBounds(point)) {
            if (getValueAt(point) == 0) {
                potentialPoints.add(point);
                setMatrix(point, 4);
                addToVisual(point, 4);
            }    
        }
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
     * If the given point is in-bounds AND the point is a path, start, or end
     * then add it as a neighbor to the currently chosen point
     * 
     * @param point
     */
    private void addToNeighborPoints(Point point) {
        if (checkBounds(point)){
            if (getValueAt(point) == 1 || getValueAt(point) == 2 || getValueAt(point) == 3 ) 
            {
                neighborPoints.add(point);
            }    
        }
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
     * @return surroundingPoints
     */
    private ArrayList<Point> generateSurroundingPoints(Point point) {
        Point surRightPoint = new Point(point.getX() + 1, point.getY());
        Point surLeftPoint = new Point(point.getX() - 1, point.getY());
        Point surBottomPoint = new Point(point.getX(), point.getY() + 1);
        Point surTopPoint = new Point(point.getX(), point.getY() - 1);

        ArrayList<Point> surroundingPoints = new ArrayList<Point>();
        surroundingPoints.add(surRightPoint);
        surroundingPoints.add(surLeftPoint);
        surroundingPoints.add(surBottomPoint);
        surroundingPoints.add(surTopPoint);

        return surroundingPoints;
    }
}
