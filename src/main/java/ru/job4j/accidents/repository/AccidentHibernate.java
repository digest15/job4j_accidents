package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentHibernate implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public Collection<Accident> list() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Accident a left join fetch a.type left join fetch a.rules", Accident.class)
                    .list();
        }
    }

    @Override
    public Accident add(Accident accident) {
        try (Session session = sf.openSession()) {
            session.save(accident);
            return accident;
        }
    }

    @Override
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Accident a left join fetch a.type left join fetch a.rules where a.id = :id", Accident.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    @Override
    public boolean update(Accident accident) {
        try (Session session = sf.openSession()) {
            session.update(accident);
            return true;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Session session = sf.openSession()) {
            session.delete(new Accident(id, null, null, null, null, null));
            return true;
        }
    }

    @Override
    public Collection<AccidentType> listTypes() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from AccidentType", AccidentType.class)
                    .list();
        }
    }

    @Override
    public Optional<AccidentType> getTypeById(int id) {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from AccidentType where id = :id", AccidentType.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    @Override
    public Collection<Rule> listRules() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Rule", Rule.class)
                    .list();
        }
    }

    @Override
    public Optional<Rule> getRuleById(int id) {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Rule where id = :id", Rule.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }
}
