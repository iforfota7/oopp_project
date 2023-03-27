package server.database;

import commons.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards, String> {
    @Query(value = "SELECT * FROM BOARDS", nativeQuery = true)
    List<Boards> findAllByOrderInsideOverviewAsc();

}
