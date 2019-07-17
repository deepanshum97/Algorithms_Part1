import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private static final String EXCEPTION_NULL_ARRAY = "Point array cannot be null.";
    private static final String EXCEPTION_DUPLICATE_INPUT = "Point has more than one occurrence at location ";
    private static final String EXCEPTION_NULL_POINT = "Point cannot be null at location ";

    private LineSegment[] segments;
    private Point[] points;

    /**
     * Finds all line segments containing 4 or more points
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        validateInput(points);
        clonePoints(points);
        List<LineSegment> list = new ArrayList<>();
        int N = points.length;

        for (int i = 0 ; i < N ; i++) {
            Point [] aux = this.points.clone();
            Arrays.sort(aux, points[i].slopeOrder());

            int lo = 0;
            int hi = 0;
            double prevSlope = aux[0].slopeTo(points[i]);

            // System.out.println("prevSlope : "+ prevSlope);
            for (int j = 0 ; j < N ; j++) {
                double currSlope = aux[j].slopeTo(points[i]);
                // System.out.println("currSlope : "+ currSlope);
                if (currSlope != prevSlope) {
                    if (hi - lo + 1 >= 3) {
                        arrangeAndAddToLineSegments(points[i], aux, lo, hi, list);
                    }

                    lo = j;
                    hi = j;

                    prevSlope = currSlope;
                } else {
                    hi = j;
                }
            }

            if (hi - lo + 1 >= 3) {
                arrangeAndAddToLineSegments(points[i], aux, lo, hi, list);
            }
        }

        segments = list.toArray(new LineSegment[list.size()]);
    }

    private void arrangeAndAddToLineSegments(Point reference, Point[] points, int lo, int hi, List<LineSegment> list) {
        // Are we computing this for the first time.
        if (reference.compareTo(points[lo]) < 0) {
            list.add(new LineSegment(reference, points[hi]));
        }
    }

    private void clonePoints(Point [] points) {
        this.points = new Point[points.length];
        for (int i = 0 ; i < points.length ; i++) {
            this.points[i] = points[i];
        }

        Arrays.sort(this.points);
    }

    /**
     * The number of line segments
     *
     * @return
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * The line segments
     *
     * @return
     */
    public LineSegment[] segments() {
        return segments.clone();
    }

    private void validateInput(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException(EXCEPTION_NULL_ARRAY);
        }

        for (int i = 0 ; i < points.length ; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(EXCEPTION_NULL_POINT + i);
            }
        }

        for (int i = 0 ; i < points.length ; i++) {
            for (int j = i + 1 ; j < points.length ; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(EXCEPTION_DUPLICATE_INPUT + i + "and " + j);
                }
            }
        }
    }
}
