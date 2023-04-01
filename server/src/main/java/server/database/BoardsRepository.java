package server.database;

import commons.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {


    /** FindByName searches for board with specific name...
     * @param name - String name
     * @return - Board with that name, null otherwise
     */
    @Query(value = "SELECT * FROM BOARDS WHERE BOARDS.NAME = ?1",
            nativeQuery = true)
    Optional<Boards> findByName(String name);

    @Modifying
    @Query (value = "DELETE FROM USER_BOARDS WHERE BOARDS_ID = ?1",
            nativeQuery = true)
    void removeReferenced(Long boardId);

}
