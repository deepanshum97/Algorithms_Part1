import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;
import java.util.Iterator;

public class KdTree {

    private static boolean VERTICAL = true;
    private Tree2d treeSet;

    private static class Node2d {
        private RectHV rectHV;
        private Point2D key;
        private Node2d left, right;
        private boolean nodeType;
        private int size;

        public Node2d(Point2D key, boolean nodeType, int size, RectHV rectHV) {
            this.key = key;
            this.nodeType = nodeType;
            this.size = size;
            this.rectHV = rectHV;
        }
    }

    private class Tree2d implements Iterable<Point2D> {
        private Node2d root;
        private Point2D nearestPoint;
        private RectHV MASTER = new RectHV(0.0, 0.0, 1.0, 1.0);

        public Tree2d() {
            this.root = null;
        }

        public void add(Point2D key) {
            root = add(root, key, VERTICAL, MASTER);
        }

        public int size() {
            return size(root);
        }

        private Node2d add(Node2d root, Point2D key, boolean nodeType, RectHV rectHV) {
            if (root == null) {
                return new Node2d(key, nodeType, 1, rectHV);
            }

            Comparator<Point2D> comparator = root.nodeType == VERTICAL ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(key, root.key);
            if (cmp < 0) {
                root.left = add(root.left, key, !nodeType, buildLowerRectangle(root, rectHV));
            } else if (cmp == 0 && key.compareTo(root.key) == 0){
                root.key = key;
            } else {
                root.right = add(root.right, key, !nodeType, buildUpperRectangle(root, rectHV));
            }

            root.size = 1 + size(root.left) + size(root.right);
            return root;
        }

        private RectHV buildUpperRectangle(Node2d root, RectHV rectHV) {
            if (root.right != null) return root.right.rectHV;

            if (root.nodeType == VERTICAL) {
                return new RectHV(root.key.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax());
            }

            return new RectHV(rectHV.xmin(), root.key.y(), rectHV.xmax(), rectHV.ymax());
        }

        private RectHV buildLowerRectangle(Node2d root, RectHV rectHV) {
            if (root.left != null) return root.left.rectHV;

            if (root.nodeType == VERTICAL) {
                return new RectHV(rectHV.xmin(), rectHV.ymin(), root.key.x(), rectHV.ymax());
            }

            return new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), root.key.y());
        }

        private int size(Node2d node) {
            if (node == null) return 0;
            return node.size;
        }

        public boolean contains(Point2D key) {
            return get(root, key) != null;
        }

        public Point2D get(Node2d root, Point2D key) {
            if (root == null) return null;

            Comparator<Point2D> comparator = root.nodeType == VERTICAL ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(key, root.key);
            if (cmp < 0) {
                return get(root.left, key);
            } else if (cmp == 0 && key.compareTo(root.key) == 0) {
                return root.key;
            } else {
                return get(root.right, key);
            }
        }

        public Iterable<Point2D> rangeQuery(RectHV rect) {
            Queue<Point2D> queue = new Queue<>();
            collect(rect, root, queue);

            return queue;
        }

        private void collect(RectHV rect, Node2d root, Queue<Point2D> queue) {
            if (root == null) return;
            if (!rect.intersects(root.rectHV)) return;

            Point2D currPoint = root.key;

            if (rect.contains(currPoint)) {
                queue.enqueue(currPoint);
            }

            collect(rect, root.left, queue);
            collect(rect, root.right, queue);
        }

        @Override
        public Iterator<Point2D> iterator() {
            Queue<Point2D> queue = new Queue<>();
            collect(MASTER, root, queue);

            return queue.iterator();
        }

        public Point2D nearest(Point2D point) {
            this.nearestPoint = null;
            nearest(root, point);

            return nearestPoint;
        }

        private void nearest(Node2d root, Point2D point) {
            if (root == null) return;
            if (nearestPoint == null) {
                nearestPoint = root.key;
            }

            if (nearestPoint.distanceSquaredTo(point) <= root.rectHV.distanceSquaredTo(point)) return;
            if (point.distanceSquaredTo(root.key) <= point.distanceSquaredTo(nearestPoint)) {
                nearestPoint = root.key;
            }

            Comparator<Point2D> comparator = root.nodeType == VERTICAL ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(point, root.key);
            if (cmp < 0) {
                nearest(root.left, point);
                nearest(root.right, point);
            } else if (cmp == 0 && point.compareTo(root.key) == 0) {
                return;
            } else {
                nearest(root.right, point);
                nearest(root.left, point);
            }
        }
    }

    /**
     * Construct an empty set of points.
     *
     */
    public KdTree() {
        this.treeSet = new Tree2d();
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

        return treeSet.rangeQuery(rect);
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
        return treeSet.nearest(point);
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
