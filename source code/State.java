package ss;

// class describing nodes and state in the game tree
public class State extends Solver {

    private int[][] grid;
    private int g_n;  // movement cost
    private int h_n;  // heuristic

    State(int[][] grid, int g_n, int h_n) {
        this.grid = grid;
        this.g_n = g_n;
        this.h_n = h_n;
    }

    int[][] getGrid() {
        return grid;
    }

    int getG_n() {
        return g_n;
    }

    int getH_n() {
        return h_n;
    }

    int getF_n() {
        return (g_n + h_n);
    }
}