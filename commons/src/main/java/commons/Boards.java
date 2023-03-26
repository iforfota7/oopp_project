package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Boards {
    @Id
    public String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @OrderBy("positionInsideBoardASC")
    public List<Lists> lists;

    /**
     * Constructor method for a board
     * @param name the name of the board (acts as unique id)
     * @param lists a list of lists contained in the board
     */
    public Boards(String name, List<Lists> lists){
        this.name = name;
        this.lists = lists;
    }

    /**
     * Default constructor for object mapper
     */
    @SuppressWarnings("unused")
    public Boards(){}

    /**
     * Equals method for boards class
     * @param o an object to be compared to a board
     * @return whether the object and board are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boards boards = (Boards) o;
        return Objects.equals(name, boards.name) && Objects.equals(lists, boards.lists);
    }

    /**
     * Hashcode method for boards class
     * @return hashcode of a given board
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, lists);
    }

    /**
     * To string method for the boards class
     * @return string containing information of board in human-readable format
     */
    @Override
    public String toString() {
        return "Boards{" +
                "name='" + name + '\'' +
                ", lists=" + lists +
                '}';
    }
}
