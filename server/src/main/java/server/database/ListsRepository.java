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


public interface ListsRepository extends JpaRepository<Lists, Long> {

    /**
     * Custom update query that decreases the position of lists inside the board after a list gets removed
     * E.g. If the list at position 3 is deleted, all lists that had a position > 3 will get their positions decreased by 1
     *
     * @param deletedListPosition The index of the deleted list
     */
    @Modifying
    @Query(value = "UPDATE Lists " +
            "SET Lists.POSITION_INSIDE_BOARD = Lists.POSITION_INSIDE_BOARD - 1 " +
            "WHERE Lists.POSITION_INSIDE_BOARD > ?1",
            nativeQuery = true)
    void decrementListPositions(int deletedListPosition);

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
}