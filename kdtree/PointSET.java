import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;

public class PointSET {
    private final SET<Point2D> pointSet;

    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        LinkedList<Point2D> pointsInside = new LinkedList<Point2D>();
        for (Point2D currentPoint : pointSet) {
            if (rect.contains(currentPoint))
                pointsInside.add(currentPoint);
        }
        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D currentPoint : pointSet) {
            double distToP = currentPoint.distanceSquaredTo(p);
            if (distToP < nearestDistance) {
                nearestDistance = distToP;
                nearestPoint = currentPoint;
            }
        }
        return nearestPoint;
    }
}