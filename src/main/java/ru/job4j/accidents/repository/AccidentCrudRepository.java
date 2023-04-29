package ru.job4j.accidents.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.Collection;
import java.util.Optional;

public interface AccidentCrudRepository extends CrudRepository<Accident, Integer> {

    @Override
    @EntityGraph(value = "accident.detail", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Accident> findAll();

    @Override
    @EntityGraph(value = "accident.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Accident> findById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("delete from Accident where id = :id")
    int delete(@Param("id") int id);

    @Query("from AccidentType")
    Collection<AccidentType> listTypes();

    @Query("from AccidentType where id = :id")
    Optional<AccidentType> getTypeById(@Param("id") int id);

    @Query("from Rule")
    Collection<Rule> listRules();

    @Query("from Rule where id = :id")
    Optional<Rule> getRuleById(@Param(("id")) int id);
}
