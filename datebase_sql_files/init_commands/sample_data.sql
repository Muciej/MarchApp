set foreign_key_checks = false;
use ev_test_event;

insert into ev_test_event.konta (login, hasło, rola_id) value
('Bartek', 'jazda_z_tym_koksem', 3),
('Alan', 'matma<3', 3),
('Przemek', 'co_nie_lubi_drzemek', 3),
('Marta', 'kurczak', 3);

insert into ev_test_event.uczestnicy (nr_startowy,id_konta, imie, nazwisko, pseudonim) value
(1, 6, 'Bartosz', 'Podbipięta', 'Bartek'),
(2, 7, 'Alan', 'Kasprzykowski', 'ElAlamein'),
(3, 8, 'Przemysław', 'Kruk', 'PrzemKo'),
(4, 9, 'Marta', 'Nie_mam_pomyslu', 'Marcia');

insert into personel (id_konta, imie, nazwisko, nr_telefonu, mail) value
(1, 'Organizator', 'Organizatorski', '666666666', 'orgzanizator@organizuje.com'),
(2, 'Admin', 'Administatorski', '123987456', 'admin@wp.pl'),
(3, 'Piotr', 'Piotrowski', '111222333', 'Piotrus@pan.pl'),
(4, 'Maciej', 'Józefkowicz', '666356123', 'muciej@gmail.com');

insert into punkty_kontrolne (kolejność, online, nazwa, kilometr, współrzędne_geograficzne) value
(1, 1, 'Start', 0, '30N, 50E'),
(2, 1, 'Przełęcz salmopolska', 10, '51N 49,6E'),
(3, 0, 'Schronisko PTTK', 20, '52N, 48E'),
(4, 1, 'Meta', 30, '52N, 50E');

insert into ev_test_event.role (id_roli, nazwa, poziom_uprawnień) values
(1, 'Organizator', 'organiser'),
(2, 'Wolontariusz', 'volounteer'),
(3, 'Uczestnik', 'participant'),
(4, 'Administrator', 'admin'),
(5, 'Rejestracja', 'register');

set foreign_key_checks = true;