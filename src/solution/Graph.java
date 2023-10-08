package solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;


// creating a graph class for ease of traversal
public class Graph {
    protected int nodeColor;
    protected ArrayList<Integer> nodeLocation;
    protected Graph firstBranch;
    protected Graph secondBranch;
    protected Graph thirdBranch;

    // create a node subclass that has 3 children
    // 1st branch, 2nd branch, 3rd branch, parent -- holds all possible paths and keeps links
    public Graph(int color, ArrayList<Integer> location){
        nodeColor = color; 
        nodeLocation = location;
        firstBranch = null;
        secondBranch = null;
        thirdBranch = null;
    }

    public boolean hasFirstBranch() {
        return firstBranch != null;
    }

    public boolean hasSecondBranch() {
        return secondBranch != null;
    }

    public boolean hasThirdBranch() {
        return thirdBranch != null;
    }

    public boolean isLeaf() {
        return (firstBranch == null) && (secondBranch == null) && (thirdBranch == null);
    }

    public void addNode(int color, ArrayList<Integer> location) {
        if (!hasFirstBranch()) {
            firstBranch = new Graph(color, location);
        } else if (!hasSecondBranch()) {
            secondBranch = new Graph(color, location);
        } else {
            thirdBranch = new Graph(color, location);
        }
    }

    /*
     * Takes in an indentation depth, and prints out the tree
     */
    protected void printRecur(int depth) {
        char indentSpaces[] = new char[depth];
        for (int i = 0; i < depth; i++) {
            indentSpaces[i] = ' ';
        }
        String indStr = new String(indentSpaces);
        
        if (isLeaf()) {
            System.out.println(indStr + "Leaf: " + nodeLocation);
        } else {
            System.out.println(indStr + "Node: " + nodeLocation);
            if (hasFirstBranch()) {
            System.out.println(indStr + "     FIRST:");
            firstBranch.printRecur(depth + 5);
            } else {
            System.out.println(indStr + "     FIRST:  no first path");
            }
            if (hasSecondBranch()) {
            System.out.println(indStr + "     SECOND:");
            secondBranch.printRecur(depth + 5);
            } else {
            System.out.println(indStr + "     SECOND:  no second path");
            }
            if (hasThirdBranch()) {
                System.out.println(indStr + "     THIRD:");
                thirdBranch.printRecur(depth + 5);
            } else {
            System.out.println(indStr + "     THIRD:  no third path");
            }
        }
    }

    public static void main(String[] args) {
        /* 
         * given the maze from mazegen
         * create a directed graph showing all possible routes
         * perform searches on this graph, prioritizing depth first search
         * [2 1 1 1 1][1 0 1 0 1][1 0 1 0 1][1 0 1 0 0][1 0 1 1 3]
         */
        ArrayList<ArrayList<String>> testMaze = new ArrayList<>();
        ArrayList<String> testCols;
        testCols = new ArrayList<>(List.of("2","1a","1b","1c","1d"));
        testMaze.add(testCols);
        testCols = new ArrayList<>(List.of("1e","0","1f","0","1g"));
        testMaze.add(testCols);
        testCols = new ArrayList<>(List.of("1h","0","1i","0","1j"));
        testMaze.add(testCols);
        testCols = new ArrayList<>(List.of("1k","0","1l","0","0"));
        testMaze.add(testCols);
        testCols = new ArrayList<>(List.of("1m","0","1n","1o","3"));
        testMaze.add(testCols);
        
        for (int colIndex = 0; colIndex < testMaze.size(); colIndex++) {
            String row = "";
            for (int rowIndex = 0; rowIndex < testMaze.get(colIndex).size(); rowIndex++) {
                // System.out.println("point " + rowIndex + " " + colIndex + " has color " + testMaze.get(colIndex).get(rowIndex));
                row += "\t" + testMaze.get(rowIndex).get(colIndex);
            }
            System.out.println(row);
        }

        // finding the maze's start
        int colNum = 0;
        ArrayList<Integer> startLocation = new ArrayList<>();
        while (startLocation.isEmpty() && colNum < testMaze.size()) {
            if (testMaze.get(colNum).indexOf("2") != -1) { // start is in this column
                startLocation.add(colNum);
                startLocation.add(testMaze.get(colNum).indexOf("2"));
            }
            colNum++;
        }

        // finding the maze's exit
        colNum = 0;
        ArrayList<Integer> endLocation = new ArrayList<>();
        while (endLocation.isEmpty() && colNum < testMaze.size()) {
            if (testMaze.get(colNum).indexOf("3") != -1) { // end is in this column
                endLocation.add(colNum);
                endLocation.add(testMaze.get(colNum).indexOf("3"));
            }
            colNum++;
        }
        
        // counting number of 1s in maze
        int count1s = 0; 
        for (int j = 0; j < testMaze.size(); j++) {
            for (int i = 0; i < testMaze.get(j).size(); i++) {
                if (testMaze.get(j).get(i).contains("1")) {
                    count1s++;
                }
            }
        }

        ArrayList<Integer> currLoc = startLocation;
        Stack<ArrayList<Integer>> traversal = new Stack<>();
        // ArrayList<Integer> nextLoc = new ArrayList<>();
        
        System.out.println(startLocation);
        traversal.add(startLocation);
        int stop = 0;
        while (currLoc != endLocation & stop < 20) {
            // need to add to stack
            if (currLoc.get(0) != testMaze.size() - 1 && testMaze.get(currLoc.get(0) + 1).get(currLoc.get(1)).contains("1")) {
                System.out.println("right path\t" + testMaze.get(currLoc.get(0) + 1).get(currLoc.get(1)));
                // nextLoc = currLoc;
                currLoc.set(0, currLoc.get(0) + 1);
                currLoc.set(1, currLoc.get(1));
                traversal.add(currLoc);
                System.out.println("new currLoc is " + currLoc + "\n--------------");
            }
            else if (currLoc.get(0) != testMaze.get(0).size() && testMaze.get(currLoc.get(0)).get(currLoc.get(1) + 1).contains("1")) {
                System.out.println("down path\t" + testMaze.get(currLoc.get(0)).get(currLoc.get(1) + 1));
                // nextLoc = currLoc;
                currLoc.set(0, currLoc.get(0));
                currLoc.set(1, currLoc.get(1) + 1);
                traversal.add(currLoc);
                System.out.println("new currLoc is " + currLoc + "\n--------------");
            }
            else if (currLoc.get(0) != 0 && testMaze.get(currLoc.get(0) - 1).get(currLoc.get(1)).contains("1")) {
                System.out.println("left path\t" + testMaze.get(currLoc.get(0) - 1).get(currLoc.get(1)));
                // nextLoc = currLoc;
                currLoc.set(0, currLoc.get(0) - 1);
                currLoc.set(1, currLoc.get(1));
                traversal.add(currLoc);
                System.out.println("new currLoc is " + currLoc + "\n--------------");
            }
            else if (currLoc.get(1) != 0 && testMaze.get(currLoc.get(0)).get(currLoc.get(1) - 1).contains("1")) {
                System.out.println("up path\t" + testMaze.get(currLoc.get(0)).get(currLoc.get(1) - 1));
                // nextLoc = currLoc;
                currLoc.set(0, currLoc.get(0));
                currLoc.set(1, currLoc.get(1) - 1);
                traversal.add(currLoc);
                System.out.println("new currLoc is " + currLoc + "\n--------------");
            }
            stop++;
        }
        
    }
}
