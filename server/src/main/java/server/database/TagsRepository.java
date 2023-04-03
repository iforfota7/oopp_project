package server.database;

import commons.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Long> {

    @Modifying
    @Query(value = "SELECT *" + "FROM TAGS WHERE TAGS.BOARD_ID = ?1", nativeQuery = true)
    List<Tags> findAllTagsByBorderID(long boardID);

}
