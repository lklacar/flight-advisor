insert into AUTHORITY
values ('ROLE_ADMIN');

insert into AUTHORITY
values ('ROLE_USER');

insert into APPLICATION_USER
values (1,
        'system',
        now(),
        'system',
        now(),
        'Admin',
        'Admin',
        '$2a$10$R9/XnsSQ5HzJsudcygmt9OeD.byqQ5cUPKCQEraVwRLj5mVwDb9Lm',
        'admin');

insert into APPLICATION_USER_AUTHORITY
values (1, 'ROLE_ADMIN');
