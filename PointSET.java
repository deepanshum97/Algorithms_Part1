import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private SET<Point2D> treeSet;

    /**
     * Construct an empty set of points.
     *
     */
    public PointSET() {
        this.treeSet = new SET<>();
    }

    /**
     * Is the set empty?
     *
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Number of points in the set
     *
     * @return
     */
    public int size() {
        return treeSet.size();
    }

    /**
     * Add the point {@link edu.princeton.cs.algs4.Point2D} to the set (if it is not already in the set)
     *
     * @param point
     */
    public void insert(Point2D point) {
        validatePoint(point);
        treeSet.add(point);
    }

    /**
     * Does the set contain point p?
     *
     * @param point
     * @return
     */
    public boolean contains(Point2D point) {
        validatePoint(point);
        return treeSet.contains(point);
    }

    /**
     * Draw all points to standard draw
     *
     */
    public void draw() {
        for (Point2D point : treeSet) {
            point.draw();
        }
    }

    /**
     * All points that are inside the rectangle (or on the boundary)
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle cannot be null.");
        }
        List<Point2D> list = new ArrayList<>();
        for (Point2D point : treeSet) {
            if (rect.contains(point)) {
                list.add(new Point2D(point.x(), point.y()));
            }
        }

        return list;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param point
     *
     * @return
     */
    public Point2D nearest(Point2D point) {
        validatePoint(point);

        Point2D minPoint = null;
        for (Point2D currPoint : treeSet) {
            if (minPoint == null || currPoint.distanceSquaredTo(point) < minPoint.distanceSquaredTo(point)) {
                minPoint = new Point2D(currPoint.x(), currPoint.y());
            }
        }

        return minPoint;
    }

    private void validatePoint(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }
    }
    /**
     * Unit testing of the methods (optional)
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
