import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
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
        Point[] pointsTemp;
        int numPoints = points.length;
        for (int i = 0; i < numPoints; i++) {
            pointsTemp = Arrays.copyOf(points, numPoints);
            Arrays.sort(pointsTemp, points[i].slopeOrder());
            int n = 0;
            double ps = Double.POSITIVE_INFINITY;

            for (int j = 1; j < numPoints; j++) {
                double slope = points[i].slopeTo(pointsTemp[j]);
                if (slope == ps) {
                    n++;
                } else {
                    ps = slope;
                    Point pStart = pointsTemp[j-n];
                    if (n >= 3 && points[i].compareTo(pStart) < 0) {
                        Point pEnd = pointsTemp[j-1];
                        tempSegments.add(new LineSegment(points[i], pEnd));
                    }
                    n = 1;
                }
            }
            if (n >= 3 && points[i].compareTo(pointsTemp[numPoints-n]) < 0) {
                Point pEnd = pointsTemp[numPoints-1];
                tempSegments.add(new LineSegment(points[i], pEnd));
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
            points[points.length - 1 - n] = new Point(x, y);
        }
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        StdOut.println(fcp.numberOfSegments());
    }
}