package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public int positionInsideBoard;

    /**
     * Constructor method for the lists class
     * @param title the name of the list
     * @param positionInsideBoard the position of list inside the board
     */
    public Lists(String title, int positionInsideBoard) {
        this.title = title;
        this.positionInsideBoard = positionInsideBoard;
    }

    /**
     * Default constructor method for object mapper
     */
    @SuppressWarnings("unused")
    private Lists(){}

    /**
     * Equals method for a list
     * @param o object to be compared to a list
     * @return whether the list and object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lists lists = (Lists) o;
        return id == lists.id && positionInsideBoard == lists.positionInsideBoard &&
                Objects.equals(title, lists.title);
    }

    /**
     * Hashcode method for lists class
     * @return the hashcode of the list
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, positionInsideBoard);
    }

    /**
     * To string method to return human-readable format
     *
     * @return a string of the lists information
     */
    @Override
    public String toString() {
        return "Lists{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", positionInsideBoard=" + positionInsideBoard +
                '}';
    }
}

