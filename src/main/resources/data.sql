insert ignore into ticket_categories(id, name)
    values
        (1, 'SINGLE_TICKET'),
        (2, 'TIME_TICKET'),
        (3, 'PERIOD_TICKET')
    /*as new
    on duplicate key update
        name=new.name*/;

insert ignore into ticket_types(id, days_of_validity, minutes_of_validity, price, reduced, category_id)
    values
         (1, 0, 0, 230, 1, 1),
         (2, 0, 0, 460, 0, 1),
         (3, 0, 15, 160, 1, 2),
         (4, 0, 15, 320, 0, 2),
         (5, 0, 30, 200, 1, 2),
         (6, 0, 30, 400, 0, 2),
         (7, 0, 60, 260, 1, 2),
         (8, 0, 60, 520, 0, 2),
         (9, 0, 90, 350, 1, 2),
         (10, 0, 90, 700, 0, 2),
         (11, 0, 1440, 750, 1, 2),
         (12, 0, 1440, 1500, 0, 2),
         (13, 0, 4320, 1600, 1, 2),
         (14, 0, 4320, 3200, 0, 2),
         (15, 0, 7, 1900, 1, 3),
         (16, 0, 7, 3800, 0, 3),
         (17, 0, 30, 4500, 1, 3),
         (18, 0, 30, 9000, 0, 3),
         (19, 0, 60, 10400, 1, 3),
         (20, 0, 60, 20800, 0, 3),
         (21, 0, 90, 15100, 1, 3),
         (22, 0, 90, 30200, 0, 3),
         (23, 0, 180, 28000, 1, 3),
         (24, 0, 180, 56000, 0, 3),
         (25, 0, 365, 52500, 1, 3),
         (26, 0, 365, 105000, 0, 3)
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

insert into hibernate_sequence(next_val)
select 30
where not exists (select * from hibernate_sequence);

update hibernate_sequence set next_val=30 where next_val<30;
