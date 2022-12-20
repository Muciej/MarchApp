drop user if exists 'march_viewer'@;

create user 'march_viewer'@ identified by 'V*Yy$5Zgpm$fj$@s';
grant select on baza_biegow_przelajowych.eventy to 'march_viewer'@;

insert into test_event.konta (login, hasło, rola_id) values
('Organizator', 'examplepass', 1),
('Admin', 'admin', 4),
('Piotruś', 'java_zle_pachnie', 2),
('Muciej', '2137', 2),
('Wiiika', 'haslo', 2);


