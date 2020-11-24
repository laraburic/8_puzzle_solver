package ss;

import java.util.ArrayList; //store moves etc.
import java.util.Comparator; //to sort by f_n
import java.util.PriorityQueue; //to speed up search

/*
   Solver is a class that contains the methods used to search for and print solutions
   plus the data structures needed for the search.
 */

public class Solver {

    static final int SIZE = 3; // 8 puzzle has a default size of 3, initialised to avoid redundancy
    static final int ERROR = -1;  // cannot find solution
    static final int FINISHED = 0;  // found goal state
    //algorithm selection:
    static final int UNIFORM_COST_SEARCH = 1;
    static final int A_STAR_MANHATTAN = 2;

    public static PriorityQueue<State> unexpanded = new PriorityQueue<>(Comparator.comparing(State::getF_n));  // Sort moves by f_n value, results in faster search
    private static ArrayList<State> expanded = new ArrayList<>();  // previously explored states
    //private static ArrayList<State> unexpanded = new ArrayList<>(); // holds unexpanded nodes

    //holds values for initial and goal board in a 2d array:
    int[][] INITIAL_BOARD =   {{8, 7, 6},
            {5, 4, 3},
            {2, 1, 0}};

    int[][] GOAL_BOARD = {{1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}};

    static int nodesExpanded = 0;
    static int maxQueueSize = 1; //priority queue

    //general breadth first search function*
    //       The method searches for a solution. It implements a breadth first search.
    //       The problem asks for a solution with the minimum number of moves.
    //       Breadth first search is both complete and optimal with respect to number of moves.
    //       The Printwriter argument is used to specify where the output should be directed.

    int generalSearch(int[][] currentGrid, int searchFunction) { //was supposed to implement PrintWriter
        unexpanded.add(new State(currentGrid,0,0)); // Initialise the unexpanded node list
        expanded.add(new State(currentGrid,0,0)); // Initialise the expanded node list
        while(true) {
            if(unexpanded.isEmpty()) return ERROR;
            ArrayList<State> children; //hold child nodes
            State tempState = unexpanded.peek();
            int[][] tempNode = tempState.getGrid(); //grid of temporary node
            int [][] topNode = new int[currentGrid.length][];  // clone top node:
            for(int i = 0; i < currentGrid.length; i++)
                topNode[i] = tempNode[i].clone();
            State topState = new State(topNode,tempState.getG_n(),tempState.getH_n()); //get number of moves
            System.out.println("Number of moves: " + topState.getG_n()); //print moves
            printState(topState.getGrid());
            unexpanded.remove();
            if(stateEqual(topState.getGrid(), GOAL_BOARD)) { // If the node represents goal state then
                //reportSolution(topState.getG_n(), output); // write solution to a file
                return FINISHED;
            } else {  // keep expanding
                children = getChild(topState, searchFunction);
                if(children.size()==0) {
                    continue;  // go to next node in queue if topNode has no descendants
                }
                nodesExpanded++; //increment number of expanded nodes
                for(State child : children) {
                    if(!containsChild(child)) {  // if unique
                        unexpanded.add(child);
                        expanded.add(child);
                        if(unexpanded.size() > maxQueueSize) maxQueueSize = unexpanded.size();
                        printState(child.getGrid()); //add child to unexpanded nodes
                    } else {
                        //output.println("No solution found");
                        //unexpanded.add(child);
//                        System.out.println("Already explored");
//                        printState(child.getGrid());
                    }
                }
            }
        }

    }

    /* Finds child nodes
     * returns children in move 0 left, move 0 right, move 0 up and move 0 down order */
    private ArrayList<State> getChild(State currentState, int searchFunction) {
        ArrayList<State> moveArray = new ArrayList<>();
        int[][] currentGrid = currentState.getGrid();
        int g_n = currentState.getG_n();
        int x = -1;
        int y = -1;
        for(int i = 0; i< SIZE; i++) {
            for(int j = 0; j< SIZE; j++) {
                if(currentGrid[i][j]==0) {
                    x = i;
                    y = j;
                }
            }
        }
        //search functions:
        int h_n = 0;
        switch(searchFunction) {
            case UNIFORM_COST_SEARCH:
                h_n = 0;  // Uniform cost search has no heuristic value
                break;
            case A_STAR_MANHATTAN:
                for(int i = 0; i< SIZE; i++)
                    for(int j = 0; j< SIZE; j++) {
                        int target = currentGrid[i][j];
                        for (int k = 0; k < SIZE; k++)
                            for (int l = 0; l < SIZE; l++) {
                                if(GOAL_BOARD[k][l]==target) { //once our target matches the goal state
                                    h_n += Math.abs(k-i) + Math.abs(l-j);
                                }
                            }
                    }
                break;
            default:
                System.err.println("Error: Entered wrong algorithm!");
                break;
        }

        //calculation of possible moves:
        if(x-1 >=0) {  // Can move up
            // Cloning
            int [][] workingGrid = new int[currentGrid.length][];
            for(int i = 0; i < currentGrid.length; i++)
                workingGrid[i] = currentGrid[i].clone();
            int temp = workingGrid[x][y];
            workingGrid[x][y] = workingGrid[x-1][y];
            workingGrid[x-1][y] = temp;
            moveArray.add(new State(workingGrid,g_n+1,h_n));
        }
        if(x+1 < SIZE) {  // Can move down
            // Cloning
            int [][] workingGrid = new int[currentGrid.length][];
            for(int i = 0; i < currentGrid.length; i++)
                workingGrid[i] = currentGrid[i].clone();

            int temp = workingGrid[x][y];
            workingGrid[x][y] = workingGrid[x+1][y];
            workingGrid[x+1][y] = temp;
            moveArray.add(new State(workingGrid,g_n+1,h_n));
        }

        if(y+1 < SIZE) {  // Can move right
            int [][] workingGrid = new int[currentGrid.length][];
            for(int i = 0; i < currentGrid.length; i++)
                workingGrid[i] = currentGrid[i].clone();

            int temp = workingGrid[x][y];
            workingGrid[x][y] = workingGrid[x][y+1];
            workingGrid[x][y+1] = temp;
            moveArray.add(new State(workingGrid,g_n+1,h_n));
        }

        if(y-1 >=0) {  // Can move left
            int [][] workingGrid = new int[currentGrid.length][];
            for(int i = 0; i < currentGrid.length; i++)
                workingGrid[i] = currentGrid[i].clone();

            int temp = workingGrid[x][y];
            workingGrid[x][y] = workingGrid[x][y-1];
            workingGrid[x][y-1] = temp;
            moveArray.add(new State(workingGrid,g_n+1,h_n));
        }
        return moveArray;
    }

    /*
       reportSolution prints the solution together with statistics on the number of moves
       and the number of expanded and unexpanded nodes.
       The Node argument n should be a node representing the goal state.
       The Printwriter argument is used to specify where the output should be directed.
     */
    /*public void reportSolution(g_n, PrintWriter output) {
        output.println("Solution found!");
        printSolution(g_n, output);
        //output.println(topState.getG_n() + " Moves");
        output.println("Number of nodes expanded: " + nodesExpanded);
        output.println("Number of nodes unexpanded: " + queue.size());
        output.println();
    }*/

    //I could not get this to work

    /* Prints out current board state */
    public void printState(int[][] currentGrid) {
        for(int i = 0; i< SIZE; i++) {
            for(int j = 0; j< SIZE; j++) {
                System.out.print(currentGrid[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /* Determines whether two states are the same */
    private boolean stateEqual(int[][] state1, int[][] state2) {
        for(int i = 0; i< SIZE; i++)
            for(int j = 0; j< SIZE; j++)
                if(state1[i][j] != state2[i][j])
                    return false;
        return true;
    }

    /* Determines whether the child node has already been explored */
    private boolean containsChild(State state) {
        int[][] child = state.getGrid();
        for(State state2 : expanded) {
            int[][] temp = state2.getGrid();
            boolean identical = true;
            for(int i = 0; i< SIZE; i++)
                for(int j = 0; j< SIZE; j++)
                    if(temp[i][j] != child[i][j])
                        identical = false;
            if(identical)
                return true;
        }
        return false;
    }
}

