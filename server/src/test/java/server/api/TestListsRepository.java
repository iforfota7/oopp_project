package server.api;

import commons.Cards;
import commons.Lists;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ListsRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestListsRepository implements ListsRepository {

    public final List<Lists> lists = new ArrayList<>();
    public final List<Cards> cards = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();
    private void call(String name) { calledMethods.add(name); }

    /**
     * Custom update query that decreases the position of lists inside
     * the board after a list gets removed E.g. If the list at position 3
     * is deleted, all lists that had a position > 3 will get their positions decreased by 1
     * @param deletedListPosition The index of the deleted list
     */
    @Override
    public void decrementListPositions(int deletedListPosition) {

        call("decrementListPosition");
        for(int i=0; i<lists.size(); i++) {

            if(lists.get(i).positionInsideBoard>deletedListPosition)
                lists.get(i).positionInsideBoard -= 1;
        }
    }

    /**
     * Custom update query that increases the position of lists inside
     * the board after a list gets inserted E.g. If the list is inserted at position 3,
     * all lists that had a position >= 3 will get their positions increased by 1
     * @param positionInBoard The index of the deleted list
     */
    @Override
    public void incrementListPosition(int positionInBoard) {

        call("incrementListPosition");

        for(int i=0; i<lists.size(); i++) {

            if(lists.get(i).positionInsideBoard>=positionInBoard)
                lists.get(i).positionInsideBoard += 1;
        }
    }

    /**
     * Gets the maximum value of the POSITION_INSIDE_BOARD among all Lists
     *
     * @return The maximum value or null in case the repository contains no Lists
     */
    @Override
    public Integer maxPositionInsideBoard() {

        call("maxPositionInsideBoard");
        int max=-1;
        for(int i=0; i<lists.size(); i++) {

            if(lists.get(i).positionInsideBoard>max)
                max=lists.get(i).positionInsideBoard;
        }

        if(max==-1) return null;

        return max;
    }

    /**
     * Retrieves all Lists from the repository, ordered by their position
     * inside board Note that this method does not need implementation and
     * is handled by JPA since it adhered to the naming conventions
     * @return A List containing all sorted Lists entries
     */
    @Override
    public List<Lists> findAllByOrderByPositionInsideBoardAsc() {

        call("findAllByOrderByPositionInsideBoardAsc");
        List<Lists> res = new ArrayList<>();
        for(int i=0; i<lists.size(); i++) {

            res.add(lists.get(i));
            //sort using insertion sort
            for(int j=res.size()-1; j>0; j--) {

                if(res.get(j).positionInsideBoard<res.get(j-1).positionInsideBoard) {

                    Lists temp = res.get(j);
                    res.set(j, res.get(j-1));
                    res.set(j-1, temp);
                }
                else break;
            }
        }
        return res;
    }

    /**
     * Remove all cards that are inside a List
     *
     * @param listID ID of the list from where to remove the cards
     */
    @Override
    public void removeCardsInsideList(long listID) {

        call("removeCardsInsideList");
        List<Cards> copy = new ArrayList<>();

        for(int i=0; i<cards.size(); i++) {
            copy.add(cards.get(i));
        }

        for(int i=0; i<copy.size(); i++) {

            if(copy.get(i).list.id==listID) {
                cards.remove(copy.get(i));
            }
        }
    }

    /**
     * @return
     */
    @Override
    public List<Lists> findAll() {

        calledMethods.add("findAll");
        return lists;
    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<Lists> findAll(Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction
     * provided in the {@code Pageable} object.
     * @param pageable
     * @return a page of entities
     */
    @Override
    public Page<Lists> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Lists> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param aLong must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    public void deleteById(Long aLong) {

    }

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public void delete(Lists entity) {

        call("delete");
        lists.remove(entity);
    }

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     *
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids}
     *         or one of its elements is {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given
     *         {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends Lists> entities) {

    }

    /**
     * Deletes all entities managed by the repository.
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Saves a given entity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Override
    public <S extends Lists> S save(S entity) {
        call("save");

        for(int i=0; i<lists.size(); i++)
            if(lists.get(i).id == entity.id) {
                lists.set(i, entity);
                return entity;
            }

        lists.add(entity);
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Lists> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param aLong must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public Optional<Lists> findById(Long aLong) {
        for(Lists l : lists)
            if(l.id == aLong)
                return Optional.of(l);
        return Optional.empty();
    }

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param aLong must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(Long aLong) {

        call("existsById");
        for(int i=0; i<lists.size(); i++) {

            if(lists.get(i).id==aLong)
                return true;
        }
        return false;
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Override
    public void flush() {

    }

    /**
     * Saves an entity and flushes changes instantly.
     *
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return the saved entity
     */
    @Override
    public <S extends Lists> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Saves all entities and flushes changes instantly.
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return the saved entities
     * @since 2.5
     */
    @Override
    public <S extends Lists> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Deletes the given entities in a batch which means it will create a single query.
     * This kind of operation leaves JPAs first level cache and the database out of sync.
     * Consider flushing the {@link EntityManager} before calling this method.
     * @param entities entities to be deleted. Must not be {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllInBatch(Iterable<Lists> entities) {

    }

    /**
     * Deletes the entities identified by the given ids using a single query.
     * This kind of operation leaves JPAs first level cache and the database out of sync.
     * Consider flushing the {@link EntityManager} before calling this method.
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Deletes all entities in a batch call.
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Returns a reference to the entity with the given identifier.
     * Depending on how the JPA persistence provider is implemented this
     * is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access. Some of them will
     * reject invalid identifiers immediately.
     * @param aLong must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see EntityManager#getReference(Class, Object) for details on when an exception is thrown.
     * @deprecated use {@link JpaRepository#} instead.
     */
    @Override
    public Lists getOne(Long aLong) {
        return null;
    }

    /**
     * Returns a reference to the entity with the given identifier.
     * Depending on how the JPA persistence provider is implemented this
     * is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access.
     * Some of them will reject invalid identifiers immediately.
     * @param aLong must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see EntityManager#getReference(Class, Object) for details on when an exception is thrown.
     * @since 2.5
     */
    @Override
    public Lists getById(Long aLong) {
        return null;
    }

    /**
     * Returns a single entity matching the given {@link Example}
     * or {@link Optional#empty()} if none was found.
     * @param example must not be {@literal null}.
     * @return a single entity matching the given {@link Example} or
     *         {@link Optional#empty()} if none was found.
     * @throws IncorrectResultSizeDataAccessException if the Example yields more than one result.
     */
    @Override
    public <S extends Lists> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Lists> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort
     *                the results by, must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Lists> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Example}.
     * In case no match could be found, an empty {@link Page} is returned.
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return a {@link Page} of entities matching the given {@link Example}.
     */
    @Override
    public <S extends Lists> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return the number of instances matching the {@link Example}.
     */
    @Override
    public <S extends Lists> long count(Example<S> example) {
        return 0;
    }

    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @return {@literal true} if the data store contains elements that match
     *         the given {@link Example}.
     */
    @Override
    public <S extends Lists> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Returns entities matching the given {@link Example} applying the
     * {@link Function queryFunction} that defines the query and its result type.
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return all entities matching the given {@link Example}.
     * @since 2.6
     */
    @Override
    public <S extends Lists, R> R findBy(Example<S> example, Function<FluentQuery.
            FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
