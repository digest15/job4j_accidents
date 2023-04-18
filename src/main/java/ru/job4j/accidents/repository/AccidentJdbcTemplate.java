package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate implements AccidentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Collection<Accident> list() {
        String sql = """
                select
                	a.id,
                	a.name name,
                	a.text,
                	a.address,
                	t.id type_id,
                	t.name type_name,
                	r.id rule_id,
                	r.name rule_name
                from
                	accidents a
                inner join
                	types t
                		on a.type_id = t.id
                inner join
                	accidents_rules ar
                		on a.id = ar.accident_id
                left outer join
                	rules r
                		on ar.rules_id = r.id;
                """;

        List<Accident> accidents = jdbcTemplate.query(
                sql,
                accidentRowMapper()
        );

        return groupingAccidents(accidents);
    }

    @Override
    @Transactional
    public Accident add(Accident accident) {
        String sql = "INSERT INTO accidents (name, text, address, type_id) VALUES (?, ?, ?, ?);";

        Accident result = null;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updated = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);

        if (updated > 0) {
            accident.setId(keyHolder.getKeyAs(Integer.class));
            int[] updatedRules = insertAccidentsRules(accident);
            if (updatedRules.length == accident.getRules().size()) {
                result = accident;
            }
        }

        if (result == null) {
            throw new RuntimeException();
        }

        return result;
    }

    @Override
    @Transactional
    public Optional<Accident> findById(int id) {
        String sql = """
                select
                	a.id,
                	a.name name,
                	a.text,
                	a.address,
                	t.id type_id,
                	t.name type_name,
                	r.id rule_id,
                	r.name rule_name
                from
                	accidents a
                inner join
                	types t
                		on a.type_id = t.id
                inner join
                	accidents_rules ar
                		on a.id = ar.accident_id
                left outer join
                	rules r
                		on ar.rules_id = r.id
                where a.id = ?;
                """;

        List<Accident> selectedAccidents = jdbcTemplate.query(
                sql,
                accidentRowMapper(),
                id
        );

        Set<Accident> accidents = groupingAccidents(selectedAccidents);

        return accidents.stream().findAny();
    }

    @Override
    @Transactional
    public boolean update(Accident accident) {
        String sql = "UPDATE accidents SET name = ?, text = ?, address = ?, type_id = ? where id = ?;";

        int updated = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            ps.setInt(5, accident.getId());
            return ps;
        });

        if (updated == 0) {
            throw new RuntimeException();
        }

        deleteAccidentsRules(accident.getId());
        int[] updatedRules = insertAccidentsRules(accident);

        return updatedRules.length == accident.getRules().size();
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        deleteAccidentsRules(id);

        String sql = "DELETE FROM accidents where id = ?";
        int deletedCount = jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setInt(1, id);
                    return ps;
                }
        );

        return deletedCount > 0;
    }

    @Override
    @Transactional
    public Collection<AccidentType> listTypes() {
        String sql = "SELECT id, name FROM types";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new AccidentType(
                        rs.getInt("id"),
                        rs.getString("name"))
        );
    }

    @Override
    @Transactional
    public Optional<AccidentType> getTypeById(int id) {
        String sql = "SELECT id, name FROM types where id = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new AccidentType(
                        rs.getInt("id"),
                        rs.getString("name")),
                id
        ).stream().findAny();
    }

    @Override
    @Transactional
    public Collection<Rule> listRules() {
        String sql = "SELECT id, name FROM rules";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Rule(
                        rs.getInt("id"),
                        rs.getString("name"))
        );
    }

    @Override
    @Transactional
    public Optional<Rule> getRuleById(int id) {
        String sql = "SELECT id, name FROM types where id = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Rule(
                        rs.getInt("id"),
                        rs.getString("name")),
                id
        ).stream().findAny();
    }

    private int[] insertAccidentsRules(Accident accident) {
        List<Rule> rules = new ArrayList<>(accident.getRules());

        String sql = "INSERT INTO accidents_rules (accident_id, rules_id) VALUES (?, ?)";

        return this.jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Rule rule = rules.get(i);
                        ps.setInt(1, accident.getId());
                        ps.setInt(2, rule.getId());
                    }
                    public int getBatchSize() {
                        return rules.size();
                    }
                });
    }

    private int deleteAccidentsRules(int accidentId) {
        String sql = "DELETE FROM accidents_rules where accident_id = ?";

        return jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setInt(1, accidentId);
                    return ps;
                }
        );
    }

    private RowMapper<Accident> accidentRowMapper() {
        return (rs, rowNum) -> {
            Accident accident = new Accident();
            accident.setId(rs.getInt("id"));
            accident.setName(rs.getString("name"));
            accident.setText(rs.getString("text"));
            accident.setAddress(rs.getString("address"));
            accident.setType(new AccidentType(
                            rs.getInt("type_id"),
                            rs.getString("type_name")
                    )
            );
            Rule rule = new Rule(rs.getInt("rule_id"), rs.getString("rule_name"));
            Set<Rule> rules = new HashSet<>();
            rules.add(rule);
            accident.setRules(rules);
            return accident;
        };
    }

    private Set<Accident> groupingAccidents(List<Accident> accidents) {
        Map<Rule, List<Accident>> ruleByAccident = accidents.stream()
                .collect(Collectors.groupingBy(accident -> (Rule) accident.getRules().toArray()[0]));

        Set<Accident> collectedAccidents = new HashSet<>(accidents);
        for (Accident accident : collectedAccidents) {
            ruleByAccident.forEach((key, value) -> {
                if (value.contains(accident)) {
                    accident.getRules().add(key);
                }
            });
        }
        return collectedAccidents;
    }
}
