drop database if exists ev_test_event;
CREATE DATABASE ev_test_event;

CREATE TABLE ev_test_event.`punkty_kontrolne` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `kolejność` INT NOT NULL,
  `online` TINYINT NULL,
  `nazwa` VARCHAR(45) NULL,
  `kilometr` INT NULL,
  `współrzędne_geograficzne` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX kolejność_UNIQUE (kolejność ASC));


CREATE TABLE ev_test_event.`uczestnicy` (
  `nr_startowy` INT NOT NULL auto_increment,
  `id_konta` INT NOT NULL,
  `imie` VARCHAR(60) NULL,
  `nazwisko` VARCHAR(60) NULL,
  `pseudonim` VARCHAR(45) NULL,
  'kod_qr'  VARCHAR(15) NULL,
  PRIMARY KEY (`nr_startowy`),
  UNIQUE INDEX `id_konta_UNIQUE` (`id_konta` ASC));


CREATE TABLE ev_test_event.`uczestnicy_do_akceptacji` (
  `id` INT NOT NULL,
  `imie` VARCHAR(45) NULL,
  `nazwisko` VARCHAR(60) NULL,
  `pseudonim` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE ev_test_event.`role` (
  `id_roli` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(30) NOT NULL,
  poziom_uprawnień VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_roli`),
  UNIQUE INDEX `nazwa_UNIQUE` (`nazwa` ASC),
  UNIQUE INDEX `poziom_uprawnień_UNIQUE` (poziom_uprawnień ASC));

CREATE TABLE ev_test_event.`konta` (
  `id_konta` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `hasło` CHAR(100) NOT NULL,
  `rola_id` INT NOT NULL,
  PRIMARY KEY (`id_konta`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  UNIQUE INDEX `hasło_UNIQUE` (`hasło` ASC));


CREATE TABLE ev_test_event.`uczestnik_punkt` (
  `id_uczestnika` INT NOT NULL,
  `id_punktu` INT NOT NULL,
  `data` DATETIME NOT NULL);

CREATE TABLE ev_test_event.`wolontariusz_punkt` (
  `id_wolontariusza` INT NOT NULL,
  `id_punktu` VARCHAR(45) NOT NULL);


CREATE TABLE ev_test_event.`personel` (
  `id_osoby` INT NOT NULL AUTO_INCREMENT,
  `id_konta` INT NOT NULL,
  `imie` VARCHAR(45) NOT NULL,
  `nazwisko` VARCHAR(60) NOT NULL,
  `nr_telefonu` VARCHAR(9) NOT NULL,
  `mail` VARCHAR(50) NULL,
  PRIMARY KEY (`id_osoby`));


CREATE TABLE ev_test_event.`punkty_online` (
  `id_punktu` INT NOT NULL,
  `ostatnia_aktywność` VARCHAR(45) NOT NULL);

ALTER TABLE ev_test_event.`uczestnicy`
ADD CONSTRAINT `id_konta_foreign`
  FOREIGN KEY (`id_konta`)
  REFERENCES ev_test_event.`konta` (`id_konta`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE ev_test_event.`uczestnicy_do_akceptacji`
ADD COLUMN `nr_telefonu` VARCHAR(9) NOT NULL AFTER `pseudonim`,
CHANGE COLUMN `id` `id` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `imie` `imie` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `nazwisko` `nazwisko` VARCHAR(60) NOT NULL ;

ALTER TABLE ev_test_event.`role`
DROP INDEX `poziom_uprawnień_UNIQUE` ;

ALTER TABLE ev_test_event.`konta`
ADD INDEX `rola_foreign_idx` (`rola_id` ASC);

ALTER TABLE ev_test_event.`konta`
ADD CONSTRAINT `rola_foreign`
  FOREIGN KEY (`rola_id`)
  REFERENCES ev_test_event.`role` (`id_roli`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;



ALTER TABLE ev_test_event.`uczestnik_punkt`
ADD INDEX `id_uczestnika_foreign_idx` (`id_uczestnika` ASC),
ADD INDEX `id_punktu_foreign_idx` (`id_punktu` ASC);

ALTER TABLE ev_test_event.`uczestnik_punkt`
ADD CONSTRAINT `id_uczestnika_foreign`
  FOREIGN KEY (`id_uczestnika`)
  REFERENCES ev_test_event.`uczestnicy` (`nr_startowy`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `id_punktu_foreign`
  FOREIGN KEY (`id_punktu`)
  REFERENCES ev_test_event.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE ev_test_event.`wolontariusz_punkt`
CHANGE COLUMN `id_punktu` `id_punktu` INT NOT NULL ;


ALTER TABLE ev_test_event.`wolontariusz_punkt`
ADD INDEX `wolontariusz_foreign_idx` (`id_wolontariusza` ASC),
ADD INDEX `punkt_foregin_idx` (`id_punktu` ASC);

ALTER TABLE ev_test_event.`wolontariusz_punkt`
ADD CONSTRAINT `wolontariusz_foreign`
  FOREIGN KEY (`id_wolontariusza`)
  REFERENCES ev_test_event.`personel` (`id_osoby`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `punkt_foregin`
  FOREIGN KEY (`id_punktu`)
  REFERENCES ev_test_event.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE ev_test_event.`personel`
ADD INDEX `konto_foreign_idx` (`id_konta` ASC);
;
ALTER TABLE ev_test_event.`personel`
ADD CONSTRAINT `konto_foreign`
  FOREIGN KEY (`id_konta`)
  REFERENCES ev_test_event.`konta` (`id_konta`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE ev_test_event.`punkty_online`
ADD INDEX `punkt2_foreign_idx` (`id_punktu` ASC);
;
ALTER TABLE ev_test_event.`punkty_online`
ADD CONSTRAINT `punkt2_foreign`
  FOREIGN KEY (`id_punktu`)
  REFERENCES ev_test_event.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE ev_test_event.`punkty_online`
CHANGE COLUMN `ostatnia_aktywność` `data_ost_aktywności` VARCHAR(45) NOT NULL ;


# use ev_test_event;
# DELIMITER $$
# CREATE TRIGGER `after_deleting_participant` AFTER DELETE ON`uczestnicy` FOR EACH ROW BEGIN
# 	 #jeśli usuwamy uczestnika to powinniśmy usunąć jego konto
#      #zakładam że uczestnika usuwamy przed rozpoczęciem biegu więc tabele takie jak uczestnik_punkt sa puste
#      DELETE FROM konta WHERE konta.id_konta = OLD.id_konta;
# END $$
# DELIMITER ;

insert into baza_biegow_przelajowych.eventy(nazwa, nazwa_bazy, rozpoczete) value
    ("test_event", "ev_test_event", true);

#tworzenie konta administratora dla danej bazy
drop user if exists admin_ev_test_event@;
create user 'admin_ev_test_event'@ identified by 'admin_ev_test_event';
grant all privileges on ev_test_event.* to 'admin_ev_test_event'@;

create view ev_test_event.wolontariusze_info_view as
select p.id_osoby, p.id_konta, wp.id_wolontariusza, wp.id_punktu, p.imie, p.nazwisko, p.nr_telefonu, p.mail, k.login, k.hasło
from ev_test_event.personel p
inner join ev_test_event.konta k on p.id_konta = k.id_konta
inner join ev_test_event.wolontariusz_punkt wp on p.id_osoby = wp.id_wolontariusza;
