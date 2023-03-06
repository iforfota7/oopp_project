package client;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;

public class CollisionChecking {

    public static boolean collide(Bounds source, Bounds destination) {
        return commonArea(source, destination) >= area(source) / 2;
    }

    private static double area(Bounds source) {
        return (source.getMaxX() - source.getMinX()) * (source.getMaxY() - source.getMinY());
    }

    private static double commonArea(Bounds source, Bounds destination) {
        double x1 = Math.max(source.getMinX(), destination.getMinX());
        double x2 = Math.min(source.getMaxX(), destination.getMaxX());
        double y1 = Math.max(source.getMinY(), destination.getMinY());
        double y2 = Math.min(source.getMaxY(), destination.getMaxX());

        return Math.max(x2 - x1, 0) * Math.max(y2 - y1, 0);
    }

}
