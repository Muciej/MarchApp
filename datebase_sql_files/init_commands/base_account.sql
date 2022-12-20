drop user if exists 'organiser_test_event'@;
drop user if exists 'login_test_event'@;
drop user if exists 'register_test_event'@;
drop user if exists 'viewer_test_event'@;

create user 'organiser_test_event'@ identified by 'examplepass';
grant 'organiser' to organise_test_event@;

create user 'login_test_event'@ identified by 'login';
grant login to 'login_test_event'@;

create user 'register_test_event'@ identified by 'register';
grant register to registe_test_event@;

create user 'viewer_test_event'@ identified by 'viewer';
grant march_viewer to 'viewer_test_event'@;

insert into test_event.konta (id_konta, login, hasło, rola_id) value
(1, 'Organizator', 'examplepass', 1);


