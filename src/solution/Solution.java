/* 
 * referred to a DFS maze solver: https://12ft.io/proxy?q=https%3A%2F%2Fbytefish.medium.com%2Fuse-depth-first-search-algorithm-to-solve-a-maze-ae47758d48e7
 */
package solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import maze.MazeGenerator;

public class Solution {
    public ArrayList<ArrayList<Integer>> maze = MazeGenerator.mazeMatrix;
    private Map<ArrayList<Integer>, Integer> node = new HashMap<ArrayList<Integer>, Integer>(); // stored as location, color
    private Map<ArrayList<Integer>, String> state = new HashMap<ArrayList<Integer>, String>(); // stored as location; visited, processed
    private Map<ArrayList<Integer>, ArrayList<Integer>> predecessor = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); // stored as location, location
    private Stack<ArrayList<Integer>> stack = new Stack<>();
    ArrayList<ArrayList<Integer>> path = new ArrayList<>();
    
    // // Arraylist update () {} -- every change made is put in an arraylist to send to vis
    // // x y color
    // // update frame by frame and not all at once
    // // x andd y are ints, and multiple need to be returned at a time, as well s a color

    private ArrayList<Integer> findStart() {
        int colNum = 0;
        ArrayList<Integer> startLocation = new ArrayList<>();
        while (startLocation.isEmpty() && colNum < maze.size()) {
            if (maze.get(colNum).indexOf(2) != -1) { // start is in this column
                startLocation.add(colNum);
                startLocation.add(maze.get(colNum).indexOf(2));
            }
            colNum++;
        }
        return startLocation;
    }

    private ArrayList<Integer> findEnd() {
        int colNum = 0;
        ArrayList<Integer> endLocation = new ArrayList<>();
        while (endLocation.isEmpty() && colNum < maze.size()) {
            if (maze.get(colNum).indexOf(3) != -1) { // end is in this column
                endLocation.add(colNum);
                endLocation.add(maze.get(colNum).indexOf(3));
            }
            colNum++;
        }
        return endLocation;
    }

    int[][] dirList = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};    

    private void populateState() {
        for (int j = 0; j < maze.size(); j++) {
            for (int i = 0; i < maze.get(j).size(); i++) {
                if (maze.get(j).get(i) == 1 || maze.get(j).get(i) == 2 || maze.get(j).get(i) == 3) {
                    ArrayList<Integer> node = new ArrayList<>();
                    node.add(j);
                    node.add(i);
                    state.put(node, "undiscovered");
                }
            }
        }
    }

    private ArrayList<ArrayList<Integer>> findPath(ArrayList<Integer> location) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        ArrayList<Integer> current = location;
        while (!path.contains(findStart())) { 
            path.add(0, current);
            current = predecessor.get(current);
        }
        System.out.println("path found: " + path);
        return path;
    }

    private boolean isValidPath(ArrayList<Integer> location) {
        if (location.get(0) < 0 || location.get(0) >= maze.size()) {
            return false;
        }
        if (location.get(1) < 0 || location.get(1) >= maze.get(0).size()) {
            return false;
        }
        if (maze.get(location.get(0)).get(location.get(1)) == 3) {
            return true;
        }
        if (maze.get(location.get(0)).get(location.get(1)) != 1) {
            return false;
        }
        return true;
    }

    public void DFS() { 
        System.out.println("\nRUNNING NEW SOLUTION");
        path.clear();
        node.clear();
        state.clear();
        predecessor.clear();
        populateState();
        stack.push(findStart());
        boolean foundPath = false;
        while(!stack.empty()) {
            ArrayList<Integer> currentLocation = new ArrayList<>();
            currentLocation = stack.pop();
            state.put(currentLocation, "processed");
            node.put(currentLocation, maze.get(currentLocation.get(0)).get(currentLocation.get(1)));
            if(currentLocation.get(0) == findEnd().get(0) && currentLocation.get(1) == findEnd().get(1)) {
                System.out.println("maze solved");
                // findPath(currentLocation);
                ArrayList<Integer> current = currentLocation;
                while (!path.contains(findStart())) { 
                    path.add(0, current);
                    if (maze.get(current.get(0)).get(current.get(1)) == 1) {
                        update(current);
                    }
                    current = predecessor.get(current);
                }
                System.out.println("path found: " + path);
                foundPath = true;
            }
            for (int[] direction : dirList) {
                int newX = currentLocation.get(1) + direction[0];
                int newY = currentLocation.get(0) + direction[1];
                ArrayList<Integer> tempPath = new ArrayList<>();
                tempPath.add(newY);
                tempPath.add(newX);
                if(isValidPath(tempPath) && state.get(tempPath).compareTo("undiscovered") == 0) {
                    stack.push(tempPath);
                    state.put(tempPath, "visited");
                    predecessor.put(tempPath, currentLocation);
                    if (maze.get(tempPath.get(0)).get(tempPath.get(1)) == 1) {
                        update(tempPath);
                    }
                }
            }
            System.out.println("stack " + stack + "\nnodes " + node + "\nstates " + state + "\npreds " + predecessor);
            System.out.println("done?: " + foundPath + "\n----------");
        }
        if (!foundPath) {
            System.out.println("Failed to find path");
        }
    }

    public ArrayList<Integer> update(ArrayList<Integer> location){
        ArrayList<Integer> newColor = new ArrayList<>();
        if(path.isEmpty() || !path.contains(location)) {
            newColor.add(location.get(0));
            newColor.add(location.get(1));
            newColor.add(4);
        } else {
            newColor.add(location.get(0));
            newColor.add(location.get(1));
            newColor.add(5);
        }
        System.out.println("change " + newColor);
        return newColor; // x y color
    }


}
