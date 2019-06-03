/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (x == that.x) {
            if (y == that.y)
                return Double.NEGATIVE_INFINITY;
            return Double.POSITIVE_INFINITY;
        }
        int dy = that.y - y;
        if (dy == 0) return 0.0;
        return (that.y - y) / (double) (that.x - x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (y != that.y) {
            return y - that.y;
        }
        return x - that.x;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

    private class SlopeComparator implements Comparator<Point> {
        public int compare(Point p0, Point p1) {
            double slopeTo0 = Point.this.slopeTo(p0);
            double slopeTo1 = Point.this.slopeTo(p1);
            if (slopeTo0 == slopeTo1)
                return 0;
            if (slopeTo0 < slopeTo1)
                return -1;
            return 1;
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p0 = new Point(1, 1);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(2, 2);
        StdOut.println(p0.compareTo(p1));
        StdOut.println(p0.compareTo(p2));
        StdOut.println(p0.compareTo(p3));
        StdOut.println();

        StdOut.println(p1.compareTo(p0));
        StdOut.println(p1.compareTo(p2));
        StdOut.println(p1.compareTo(p3));
        StdOut.println();

        StdOut.println(p2.compareTo(p0));
        StdOut.println(p2.compareTo(p1));
        StdOut.println(p2.compareTo(p3));
        StdOut.println();

        StdOut.println(p3.compareTo(p0));
        StdOut.println(p3.compareTo(p1));
        StdOut.println(p3.compareTo(p2));

        Point[] points = new Point[] {p0, p1, p2, p3};
        Arrays.sort(points);
        StdOut.println(points);
    }
}
