drop user if exists 'organiser'@;

create user 'organiser'@ identified by 'examplepass';
grant 'organiser' to organiser@;

create user 'login'@ identified by 'login';
grant login to 'login'@;

insert into konta (id_konta, login, hasło, rola_id) value
(1, 'Organizator', 'examplepass', 1);


