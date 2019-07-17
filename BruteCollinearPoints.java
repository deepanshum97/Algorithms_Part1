import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private static final String EXCEPTION_NULL_ARRAY = "Point array cannot be null.";
    private static final String EXCEPTION_DUPLICATE_INPUT = "Point has more than one occurrence at location ";
    private static final String EXCEPTION_NULL_POINT = "Point cannot be null at location ";

    private LineSegment[] segments;

    /**
     * Finds all line segments containing 4 points
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        validateInput(points);
        int len = points.length;
        List<LineSegment> list = new ArrayList<LineSegment>();
        for (int i = 0 ; i < len ; i++) {
            for (int j = i + 1 ; j < len ; j++) {
                for (int k = j + 1 ; k < len ; k++) {
                    for (int l = k + 1 ; l < len ; l++) {
                        boolean slopesEqual = points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]);
                        slopesEqual = slopesEqual && points[i].slopeTo(points[j]) == points[i].slopeTo(points[l]);

                        if (slopesEqual) {
                            arrangeAndAddToLineSegments(points, i, j, k, l, list);
                        }
                    }
                }
            }
        }

        segments = list.toArray(new LineSegment[list.size()]);
    }

    private void arrangeAndAddToLineSegments(Point[] points, int i, int j, int k, int l, List<LineSegment> list) {
        Point[] arr = new Point[4];
        arr[0] = points[i];
        arr[1] = points[j];
        arr[2] = points[k];
        arr[3] = points[l];

        Arrays.sort(arr);
        list.add(new LineSegment(arr[0], arr[3]));
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
