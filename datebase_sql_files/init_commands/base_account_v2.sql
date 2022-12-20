drop user if exists 'march_viewer'@;

create user 'march_viewer'@ identified by 'V*Yy$5Zgpm$fj$@s';
grant select on baza_biegow_przelajowych.eventy to 'march_viewer'@;