package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

   public String color;

    @ManyToOne
    public Boards board;


    public Tags(String title, String color, Boards boards) {
        this.title = title;
        this.color = color;
        this.board = boards;
    }

    private Tags(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tags = (Tags) o;
        return id == tags.id && color == tags.color && Objects.equals(title, tags.title) && Objects.equals(board, tags.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, color, board);
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", color=" + color +
                ", boards=" + board +
                '}';
    }
}
