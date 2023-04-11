import commons.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagsTest {
    Tags tag1;
    Tags tag2;
    Tags tag3;



    @BeforeEach
    void setUp(){

        tag1 = new Tags("title 1", "backgroundColor","fontColor");
        tag2 = new Tags("title 2", "backgroundColor 1","fontColor");
        tag3 = new Tags("title 2", "backgroundColor 1","fontColor");

    }
    @Test
    void testConstructor(){
        assertNotNull(new Tags("1", "2", "3"));
        assertNotNull(tag1);
        assertNotNull(new Tags());
    }
    @Test
    void testEqualsTrue() {
        assertEquals(tag2, tag3);
        tag1.title = tag2.title;
        tag1.backgroundColor = tag2.backgroundColor;
        assertEquals(tag1, tag2);
    }
    @Test
    void testEqualsFalse() {
        assertNotEquals(tag1, tag2);
    }
    @Test
    void testHashCode() {
        int hashcode1 = tag1.hashCode();

        assertNotEquals(hashcode1, tag2.hashCode());
        assertNotEquals(hashcode1, tag3.hashCode());
        assertEquals(hashcode1, tag1.hashCode());

        assertEquals(tag2.hashCode(), tag3.hashCode());
    }

    @Test
    void testToString() {
        String toStringTag = "Tags{id=0, title='title 1', " +
                "backgroundColor=backgroundColor, fontColor=fontColor}";

        assertEquals(tag1.toString(), toStringTag);
        assertEquals(tag3.toString(), tag2.toString());
        assertNotEquals(tag1.toString(), tag2.toString());
        assertNotEquals(tag1.toString(), tag3.toString());

    }
}