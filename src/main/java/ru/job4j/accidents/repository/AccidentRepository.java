package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;

public interface AccidentRepository {

    Collection<Accident> list();

    Accident add(Accident accident);

    Accident findById(int id);

    boolean update(Accident accident);

    boolean delete(int id);

    Collection<AccidentType> listTypes();

    Collection<Rule> listRules();
}
