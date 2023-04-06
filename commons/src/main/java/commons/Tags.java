package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

    public String color;


    /**
     * Constructor for the Tags entity
     *
     * @param title the title of the tag
     * @param color the color of the tag
     */
    public Tags(String title, String color) {
        this.title = title;
        this.color = color;
    }

    /**
     * Default constructor for the object mapper
     *
     */
    @SuppressWarnings("unused")
    public Tags(){}

    /**
     * Method for checking if 2 tags are equal
     *
     * @param o The other object
     * @return True iff the 2 objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tags = (Tags) o;
        return id == tags.id && Objects.equals(color, tags.color) &&
                Objects.equals(title, tags.title);
    }

    /**
     * Computes the hashcode of the tag object
     *
     * @return The hashcode of the tag
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, color);
    }

    /**
     * Returns the tag objects in a human-readable format
     *
     * @return The human-readable string
     */
    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", color=" + color +
                '}';
    }
}
