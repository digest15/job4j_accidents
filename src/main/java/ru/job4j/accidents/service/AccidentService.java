package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;

import java.util.Collection;

public interface AccidentService {

    Collection<Accident> list();

}
