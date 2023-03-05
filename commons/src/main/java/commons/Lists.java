package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class Lists {
    @Id
    public String name;

    @OneToMany
    public List<Cards> cards;

    /**
     * Constructor method for the lists class
     * @param name the name of the list (acts as unique id)
     * @param cards a list of cards contained in the list
     */
    public Lists(String name, List<Cards> cards) {
        this.name = name;
        this.cards = cards;
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
        return Objects.equals(name, lists.name) && Objects.equals(cards, lists.cards);
    }

    /**
     * Hashcode method for lists class
     * @return the hashcode of the list
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, cards);
    }

    /**
     * To string method to return human-readable format
     * @return a string of the lists information
     */
    @Override
    public String toString() {
        return "Lists{" +
                "name='" + name + '\'' +
                ", cards=" + cards +
                '}';
    }
}

