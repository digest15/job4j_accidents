package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements AccidentRepository{

    private final Map<Integer, Accident> data = new ConcurrentHashMap<>();

    public AccidentMem() {
        initData();
    }

    private void initData() {
        data.put(1, new Accident(1, "Петр Арсентьев", "Собирает хворост в лесу", "Где-то под Воронежом"));
        data.put(2, new Accident(2, "Башаров", "Смотрит шпили", "Солсбери"));
        data.put(3, new Accident(3, "Петров", "Смотрит шпили", "Солсбери"));
        data.put(4, new Accident(4, "Чипига", "Не исполняет приказы", "Москва"));
    }

    @Override
    public Collection<Accident> list() {
        return new ArrayList<>(data.values());
    }
}
