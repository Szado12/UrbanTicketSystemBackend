insert ignore into ticket_categories(id, name)
    values
        (1, 'SINGLE_TICKET'),
        (2, 'TIME_TICKET'),
        (3, 'SEASON_TICKET')
    /*as new
    on duplicate key update
        name=new.name*/;

insert ignore into ticket_types(id, days_of_validity, minutes_of_validity, price, reduced, category_id, display_name)
    values
         (1, 0, 0, 230, 1, 1, 'Single ticket'),
         (2, 0, 0, 460, 0, 1, 'Single ticket'),
         (3, 0, 15, 160, 1, 2, '15 minutes time ticket'),
         (4, 0, 15, 320, 0, 2, '15 minutes time ticket'),
         (5, 0, 30, 200, 1, 2, '30 minutes time ticket'),
         (6, 0, 30, 400, 0, 2, '30 minutes time ticket'),
         (7, 0, 60, 260, 1, 2, '60 minutes time ticket'),
         (8, 0, 60, 520, 0, 2, '60 minutes time ticket'),
         (9, 0, 90, 350, 1, 2, '90 minutes time ticket'),
         (10, 0, 90, 700, 0, 2, '90 minutes time ticket'),
         (11, 0, 1440, 750, 1, 2, '24 hours time ticket'),
         (12, 0, 1440, 1500, 0, 2, '24 hours time ticket'),
         (13, 0, 4320, 1600, 1, 2, '72 hours time ticket'),
         (14, 0, 4320, 3200, 0, 2, '72 hours time ticket'),
         (15, 7, 0, 1900, 1, 3, '7 days season ticket'),
         (16, 7, 0, 3800, 0, 3, '7 days season ticket'),
         (17, 30, 0, 4500, 1, 3, '30 days season ticket'),
         (18, 30, 0, 9000, 0, 3, '30 days season ticket'),
         (19, 60, 0, 10400, 1, 3, '60 days season ticket'),
         (20, 60, 0, 20800, 0, 3, '60 days season ticket'),
         (21, 90, 0, 15100, 1, 3, '90 days season ticket'),
         (22, 90, 0, 30200, 0, 3, '90 days season ticket'),
         (23, 180, 0, 28000, 1, 3, '180 days season ticket'),
         (24, 180, 0, 56000, 0, 3, '180 days season ticket'),
         (25, 365, 0, 52500, 1, 3, '365 days season ticket'),
         (26, 365, 0, 105000, 0, 3, '365 days season ticket')
    /*as new
    on duplicate key update
        days_of_validity=new.days_of_validity,
        minutes_of_validity=new.minutes_of_validity,
        price=new.price,
        reduced=new.reduced,
        category_id=new.category_id*/;

insert ignore into users(id, active, name, password, role, surname, username)
    values
        (1, 1, 'Jan', '$2a$10$7rJ9h.ExpmpdQczWEXdFGu6k7EIl9y/1ebTG/A2sa577uvJjoq926', 'ADMIN', 'Lewandowski', 'admin@gmail.com'),
        (2, 1, 'Anna', '$2a$10$7rJ9h.ExpmpdQczWEXdFGu6k7EIl9y/1ebTG/A2sa577uvJjoq926', 'STAFF', 'Kowalska', 'pracownik@gmail.com'),
        (3, 1, 'Krzysztof', '$2a$10$7rJ9h.ExpmpdQczWEXdFGu6k7EIl9y/1ebTG/A2sa577uvJjoq926', 'CLIENT', 'Adamczyk', 'klient@gmail.com')
    /*as new
    on duplicate key update
        active=new.active,
        name=new.name,
        password=new.password,
        role=new.role,
        surname=new.surname,
        username=new.username*/;

insert ignore into tickets(id, uuid, bought_time, ticket_status, validated_in_bus, validated_time, type_id, user_id)
    values
        (1, '990cad72', sysdate(), 'BOUGHT', 0, null, 5, 3),
        (2, '990cce71', sysdate(), 'BOUGHT', 0, null, 9, 3),
        (3, '990cd228', sysdate(), 'BOUGHT', 0, null, 14, 3);

insert into hibernate_sequence(next_val)
select 30
where not exists (select * from hibernate_sequence);

update hibernate_sequence set next_val=30 where next_val<30;
