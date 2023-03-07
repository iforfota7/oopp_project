package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Objects;

@Entity
public class Cards {
    @Id
    public String name;
    @OneToOne
    public Lists list;

    /**
     * Constructor method for the cards class
     * @param name the name of the card (acts as unique id)
     * @param list the list in which the card is
     */
    public Cards(String name, String description, List<String> tasks, Lists list) {
        this.name = name;
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
        return Objects.equals(name, cards.name) && Objects.equals(list, cards.list);
    }

    /**
     * Hashcode method for the cards class
     * @return the hashcode of a card
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, list);
    }

    /**
     * To string method to return cards object in human-readable format
     * @return a string with the cards information
     */
    @Override
    public String toString() {
        return "Cards{" +
                "name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
