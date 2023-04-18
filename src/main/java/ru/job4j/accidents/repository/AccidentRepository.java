package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.Collection;
import java.util.Optional;

public interface AccidentRepository {

    Collection<Accident> list();

    Accident add(Accident accident);

    Optional<Accident> findById(int id);

    boolean update(Accident accident);

    boolean delete(int id);

    Collection<AccidentType> listTypes();

    Optional<AccidentType> getTypeById(int id);

    Collection<Rule> listRules();

    Optional<Rule> getRuleById(int id);
}
