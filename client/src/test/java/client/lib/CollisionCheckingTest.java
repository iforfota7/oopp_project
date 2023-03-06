package client.lib;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionCheckingTest {

    @Test
    void collideTotalOverlapTest() {
        Bounds bound1 = new BoundingBox(0, 0, 0, 10, 10, 0);
        Bounds bound2 = new BoundingBox(0, 0, 0, 20, 20, 0);
        assertTrue(CollisionChecking.collide(bound1, bound2));
        assertFalse(CollisionChecking.collide(bound2, bound1));
    }

    @Test
    void collidePartialOverlapTest() {
        Bounds bound1 = new BoundingBox(5, -10, 0, 10, 20, 0);
        Bounds bound2 = new BoundingBox(0, 0, 0, 20, 20, 0);
        assertTrue(CollisionChecking.collide(bound1, bound2));
        assertFalse(CollisionChecking.collide(bound2, bound1));
    }

    @Test
    void collideNoOverlapTest() {
        Bounds bound1 = new BoundingBox(100, 100, 0, 10, 10, 0);
        Bounds bound2 = new BoundingBox(0, 0, 0, 20, 20, 0);
        assertFalse(CollisionChecking.collide(bound1, bound2));
        assertFalse(CollisionChecking.collide(bound2, bound1));
    }

}