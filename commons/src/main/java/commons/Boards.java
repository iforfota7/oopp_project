package commons;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

public class Boards {
    @Id
    public String name;
    @OneToMany
    public List<Lists> lists;


    public Boards(String name, List<Lists> lists) {
        this.name = name;
        this.lists = lists;
    }

    @SuppressWarnings("unused")
    public Boards(){
        // for object mapper
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boards boards = (Boards) o;
        return Objects.equals(name, boards.name) && Objects.equals(lists, boards.lists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lists);
    }

    @Override
    public String toString() {
        return "Boards{" +
                "name='" + name + '\'' +
                ", lists=" + lists +
                '}';
    }
}
