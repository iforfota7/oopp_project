package server.database;

import commons.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

    @Modifying
    @Query(value = "UPDATE Boards " +
            "SET Boards.POSITION_INSIDE_OVERVIEW = Boards.POSITION_INSIDE_OVERVIEW + 1 " +
            "WHERE Boards.POSITION_INSIDE_OVERVIEW >= ?1",
            nativeQuery = true)
    void incrementBoardsPosition(int positionInOverview);


    /**
     * Gets the maximum value of the POSITION_INSIDE_OVERVIEW among all Boards
     *
     * @return The maximum value or null in case the repository contains no Boards
     */
    @Query(value = "SELECT MAX(POSITION_INSIDE_OVERVIEW) " +
            "FROM BOARDS",
            nativeQuery = true)
    Integer maxPositionInsideBoard();
}
