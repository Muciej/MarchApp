drop user if exists 'organiser'@;
drop user if exists 'login'@;
drop user if exists 'register'@;
drop user if exists 'viewer'@;

create user 'organiser'@ identified by 'examplepass';
grant 'organiser' to organiser@;

create user 'login'@ identified by 'login';
grant login to 'login'@;

create user 'register'@ identified by 'register';
grant register to register@;

create user 'viewer'@ identified by 'viewer';
grant march_viewer to 'viewer'@;

insert into beskida.konta (id_konta, login, hasło, rola_id) value
(1, 'Organizator', 'examplepass', 1);


