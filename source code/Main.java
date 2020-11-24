package ss;

import java.util.*;
import static java.lang.System.exit;

public class Main extends Solver {

    public static void main(String[] args) {new Main();}

    private static final int ERROR = -1;  // cannot find solution
    private static final int FINISHED = 0;  // found goal state

    Main() {
        int returnVal = -1; //determines whether search has finished
        int algorithm; //used for search algorithm user choice

        Scanner scanner = new Scanner(System.in); //needed for integer parsing and error check

        System.out.println("Initial board:"); //printing of initial board
        printState(INITIAL_BOARD);
        System.out.println("Select a search algorithm:\n1) Uniform Cost Search\n2) A* Manhattan");
        algorithm = Integer.parseInt(scanner.nextLine());
        if(algorithm<=0 || algorithm>2) {
            System.err.println("Please enter a correct algorithm: 1 or 2!");
            exit(1);
        } else {
            returnVal = generalSearch(INITIAL_BOARD, algorithm); //if everything is fine set up the problem to be solved.
        }

        if(returnVal == FINISHED) {
            System.out.println("Solved!\n");
            System.out.println("Number of nodes expanded: " + nodesExpanded);
            System.out.println("Number of nodes unexpanded: " + unexpanded.size());
            //System.out.println("Number of moves: " + g_n); //access from State class
        } else if (returnVal == ERROR) {
            System.out.println("Error: No solution found!");
        }
    }
}
