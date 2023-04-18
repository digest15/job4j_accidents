CREATE TABLE if not exists accidents (
  id serial primary key,
  name text not null,
  text text,
  address text,
  type_id int not null REFERENCES types(id)
);