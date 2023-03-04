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

    public Lists(String name, List<Cards> cards) {
        this.name = name;
        this.cards = cards;
    }

    @SuppressWarnings("unused")
    private Lists(){
        // for object mapper
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lists lists = (Lists) o;
        return Objects.equals(name, lists.name) && Objects.equals(cards, lists.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cards);
    }

    @Override
    public String toString() {
        return "Lists{" +
                "name='" + name + '\'' +
                ", cards=" + cards +
                '}';
    }
}

