drop role if exists 'organiser', 'admin', 'volounteer', 'register', 'participant', 'login', 'march_viewer';
create role 'organiser', 'admin', 'volounteer', 'register', 'participant', 'login', 'march_viewer';

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
grant select, update, insert, delete on beskida.wolontariusz_punkt to volounteer;
grant select, update, insert, delete on beskida.punkty_online to volounteer;
grant select on beskida.personel to volounteer;


/*
 Setting admin role privileges
 */

grant volounteer to admin;
grant select, insert, update, delete on beskida.personel to admin;
grant insert, update, delete on beskida.uczestnicy to admin;
grant all on beskida.uczestnicy_do_akceptacji to admin;


/*
 Setting participant role privileges
 */

grant select, update, insert on beskida.uczestnicy to participant;
grant select on beskida.uczestnik_punkt to participant;
grant select on beskida.punkty_kontrolne to participant;
grant select on beskida.konta to participant;
grant select on beskida.wolontariusz_punkt to participant;
grant select on beskida.punkty_kontrolne to participant;


/*
 Setting register role privileges
 */

grant all on beskida.uczestnicy_do_akceptacji to register;


/*
 Setting login role privileges
 */

grant select on beskida.konta to login;


/*
 Setting march_viewer role privileges
 */

grant select on baza_biegow_przelajowych.eventy to 'march_viewer';

insert into beskida.role (id_roli, nazwa, poziom_uprawnień) values
(1, 'Organizator', 'organiser'),
(2, 'Wolontariusz', 'volounteer'),
(3, 'Uczestnik', 'participant'),
(4, 'Administrator', 'admin'),
(5, 'Rejestracja', 'register');