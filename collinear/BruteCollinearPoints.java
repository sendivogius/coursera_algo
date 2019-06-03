import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();
            for (int j = 0; j < i; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
        }
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        calcSegments(pointsCopy);
    }

    private void calcSegments(Point[] points) {
        ArrayList<LineSegment> tempSegments = new ArrayList<>();
        int n = points.length;
        for (int p = 0; p < n; p++) {
            Point pP = points[p];
            for (int q = p + 1; q < n; q++) {
                Point pQ = points[q];
                double slopePq = pP.slopeTo(pQ);
                for (int r = q + 1; r < n; r++) {
                    Point pR = points[r];
                    double slopePr = pP.slopeTo(pR);
                    if (slopePq != slopePr) continue;
                    for (int s = r + 1; s < n; s++) {
                        Point pS = points[s];
                        double slopePs = pP.slopeTo(pS);
                        if (slopePs != slopePr) continue;

                        tempSegments.add(new LineSegment(pP, pS));
                    }
                }
            }
        }
        segments = tempSegments.toArray(new LineSegment[tempSegments.size()]);
    }

    public int numberOfSegments() {
        return this.segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, numberOfSegments());
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point[] points = new Point[n];
        while (n-- > 0) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[points.length-1-n] = new Point(x, y);
        }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        LineSegment[] seg = bcp.segments();
        seg[0] = new LineSegment(new Point(1,1), new Point(2,2));
        StdOut.println(bcp.numberOfSegments());

    }
}