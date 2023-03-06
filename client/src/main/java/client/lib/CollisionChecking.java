package client.lib;

import javafx.geometry.Bounds;

public class CollisionChecking {

    /**
     * Checks whether a dragged rectangular shaped node collides with another rectangular shaped node
     * Two objects are said to collide iff their area of intersection is greater than 50% of the source node's area
     *
     * @param source The bounds of the dragged node
     * @param destination The bounds of the destination node
     * @return True iff the 2 objects are said to collide
     */
    public static boolean collide(Bounds source, Bounds destination) {
        return commonArea(source, destination) >= area(source) / 2;
    }

    /**
     * Returns the area of a rectangular shaped object
     *
     * @param source The rectangular shaped object
     * @return The area of the object
     */

    private static double area(Bounds source) {
        return (source.getMaxX() - source.getMinX()) * (source.getMaxY() - source.getMinY());
    }

    /**
     * Computes the area of intersection between 2 rectangular shapes
     *
     * @param source The first rectangular shape described as a Bound object
     * @param destination The second rectangular shape describe as a Bound object
     * @return The area of intersection of the 2 shapes
     */

    private static double commonArea(Bounds source, Bounds destination) {
        double x1 = Math.max(source.getMinX(), destination.getMinX());
        double x2 = Math.min(source.getMaxX(), destination.getMaxX());
        double y1 = Math.max(source.getMinY(), destination.getMinY());
        double y2 = Math.min(source.getMaxY(), destination.getMaxY());

        return Math.max(x2 - x1, 0) * Math.max(y2 - y1, 0);
    }

}
