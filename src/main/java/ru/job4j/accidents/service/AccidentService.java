package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;
import java.util.Optional;

public interface AccidentService {

    Collection<Accident> list();

    Accident add(Accident accident);

    Accident add(Accident accident, String[] ids);

    Optional<Accident> findById(int id);

    boolean update(Accident accident, String[] ids);

    boolean delete(int id);

    Collection<AccidentType> listTypes();

    Optional<AccidentType> getTypeById(int id);

    Collection<Rule> listRules();

    Optional<Rule> getRuleById(int id);

}
