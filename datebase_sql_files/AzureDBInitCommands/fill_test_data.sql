ALTER TABLE konta NOCHECK CONSTRAINT ALL;
ALTER TABLE uczestnicy NOCHECK CONSTRAINT ALL;
ALTER TABLE personel NOCHECK CONSTRAINT ALL;
ALTER TABLE punkty_kontrolne NOCHECK CONSTRAINT ALL;
ALTER TABLE role NOCHECK CONSTRAINT ALL;

insert into konta (login, hasło, rola_id) values
('Organizator', 'organizator', 1),
-- #     wolontariusze
('Wolontariusz', 'wolontariusz', 2),
('Muciej', '2137', 2),
('Wiiika', 'haslo', 2),
-- #     admini
('Administrator', 'admin', 4),
-- #     uczestnicy
('Uczestnik', 'uczestnik', 3),
('Bartek', 'jazda_z_tym_koksem', 3),
('Alan', 'matma<3', 3),
('Przemek', 'co_nie_lubi_drzemek', 3),
('Marta', 'kurczak', 3),
('Piotruś', 'java_zle_pachnie', 2);

insert into uczestnicy (id_konta, imie, nazwisko, pseudonim) values
(6, 'Uczestnik', 'Uczestniak', 'User'),
(7, 'Bartosz', N'Podbipięta', 'Bartek'),
(8, 'Alan', 'Kasprzykowski', 'ElAlamein'),
(9, N'Przemysław', 'Kruk', 'PrzemKo'),
(10, 'Marta', 'Nie_mam_pomyslu', 'Marcia'),
(11, 'Piotr', 'Piotrczak', N'Piotruś');

insert into personel (id_konta, imie, nazwisko, nr_telefonu, mail) values
(1, 'Organizator', 'Organizatorski', '666666666', 'orgzanizator@organizuje.com'),
(2, 'Wolontariusz', 'Wolontariuszowski', '213756753', 'wolontariusz@skanuje.com'),
(3, 'Maciej', N'Józefkowicz', '666666666', 'maciej.joz@lucy.com'),
(4, 'Wiktoria', N'Październikowska', '123321123', 'wiki@ikiw.com'),
(5, 'Administrator', 'Adminowski', '123050322', 'admin@i_tak_nie_odpisze.pl');

insert into punkty_kontrolne (online, nazwa, kilometr, współrzędne_geograficzne) values
(1, 'Start', 0, '30N, 50E'),
(1, N'Przełęcz salmopolska', 10, '51N 49,6E'),
(0, 'Schronisko PTTK', 20, '52N, 48E'),
(1, 'Meta', 30, '52N, 50E');

insert into role (nazwa, poziom_uprawnień) values
('Organizator', 'organiser'),
('Wolontariusz', 'volounteer'),
('Uczestnik', 'participant'),
('Administrator', 'admin'),
('Rejestracja', 'register');

ALTER TABLE konta CHECK CONSTRAINT ALL;
ALTER TABLE uczestnicy CHECK CONSTRAINT ALL;
ALTER TABLE personel CHECK CONSTRAINT ALL;
ALTER TABLE punkty_kontrolne CHECK CONSTRAINT ALL;
ALTER TABLE role CHECK CONSTRAINT ALL;