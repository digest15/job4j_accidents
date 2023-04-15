package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;

import java.util.Collection;

public interface AccidentRepository {

    Collection<Accident> list();

    Accident add(Accident accident);

    Accident findById(int id);

    boolean update(Accident accident);

    boolean delete(int id);
}
