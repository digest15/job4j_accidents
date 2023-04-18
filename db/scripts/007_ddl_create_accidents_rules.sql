CREATE TABLE if not exists accidents_rules (
   id SERIAL PRIMARY KEY,
   accident_id int not null REFERENCES accidents(id),
   rules_id int not null REFERENCES rules(id)
);