package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Objects;

@Entity
public class Cards {

    @Id
    public String id;
    public String title;
    public int positionInsideList;
    @ManyToOne
    public Lists list;

    /**
     * Constructor method for the cards class
     * @param id the fxml id of the card (acts as unique id)
     * @param title the name of the card
     * @param positionInsideList the position of card inside its list container
     * @param list the list in which the card is
     */
    public Cards(String id, String title, int positionInsideList, Lists list) {
        this.id = id;
        this.title = title;
        this.positionInsideList = positionInsideList;
        this.list = list;
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
        return positionInsideList == cards.positionInsideList &&
                Objects.equals(id, cards.id) && Objects.equals(title, cards.title) && Objects.equals(list, cards.list);
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
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", positionInsideList=" + positionInsideList +
                ", list=" + list +
                '}';
    }
}
