package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;

@Entity
public class Cards {
    @Id
    public String name;

    public String description;
    public List<String> tasks;

    @ManyToOne
    public Lists list;

    public Cards(String name, String description, List<String> tasks, Lists list) {
        this.name = name;
        this.description = description;
        this.tasks = tasks;
        this.list = list;
    }

    @SuppressWarnings("unused")
    public Cards(){
        // for object mapper
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return Objects.equals(name, cards.name) && Objects.equals(description, cards.description) && Objects.equals(tasks, cards.tasks) && Objects.equals(list, cards.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, tasks, list);
    }

    @Override
    public String toString() {
        return "Cards{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                ", list=" + list +
                '}';
    }
}
