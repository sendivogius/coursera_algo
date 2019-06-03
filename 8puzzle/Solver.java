import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {
        private Board b;
        private int moves;
        private SearchNode prev;
        private int prior;

        public SearchNode(Board b, int moves, SearchNode prev) {
            this.b = b;
            this.moves = moves;
            this.prev = prev;
            this.prior = moves + b.manhattan();
        }


        @Override
        public int compareTo(SearchNode other) {
            return prior - other.prior;
        }
    }


    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();

        solution = solve(initial);
    }

    private SearchNode solve(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, 0, null));

        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        MinPQ<SearchNode> currentQueue = pq;

        while (true) {
            SearchNode current = currentQueue.delMin();
            if (current.b.isGoal()) {
                return currentQueue == pq ? current : null;
            }
            for (Board bn : current.b.neighbors()) {
                if (current.prev == null || !current.prev.b.equals(bn))
                    currentQueue.insert(new SearchNode(bn, current.moves + 1, current));
            }

            currentQueue = currentQueue == pq ? pqTwin : pq;
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        return isSolvable() ? solution.moves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> st = new Stack<>();
        SearchNode sn = solution;
        while (sn != null) {
            st.push(sn.b);
            sn = sn.prev;
        }
        return st;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}