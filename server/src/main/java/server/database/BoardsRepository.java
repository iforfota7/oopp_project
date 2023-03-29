package server.database;

import commons.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, String> {


    @Query(value = "SELECT * FROM BOARDS WHERE BOARDS.NAME = ?1",
            nativeQuery = true)
    Optional<Boards> findByName(String name);

}
