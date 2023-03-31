package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public boolean checked;

    @ManyToOne
    public Cards card;

    public int position;

    /**
     * Constructor method for the Subtask class
     * @param id unique id of a subtask
     * @param title the title of the card
     * @param checked a boolean which defines the state of the checkbox
     *                of the subtask-> finished or not
     * @param card the parent card of the subtask
     */
    public Subtask(long id, String title, boolean checked, Cards card, int position) {
        this.id = id;
        this.title = title;
        this.checked = checked;
        this.card = card;
        this.position = position;
    }
    /**
     * Default constructor method for the object mapper
     */
    @SuppressWarnings("unused")
    public Subtask(){}

    /**
     * Equals method to compare subtask to another object
     * @param o an object that a subtask can be compared with
     * @return whether the subtask and object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return id == subtask.id && checked == subtask.checked
                && Objects.equals(title, subtask.title)
                && Objects.equals(card.id, subtask.card.id)
                && position == subtask.position;
    }

    /**
     * Hashcode method for the subtask class
     * @return the hashcode of a card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, checked, card, position);
    }

    /**
     * To string method to return subtask object in human-readable format
     *
     * @return a string with the subtasks information
     */
    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", checked=" + checked +
                ", card=" + card +
                ", position=" + position +
                '}';
    }
}
