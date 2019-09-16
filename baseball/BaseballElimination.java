import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;


public class BaseballElimination {
    private ST<String, Integer> teams;
    private int[] wins;
    private int[] loses;
    private int[] remaining;
    private int[][] remainingAgainst;
    private int maxWins;


    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();

        teams = new ST<String, Integer>();
        wins = new int[n];
        loses = new int[n];
        remaining = new int[n];
        remainingAgainst = new int[n][n];

        for (int i = 0; i < n; i++) {
            teams.put(in.readString(), i);
            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                remainingAgainst[i][j] = in.readInt();
            }
        }
        maxWins = StdStats.max(wins);
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
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return isTriviallyEliminated(team);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        return teams.keys();
    }

    private boolean isTriviallyEliminated(String team) {
        int i = teams.get(team);
        return wins[i] + remaining[i] < maxWins;
    }

    public int numberOfTeams() {
        return wins.length;
    }

    public int wins(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return loses[teams.get(team)];
    }

    public int remaining(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException();
        }
        return remainingAgainst[teams.get(team1)][teams.get(team2)];
    }
}
