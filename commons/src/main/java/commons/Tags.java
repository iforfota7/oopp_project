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

    public String backgroundColor;
    public String fontColor;


    /**
     * Constructor for the Tags entity
     *
     * @param title the title of the tag
     * @param backgroundColor the color of the tag (background)
     * @param fontColor the color of the font
     */
    public Tags(String title, String backgroundColor, String fontColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
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
        return id == tags.id && Objects.equals(backgroundColor, tags.backgroundColor) &&
                Objects.equals(fontColor, tags.fontColor) && Objects.equals(title, tags.title);
    }

    /**
     * Computes the hashcode of the tag object
     *
     * @return The hashcode of the tag
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, backgroundColor, fontColor);
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
                ", backgroundColor=" + backgroundColor +
                ", fontColor=" + fontColor +
                '}';
    }
}
