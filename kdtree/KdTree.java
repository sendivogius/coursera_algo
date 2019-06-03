import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.LinkedList;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private final Point2D splitPoint;
        private final boolean isHorizontal;
        private final RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D p, boolean isHorizontal, RectHV r) {
            splitPoint = p;
            this.isHorizontal = isHorizontal;
            left = null;
            right = null;
            rect = r;
        }
    }


    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        root = insert(root, p, false, 0, 0, 1, 1);
    }

    private Node insert(Node subRoot, Point2D p, boolean isHorizontal, double xmin, double ymin,
                        double xmax, double ymax) {
        if (subRoot == null) {
            size++;
            return new Node(p, isHorizontal, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (subRoot.splitPoint.equals(p))
            return subRoot;

        if (!subRoot.isHorizontal) {
            if (p.x() < subRoot.splitPoint.x())
                subRoot.left = insert(subRoot.left, p, !isHorizontal,
                                      subRoot.rect.xmin(), subRoot.rect.ymin(),
                                      subRoot.splitPoint.x(), subRoot.rect.ymax());
            else
                subRoot.right = insert(subRoot.right, p, !isHorizontal,
                                       subRoot.splitPoint.x(), subRoot.rect.ymin(),
                                       subRoot.rect.xmax(), subRoot.rect.ymax());
        }
        else {
            if (p.y() < subRoot.splitPoint.y())
                subRoot.left = insert(subRoot.left, p, !isHorizontal,
                                      subRoot.rect.xmin(), subRoot.rect.ymin(),
                                      subRoot.rect.xmax(), subRoot.splitPoint.y());
            else
                subRoot.right = insert(subRoot.right, p, !isHorizontal,
                                       subRoot.rect.xmin(), subRoot.splitPoint.y(),
                                       subRoot.rect.xmax(), subRoot.rect.ymax());
        }
        return subRoot;

    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node subRoot, Point2D p) {
        if (subRoot == null)
            return false;
        if (subRoot.splitPoint.equals(p))
            return true;

        if (!subRoot.isHorizontal) {
            if (p.x() < subRoot.splitPoint.x())
                return contains(subRoot.left, p);
            else
                return contains(subRoot.right, p);
        }
        else {
            if (p.y() < subRoot.splitPoint.y())
                return contains(subRoot.left, p);
            else
                return contains(subRoot.right, p);
        }
    }

    public void draw() {
        draw(root, 0, 1, 0, 1);
    }

    private void draw(Node subRoot, double minX, double maxX, double minY, double maxY) {
        if (subRoot == null) return;
        double oldRadius = StdDraw.getPenRadius();
        // StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(Color.BLACK);
        subRoot.splitPoint.draw();
        StdDraw.setPenRadius(oldRadius);

        if (subRoot.isHorizontal) {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(minX, subRoot.splitPoint.y(), maxX, subRoot.splitPoint.y());

            draw(subRoot.left, minX, maxX, minY, subRoot.splitPoint.y());
            draw(subRoot.right, minX, maxX, subRoot.splitPoint.y(), maxY);
        }
        else { // vertical split
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(subRoot.splitPoint.x(), minY, subRoot.splitPoint.x(), maxY);

            draw(subRoot.left, minX, subRoot.splitPoint.x(), minY, maxY);
            draw(subRoot.right, subRoot.splitPoint.x(), maxX, minY, maxY);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        return range(root, rect);
    }

    private LinkedList<Point2D> range(Node subRoot, RectHV queryRect) {
        if (subRoot == null || !subRoot.rect.intersects(queryRect))
            return new LinkedList<Point2D>();

        LinkedList<Point2D> left = range(subRoot.left, queryRect);
        LinkedList<Point2D> right = range(subRoot.right, queryRect);
        left.addAll(right);

        if (queryRect.contains(subRoot.splitPoint))
            left.add(subRoot.splitPoint);
        return left;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (size == 1)
            return root.splitPoint;

        return nearest(root, p, Double.POSITIVE_INFINITY);
    }

    private Point2D nearest(Node subRoot, Point2D p, double minDistSoFar) {
        if (subRoot == null || subRoot.rect.distanceSquaredTo(p) >= minDistSoFar)
            return null;

        Point2D minPoint = null;

        double distToP = subRoot.splitPoint.distanceSquaredTo(p);
        if (minDistSoFar > distToP) {
            minDistSoFar = distToP;
            minPoint = subRoot.splitPoint;
        }

        Node first, second;
        if (subRoot.isHorizontal) {
            // ----
            if (p.y() < subRoot.splitPoint.y()) {
                first = subRoot.left;
                second = subRoot.right;
            }
            else {
                first = subRoot.right;
                second = subRoot.left;
            }
        }
        else {
            // |
            if (p.x() < subRoot.splitPoint.x()) {
                first = subRoot.left;
                second = subRoot.right;
            }
            else {
                first = subRoot.right;
                second = subRoot.left;
            }
        }

        Point2D minFirst = nearest(first, p, minDistSoFar);
        if (minFirst != null) {
            double minDistToFirst = minFirst.distanceSquaredTo(p);
            if (minDistToFirst < minDistSoFar) {
                minDistSoFar = minDistToFirst;
                minPoint = minFirst;
            }
        }

        Point2D minSecond = nearest(second, p, minDistSoFar);
        if (minSecond != null) {
            double minDistToSecond = minSecond.distanceSquaredTo(p);
            if (minDistToSecond < minDistSoFar) {
                minPoint = minSecond;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(1, 1));
        StdOut.println(k.size());
        k.insert(new Point2D(1, 1));
        StdOut.println(k.size());
        k.insert(new Point2D(1, 2));
        StdOut.println(k.size());
    }
}