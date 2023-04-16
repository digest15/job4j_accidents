package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class AccidentMem implements AccidentRepository{

    private final Map<Integer, Accident> data = new ConcurrentHashMap<>();

    private final AtomicInteger sequence = new AtomicInteger(0);

    private final Map<Integer, AccidentType> types = new HashMap<>();

    public AccidentMem() {
        initData();
    }

    private void initData() {
        types.put(1, new AccidentType(1, "Две машины"));
        types.put(2, new AccidentType(2, "Машина и человек"));
        types.put(3, new AccidentType(3, "Машина и велосипед"));
        types.put(4, new AccidentType(4, "Только человек"));

        add(new Accident(0, "Петр Арсентьев", "Собирает хворост в лесу", "Где-то под Воронежом", new AccidentType(4, null)));
        add(new Accident(0, "Башаров", "Смотрит шпили", "Солсбери", new AccidentType(4, null)));
        add(new Accident(0, "Петров", "Смотрит шпили", "Солсбери", new AccidentType(4, null)));
        add(new Accident(0, "Чипига", "Не исполняет приказы", "Москва", new AccidentType(4, null)));
    }

    @Override
    public Collection<Accident> list() {
        return data.values().stream()
                .map(this::setAccidentType)
                .collect(Collectors.toList());
    }

    @Override
    public Accident add(Accident accident) {
        int id = sequence.incrementAndGet();
        accident.setId(id);
        data.put(id, accident);
        return accident;
    }

    @Override
    public Accident findById(int id) {
        return Optional.ofNullable(data.get(id))
                .map(this::setAccidentType)
                .orElse(null);
    }

    @Override
    public boolean update(Accident accident) {
        return data.computeIfPresent(accident.getId(), (k, v) -> accident) != null;
    }

    @Override
    public boolean delete(int id) {
        return data.remove(id) != null;
    }

    @Override
    public Collection<AccidentType> listTypes() {
        return new ArrayList<>(types.values());
    }

    private Accident setAccidentType(Accident accident) {
        accident.setType(types.get(accident.getType().getId()));
        return accident;
    }
}
