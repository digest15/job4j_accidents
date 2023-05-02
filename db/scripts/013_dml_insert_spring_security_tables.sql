insert into authorities (authority) values ('ROLE_USER');
insert into authorities (authority) values ('ROLE_ADMIN');

insert into users (username, enabled, password, authority_id) values ('root', true, '$2a$10$bOUrbS7C8DD39/rT2cbcTO6U5JQJCtz59sps8dZRFYqlCxeRJXai6', (select id from authorities where authority = 'ROLE_ADMIN'));