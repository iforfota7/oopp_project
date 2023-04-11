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

import commons.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CardsRepository extends JpaRepository<Cards, Long> {

    /**
     * Custom update query that decreases the position of cards inside
     * the list after a card gets removed from said list
     * E.g. If the card at position 3 is deleted, all cards that
     * had a position > 3 will get their positions decreased by 1
     * @param positionInList The index of the deleted card
     * @param listID the id of the list holding the card
     */
    @Modifying
    @Query(value = "UPDATE Cards " +
            "SET Cards.POSITION_INSIDE_LIST = Cards.POSITION_INSIDE_LIST - 1 " +
            "WHERE Cards.POSITION_INSIDE_LIST > ?1 AND Cards.LIST_ID = ?2",
            nativeQuery = true)
    void decrementCardPosition(int positionInList, long listID);

    /**
     * Custom update query that increases the position of cards
     * inside the list after a new card gets added to said list
     * @param positionInList The index of the added card
     * @param listID the id of the list holding the card
     */
    @Modifying
    @Query(value = "UPDATE Cards " +
            "SET Cards.POSITION_INSIDE_LIST = Cards.POSITION_INSIDE_LIST + 1 " +
            "WHERE Cards.POSITION_INSIDE_LIST >= ?1 AND Cards.LIST_ID = ?2",
            nativeQuery = true)
    void incrementCardPosition(int positionInList, long listID);

    /**
     * Gets the maximum value of the POSITION_INSIDE_LIST among all cards inside
     * a specific Lists entity
     *
     * @param listID The list id of the Lists entity that we search inside of
     * @return The maximum value or null in case the repository contains no Cards
     */
    @Query(value = "SELECT MAX(POSITION_INSIDE_LIST) " +
            "FROM CARDS " +
            "WHERE LIST_ID = ?1",
            nativeQuery = true)
    Integer maxPositionInsideList(long listID);

}