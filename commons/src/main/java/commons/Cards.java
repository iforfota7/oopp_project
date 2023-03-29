package commons;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public int positionInsideList;
    public String description;

    @ManyToOne
    public Lists list;



    /**
     * Constructor method for the cards class
     * @param title the name of the card
     * @param positionInsideList the position of card inside its list container
     * @param list the list in which the card is
     */
    public Cards(String title, int positionInsideList, Lists list, String description) {
        this.title = title;
        this.positionInsideList = positionInsideList;
        this.list = list;
        this.description = description;
    }

    /**
     * Default constructor method for the object mapper
     */
    @SuppressWarnings("unused")
    public Cards(){}

    /**
     * Equals method to compare card to another object
     * @param o an object that a card can be compared with
     * @return whether the card and object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return id == cards.id && positionInsideList == cards.positionInsideList
                && Objects.equals(title, cards.title) && list.id==cards.list.id
                && Objects.equals(description, cards.description);
    }

    /**
     * Hashcode method for the cards class
     * @return the hashcode of a card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, positionInsideList, list);
    }

    /**
     * To string method to return cards object in human-readable format
     *
     * @return a string with the cards information
     */
    @Override
    public String toString() {
        return "Cards{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", positionInsideList=" + positionInsideList +
                ", description='" + description + '\'' +
                ", list=" + list +
                '}';
    }
}
