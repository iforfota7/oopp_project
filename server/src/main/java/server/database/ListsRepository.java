/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.database;

import commons.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ListsRepository extends JpaRepository<Lists, Long> {

    /**
     * Custom update query that decreases the position of lists
     * inside the board after a list gets removed
     * E.g. If the list at position 3 is deleted, all lists
     * that had a position > 3 will get their positions decreased by 1
     * @param deletedListPosition The index of the deleted list
     */
    @Modifying
    @Query(value = "UPDATE Lists " +
            "SET Lists.POSITION_INSIDE_BOARD = Lists.POSITION_INSIDE_BOARD - 1 " +
            "WHERE Lists.POSITION_INSIDE_BOARD > ?1",
            nativeQuery = true)
    void decrementListPositions(int deletedListPosition);

    /**
     * Custom update query that increases the position of lists inside
     * the board after a list gets inserted E.g. If the list is inserted
     * at position 3, all lists that had a position >= 3 will get their positions increased by 1
     * @param positionInBoard The index of the deleted list
     */
    @Modifying
    @Query(value = "UPDATE Lists " +
            "SET Lists.POSITION_INSIDE_BOARD = Lists.POSITION_INSIDE_BOARD + 1 " +
            "WHERE Lists.POSITION_INSIDE_BOARD >= ?1",
            nativeQuery = true)
    void incrementListPosition(int positionInBoard);

    /**
     * Gets the maximum value of the POSITION_INSIDE_BOARD among all Lists
     *
     * @return The maximum value or null in case the repository contains no Lists
     */
    @Query(value = "SELECT MAX(POSITION_INSIDE_BOARD) " +
            "FROM LISTS",
            nativeQuery = true)
    Integer maxPositionInsideBoard();

    /**
     * Retrieves all Lists from the repository, ordered by their position inside board
     * Note that this method does not need implementation and is handled by JPA
     * since it adhered to the naming conventions
     * @return A List containing all sorted Lists entries
     */
    List<Lists> findAllByOrderByPositionInsideBoardAsc();

    /**
     * Retrieves all lists in specific board
     * from the repository, ordered by their position inside board
     * @param boardName the name of the board for which lists should be retrieved
     * @return A List containing relevant Lists entries
     */
    @Modifying
    @Query(value = "SELECT * " +
                    "FROM LISTS " +
                    "WHERE LISTS.BOARD_ID = ?1",
                    nativeQuery = true)
    List<Lists> findAllByOrderByPositionInsideBoardAsc(long boardName);

    /**
     * Remove all cards that are inside a List
     *
     * @param listID ID of the list from where to remove the cards
     */
    @Modifying
    @Query(value = "DELETE FROM CARDS " +
            "WHERE LIST_ID = ?1",
            nativeQuery = true)
    void removeCardsInsideList(long listID);
}