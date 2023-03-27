package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public int positionInsideBoard;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    @OrderBy("positionInsideList ASC")
    public List<Cards> cards;

    @ManyToOne
    @JsonIgnore
    public Boards board;

    /**
     * Constructor method for the lists class
     * @param title the name of the list
     * @param positionInsideBoard the position of list inside the board
     */
    public Lists(String title, int positionInsideBoard, Boards board) {
        this.title = title;
        this.positionInsideBoard = positionInsideBoard;
        this.cards = new ArrayList<>();
        this.board = board;
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
        return id == lists.id && positionInsideBoard == lists.positionInsideBoard &&
                Objects.equals(title, lists.title) && Objects.equals(cards, lists.cards) &&
                Objects.equals(board, lists.board);
    }

    /**
     * Hashcode method for lists class
     * @return the hashcode of the list
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, positionInsideBoard, board);
    }

    /**
     * To string method to return human-readable format
     *
     * @return a string of the lists information
     */
    @Override
    public String toString() {
        return "Lists{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", positionInsideBoard=" + positionInsideBoard +
                ", cards=" + cards +
                ", board=" + board +
                '}';
    }
}

