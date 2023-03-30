package server.database;

import commons.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

    /**
     * Query to find a board by name
     * @param name the name of the board
     * @return a board with that name, if it exists
     */
    @Query(value = "SELECT * FROM BOARDS WHERE BOARDS.NAME = ?1",
            nativeQuery = true)
    Optional<Boards> findByName(String name);

}
