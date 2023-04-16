package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccidentServiceImpl implements AccidentService {

    private final AccidentRepository accidentRepository;

    @Override
    public Collection<Accident> list() {
        return accidentRepository.list();
    }

    @Override
    public Accident add(Accident accident) {
        return accidentRepository.add(accident);
    }

    @Override
    public Accident add(Accident accident, String[] ids) {
        Set<Rule> rules = Arrays.stream(ids)
                .map(str -> new Rule(Integer.parseInt(str), null))
                .collect(Collectors.toSet());
        accident.setRules(rules);

        return add(accident);
    }

    @Override
    public Accident findById(int id) {
        return accidentRepository.findById(id);
    }

    @Override
    public boolean update(Accident accident) {
        return accidentRepository.update(accident);
    }

    @Override
    public boolean delete(int id) {
        return accidentRepository.delete(id);
    }

    @Override
    public Collection<AccidentType> listTypes() {
        return accidentRepository.listTypes();
    }

    @Override
    public Collection<Rule> listRules() {
        return accidentRepository.listRules();
    }
}
