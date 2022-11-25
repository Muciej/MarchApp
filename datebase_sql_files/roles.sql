use beskida;

drop role if exists 'organiser', 'admin', 'volounteer', 'register', 'participant', 'login';
create role 'organiser', 'admin', 'volounteer', 'register', 'participant', 'login';

/*
Setting organiser role privileges
 */

grant select, insert, update, delete on beskida.* to 'organiser';
grant create view on beskida.* to 'organiser';


/*
 Setting volounteer role privileges
 */

grant select, insert, update, delete on beskida.uczestnik_punkt to 'volounteer';
grant select on beskida.uczestnicy to 'volounteer'
grant select, update, insert, delete on wolontariusz_punkt to volounteer;
grant select, update, insert, delete on punkty_online to volounteer;
grant select on personel to volounteer;


/*
 Setting admin role privileges
 */

grant volounteer to admin;
grant select, insert, update, delete on personel to admin;
grant insert, update, delete on uczestnicy to admin;
grant all on uczestnicy_do_akceptacji to admin;


/*
 Setting participant role privileges
 */

grant select, update, insert on uczestnicy to participant;
grant select on uczestnik_punkt to participant;
grant select on punkty_kontrolne to participant;
grant select on konta to participant;
grant select on wolontariusz_punkt to participant;
grant select on punkty_kontrolne to participant;


/*
 Setting register role privileges
 */

grant all on uczestnicy_do_akceptacji to register;


/*
 Setting login role privileges
 */

grant select on konta to login;


insert into role (id_roli, nazwa, poziom_uprawnień) values
(1, 'Organizator', 'organiser'),
(2, 'Wolontariusz', 'volounteer'),
(3, 'Uczestnik', 'participant'),
(4, 'Administrator', 'admin'),
(5, 'Rejestracja', 'register');