package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Column(unique = true)
    public String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @OrderBy("positionInsideBoard ASC")
    @JsonIgnore
    public List<Lists> lists;

    @OneToMany(mappedBy="board", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Tags> tags;

    /**
     * Constructor method for a board
     * @param name the name of the board (acts as unique id)
     * @param lists a list of lists contained in the board
     * @param tags a list of tags contained in the board
     */
    public Boards(String name, List<Lists> lists, List<Tags> tags){
        this.name = name;
        this.lists = lists;
        this.tags = tags;
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
        return id == boards.id && Objects.equals(name, boards.name)
                && Objects.equals(lists, boards.lists);
    }

    /**
     * Hashcode method for Boards class
     * @return the hashcode of the board
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, lists);
    }

    /**
     * To string method for the board
     * @return human-readable format of board information
     */
    @Override
    public String toString() {
        return "Boards{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lists=" + lists.toString() +
                '}';
    }
}
