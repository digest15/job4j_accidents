package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem implements AccidentRepository{

    private final Map<Integer, Accident> data = new ConcurrentHashMap<>();

    private final AtomicInteger sequence = new AtomicInteger(0);

    public AccidentMem() {
        initData();
    }

    private void initData() {
        add(new Accident(0, "Петр Арсентьев", "Собирает хворост в лесу", "Где-то под Воронежом"));
        add(new Accident(0, "Башаров", "Смотрит шпили", "Солсбери"));
        add(new Accident(0, "Петров", "Смотрит шпили", "Солсбери"));
        add(new Accident(0, "Чипига", "Не исполняет приказы", "Москва"));
    }

    @Override
    public Collection<Accident> list() {
        return new ArrayList<>(data.values());
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
        return data.get(id);
    }

    @Override
    public boolean update(Accident accident) {
        return data.computeIfPresent(accident.getId(), (k, v) -> accident) != null;
    }

    @Override
    public boolean delete(int id) {
        return data.remove(id) != null;
    }
}
