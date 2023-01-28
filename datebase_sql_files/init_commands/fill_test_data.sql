set foreign_key_checks = false;
use ev_test_event;

insert into ev_test_event.konta (login, hasło, rola_id) value
('Organizator', 'organizator', 1),
#     wolontariusze
('Wolontariusz', 'wolontariusz', 2),
('Muciej', '2137', 2),
('Wiiika', 'haslo', 2),
#     admini
('Administrator', 'admin', 4),
#     uczestnicy
('Uczestnik', 'uczestnik', 3),
('Bartek', 'jazda_z_tym_koksem', 3),
('Alan', 'matma<3', 3),
('Przemek', 'co_nie_lubi_drzemek', 3),
('Marta', 'kurczak', 3),
('Piotruś', 'java_zle_pachnie', 2);

insert into ev_test_event.uczestnicy (nr_startowy,id_konta, imie, nazwisko, pseudonim) value
(5, 6, 'Uczestnik', 'Uczestniak', 'User'),
(1, 7, 'Bartosz', 'Podbipięta', 'Bartek'),
(2, 8, 'Alan', 'Kasprzykowski', 'ElAlamein'),
(3, 9, 'Przemysław', 'Kruk', 'PrzemKo'),
(4, 10, 'Marta', 'Nie_mam_pomyslu', 'Marcia'),
(6, 11, 'Piotr', 'Piotrczak', 'Piotruś');

insert into personel (id_konta, imie, nazwisko, nr_telefonu, mail) value
(1, 'Organizator', 'Organizatorski', '666666666', 'orgzanizator@organizuje.com'),
(2, 'Wolontariusz', 'Wolontariuszowski', '213756753', 'wolontariusz@skanuje.com'),
(3, 'Maciej', 'Józefkowicz', '666666666', 'maciej.joz@lucy.com'),
(4, 'Wiktoria', 'Październikowska', '123321123', 'wiki@ikiw.com'),
(5, 'Administrator', 'Adminowski', '123050322', 'admin@i_tak_nie_odpisze.pl');

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