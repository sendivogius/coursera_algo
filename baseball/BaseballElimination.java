import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;


public class BaseballElimination {
    private final ST<String, Bag<String>> ceos;
    private final ST<String, Integer> teams;
    private final int[] wins;
    private final int[] loses;
    private final int[] remaining;
    private final int[][] remainingAgainst;
    private int maxWins;
    private final boolean[] checked;
    private String maxWinsTeam;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        maxWins = 0;

        teams = new ST<String, Integer>();
        wins = new int[n];
        loses = new int[n];
        remaining = new int[n];
        remainingAgainst = new int[n][n];
        ceos = new ST<String, Bag<String>>();
        checked = new boolean[n];

        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teams.put(team, i);
            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                remainingAgainst[i][j] = in.readInt();
            }

            if (wins[i] > maxWins) {
                maxWins = wins[i];
                maxWinsTeam = team;
            }

        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    public Iterable<String> teams() {
        return teams.keys();
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        checkTeam(team);
        return !ceos.get(team).isEmpty();
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        checkTeam(team);
        return ceos.get(team).isEmpty() ? null : ceos.get(team);
    }

    private void checkTeam(String team) {
        if (checked[teams.get(team)])
            return;

        checked[teams.get(team)] = true;
        if (isTriviallyEliminated(team)) {
            Bag<String> b = new Bag<>();
            b.add(maxWinsTeam);
            ceos.put(team, b);
            return;
        }

        // build graph
        FlowNetwork net = buildEliminationNetwork(team);
        FordFulkerson ff = new FordFulkerson(net, net.V() - 2, net.V() - 1);
        Bag<String> b = new Bag<>();
        for (String t : teams()) {
            if (t.equals(team))
                continue;
            if (ff.inCut(teams.get(t)))
                b.add(t);
        }
        ceos.put(team, b);
    }

    private FlowNetwork buildEliminationNetwork(String team) {
        int n = numberOfTeams();
        int gameVertices = n * (n - 1) / 2;
        int teamVertices = n;

        FlowNetwork fn = new FlowNetwork(gameVertices + teamVertices + 2);
        final int startNode = gameVertices + teamVertices;
        final int endNode = startNode + 1;

        int teamNr = teams.get(team);
        // 0-(n-1) teams
        // n - games

        int gameIdx = teamVertices;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i == teamNr || j == teamNr) {
                    gameIdx++;
                    continue;
                }
                fn.addEdge(new FlowEdge(startNode, gameIdx, remainingAgainst[i][j]));
                // StdOut.println("edge " + startNode + " to" + gameIdx + " cap " + remainingAgainst[i][j]);

                fn.addEdge(new FlowEdge(gameIdx, i, Double.POSITIVE_INFINITY));
                // StdOut.println("edge " + gameIdx + " to" + i + " cap " + Double.POSITIVE_INFINITY);
                fn.addEdge(new FlowEdge(gameIdx, j, Double.POSITIVE_INFINITY));
                // StdOut.println("edge " + gameIdx + " to" + j + " cap " + Double.POSITIVE_INFINITY);

                gameIdx++;
            }
        }

        final int wn = wins[teamNr];
        final int rn = remaining[teamNr];
        for (int i = 0; i < n; i++) {
            if (i == teamNr) {
                continue;
            }
            int cap = wn + rn - wins[i];
            fn.addEdge(new FlowEdge(i, endNode, cap));
            // StdOut.println("edge " + i + " to" + endNode + " cap " + cap);

        }
        return fn;
    }

    private boolean isTriviallyEliminated(String team) {
        int i = teams.get(team);
        return wins[i] + remaining[i] < maxWins;
    }

    public int numberOfTeams() {
        return wins.length;
    }

    public int wins(String team) {
        validateTeam(team);
        return wins[teams.get(team)];
    }

    private void validateTeam(String team) {
        if (team == null || !teams.contains(team)) {
            throw new IllegalArgumentException();
        }
    }

    public int losses(String team) {
        validateTeam(team);
        return loses[teams.get(team)];
    }

    public int remaining(String team) {
        validateTeam(team);
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);

        return remainingAgainst[teams.get(team1)][teams.get(team2)];
    }
}
