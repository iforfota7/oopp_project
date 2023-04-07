package server.api;

import commons.Boards;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestBoardsRepository implements BoardsRepository {

public final List<Boards> boards = new ArrayList<>();



public final List<String> calledMethods = new ArrayList<>();

private void call(String name) {calledMethods.add(name);}
    @Override
    public Optional<Boards> findByName(String name) {
    call("findByName");
        for(Boards b : boards){
            if(b.name.equals(name))
                return Optional.of(b);
        }
        return Optional.empty();
    }

    @Override
    public void removeReferenced(Long boardId) {

    }

    /**
     * @return
     */
    @Override
    public List<Boards> findAll() {
        calledMethods.add("findAll");
        return boards;
    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<Boards> findAll(Sort sort) {

        return null;
    }
    /**
     * Returns a {@link Page} of entities meeting the paging
     * restriction provided in the {@code Pageable} object.
     * @param pageable
     * @return a page of entities;
     */
    @Override
    public Page<Boards> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Boards> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
       boards.removeIf(x-> x.id == aLong);
    }

    @Override
    public void delete(Boards entity) {
        call("delete");
        boards.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Boards> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Boards> S save(S entity) {
        call("save");
        boolean cardExists = false;

        for(Boards b : boards){
            if(b.id == entity.id){
                cardExists = true;
               boards.remove(b);
               boards.add(entity);

            }
            if(b.name == entity.name)
                cardExists = true;
        }
        if(cardExists == false)
                boards.add(entity);


        return entity;
    }

    @Override
    public <S extends Boards> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Boards> findById(Long aLong) {
        call("findById");
        for(Boards b : boards){
            if(b.id == aLong){
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }



    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        for(int i = 0; i<boards.size(); i++){
            Boards board = boards.get(i);
            if(board.id == aLong)
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Boards> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Boards> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Boards> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Boards getOne(Long aLong) {
        return null;
    }

    @Override
    public Boards getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Boards> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Boards> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Boards> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Boards> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Boards> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Boards> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Boards, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
