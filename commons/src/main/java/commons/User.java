package commons;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
public class User {
    @Id
    public String username;

    @ManyToMany (cascade = CascadeType.ALL)
    public List<Boards> boards;

    public boolean isAdmin;
    @ElementCollection
    @CollectionTable(name = "color_preset", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name = "preset_name")
    @Column(name = "color")
    public Map<String, String> colorPreset = new HashMap<>();

    /**
     * Constructor method for User class
     * @param username the username of the user
     * @param boards a list of boards the user has visited
     * @param isAdmin whether the user is an admin
     */
    public User(String username, List<Boards> boards, boolean isAdmin) {
        this.username = username;
        this.boards = boards;
        this.isAdmin = isAdmin;
        Map<String, String> colorPreset = new HashMap<>();
        colorPreset.put("default", "#4169E1 #FFFFF0");
        colorPreset.put("HighLight", "#F08080 #DDA0DD");
        colorPreset.put("Less relevant", "#E0FFFF #AFEEEE");
        this.colorPreset = colorPreset;

    }

    /**
     * Default constructor method for the object mapper
     */
    @SuppressWarnings("unused")
    public User(){}

    /**
     * Equals method to compare to another object
     * @param o an object to be compared to
     * @return whether the object and user are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isAdmin == user.isAdmin &&
                Objects.equals(username, user.username) && Objects.equals(boards, user.boards);
    }

    /**
     * Hashcode method for the user class
     * @return the hashcode of a user
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, boards, isAdmin);
    }

    /**
     * To string method to return human-readable format
     * @return a string with the user information
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", boards=" + boards +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
