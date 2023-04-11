package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public String boardBgColor;
    public String boardFtColor;
    public String listBgColor;
    public String listFtColor;
    public String defaultColor;
    @ElementCollection
    @CollectionTable(name = "color_preset", joinColumns = @JoinColumn(name = "boardId"))
    @MapKeyColumn(name = "preset_name")
    @Column(name = "color")
    public Map<String, String> colorPreset = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.boardBgColor = "#E6E6FA";
        this.boardFtColor = "#000000";
        this.listBgColor = "#ffffff";
        this.listFtColor = "#000000";
        this.defaultColor = "default";
        this.tags = tags;
        Map<String, String> colorPreset = new HashMap<>();
        colorPreset.put("default", "#e6e6fa #000000");
        this.colorPreset = colorPreset;
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
        if (!(o instanceof Boards)) return false;
        Boards boards = (Boards) o;
        return id == boards.id && Objects.equals(name, boards.name) &&
                Objects.equals(lists, boards.lists) &&
                Objects.equals(boardBgColor, boards.boardBgColor) &&
                Objects.equals(boardFtColor, boards.boardFtColor) &&
                Objects.equals(listBgColor, boards.listBgColor) &&
                Objects.equals(listFtColor, boards.listFtColor) &&
                Objects.equals(defaultColor, boards.defaultColor) &&
                Objects.equals(colorPreset, boards.colorPreset) &&
                Objects.equals(tags, boards.tags);
    }

    /**
     * Hashcode method for Boards class
     * @return the hashcode of the board
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, lists, boardBgColor, boardFtColor,
                listBgColor, listFtColor, defaultColor, colorPreset, tags);
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
                ", lists=" + lists +
                ", boardBgColor='" + boardBgColor + '\'' +
                ", boardFtColor='" + boardFtColor + '\'' +
                ", listBgColor='" + listBgColor + '\'' +
                ", listFtColor='" + listFtColor + '\'' +
                ", defaultColor='" + defaultColor + '\'' +
                ", colorPreset=" + colorPreset +
                ", tags=" + tags +
                '}';
    }
}
