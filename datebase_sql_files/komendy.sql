CREATE DATABASE `beskida`;

CREATE TABLE `beskida`.`punkty_kontrolne` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `kolejność` INT NOT NULL,
  `online` TINYINT NULL,
  `nazwa` VARCHAR(45) NULL,
  `kilometr` INT NULL,
  `współrzędne_geograficzne` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX kolejność_UNIQUE (kolejność ASC) VISIBLE );


CREATE TABLE `beskida`.`uczestnicy` (
  `nr_startowy` INT NOT NULL,
  `id_konta` INT NOT NULL,
  `imie` VARCHAR(60) NULL,
  `nazwisko` VARCHAR(60) NULL,
  `pseudonim` VARCHAR(45) NULL,
  PRIMARY KEY (`nr_startowy`),
  UNIQUE INDEX `id_konta_UNIQUE` (`id_konta` ASC));


CREATE TABLE `beskida`.`uczestnicy_do_akceptacji` (
  `id` INT NOT NULL,
  `imie` VARCHAR(45) NULL,
  `nazwisko` VARCHAR(60) NULL,
  `pseudonim` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `beskida`.`role` (
  `id_roli` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(30) NOT NULL,
  `poziom_uprawnień` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_roli`),
  UNIQUE INDEX `nazwa_UNIQUE` (`nazwa` ASC) VISIBLE,
  UNIQUE INDEX `poziom_uprawnień_UNIQUE` (`poziom_uprawnień` ASC) VISIBLE);




CREATE TABLE `beskida`.`konta` (
  `id_konta` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `hasło` CHAR(50) NOT NULL,
  `rola_id` INT NOT NULL,
  PRIMARY KEY (`id_konta`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  UNIQUE INDEX `hasło_UNIQUE` (`hasło` ASC) VISIBLE);


CREATE TABLE `beskida`.`uczestnik_punkt` (
  `id_uczestnika` INT NOT NULL,
  `id_punktu` INT NOT NULL,
  `data` VARCHAR(45) NOT NULL);

CREATE TABLE `beskida`.`wolontariusz_punkt` (
  `id_wolontariusza` INT NOT NULL,
  `id_punktu` VARCHAR(45) NOT NULL);


CREATE TABLE `beskida`.`personel` (
  `id_osoby` INT NOT NULL AUTO_INCREMENT,
  `id_konta` INT NOT NULL,
  `imie` VARCHAR(45) NOT NULL,
  `nazwisko` VARCHAR(60) NOT NULL,
  `nr_telefonu` VARCHAR(9) NOT NULL,
  `mail` VARCHAR(50) NULL,
  PRIMARY KEY (`id_osoby`));


CREATE TABLE `beskida`.`punkty_online` (
  `id_punktu` INT NOT NULL,
  `ostatnia_aktywność` VARCHAR(45) NOT NULL);


CREATE DATABASE `baza_biegow_przelajowych`;
CREATE TABLE `baza_biegow_przelajowych`.`eventy` (
  `id_event` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(70) NOT NULL,
  `nazwa_bazy` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_event`));



ALTER TABLE `beskida`.`uczestnicy` 
ADD CONSTRAINT `id_konta_foreign`
  FOREIGN KEY (`id_konta`)
  REFERENCES `beskida`.`konta` (`id_konta`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `beskida`.`uczestnicy_do_akceptacji` 
ADD COLUMN `nr_telefonu` VARCHAR(9) NOT NULL AFTER `pseudonim`,
CHANGE COLUMN `id` `id` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `imie` `imie` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `nazwisko` `nazwisko` VARCHAR(60) NOT NULL ;

ALTER TABLE `beskida`.`role` 
DROP INDEX `poziom_uprawnień_UNIQUE` ;
;

ALTER TABLE `beskida`.`konta` 
ADD INDEX `rola_foreign_idx` (`rola_id` ASC) VISIBLE;
;
ALTER TABLE `beskida`.`konta` 
ADD CONSTRAINT `rola_foreign`
  FOREIGN KEY (`rola_id`)
  REFERENCES `beskida`.`role` (`id_roli`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;



ALTER TABLE `beskida`.`uczestnik_punkt` 
ADD INDEX `id_uczestnika_foreign_idx` (`id_uczestnika` ASC) VISIBLE,
ADD INDEX `id_punktu_foreign_idx` (`id_punktu` ASC) VISIBLE;
;
ALTER TABLE `beskida`.`uczestnik_punkt` 
ADD CONSTRAINT `id_uczestnika_foreign`
  FOREIGN KEY (`id_uczestnika`)
  REFERENCES `beskida`.`uczestnicy` (`nr_startowy`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `id_punktu_foreign`
  FOREIGN KEY (`id_punktu`)
  REFERENCES `beskida`.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `beskida`.`wolontariusz_punkt` 
CHANGE COLUMN `id_punktu` `id_punktu` INT NOT NULL ;


ALTER TABLE `beskida`.`wolontariusz_punkt` 
ADD INDEX `wolontariusz_foreign_idx` (`id_wolontariusza` ASC) VISIBLE,
ADD INDEX `punkt_foregin_idx` (`id_punktu` ASC) VISIBLE;
;
ALTER TABLE `beskida`.`wolontariusz_punkt` 
ADD CONSTRAINT `wolontariusz_foreign`
  FOREIGN KEY (`id_wolontariusza`)
  REFERENCES `beskida`.`personel` (`id_osoby`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `punkt_foregin`
  FOREIGN KEY (`id_punktu`)
  REFERENCES `beskida`.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `beskida`.`personel` 
ADD INDEX `konto_foreign_idx` (`id_konta` ASC) VISIBLE;
;
ALTER TABLE `beskida`.`personel` 
ADD CONSTRAINT `konto_foreign`
  FOREIGN KEY (`id_konta`)
  REFERENCES `beskida`.`konta` (`id_konta`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `beskida`.`punkty_online` 
ADD INDEX `punkt2_foreign_idx` (`id_punktu` ASC) VISIBLE;
;
ALTER TABLE `beskida`.`punkty_online` 
ADD CONSTRAINT `punkt2_foreign`
  FOREIGN KEY (`id_punktu`)
  REFERENCES `beskida`.`punkty_kontrolne` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `beskida`.`punkty_online` 
CHANGE COLUMN `ostatnia_aktywność` `data_ost_aktywności` VARCHAR(45) NOT NULL ;


use beskida;
DELIMITER $$
CREATE TRIGGER `after_deleting_participant` AFTER DELETE ON`uczestnicy` FOR EACH ROW BEGIN
	 #jeśli usuwamy uczestnika to powinniśmy usunąć jego konto
     #zakładam że uczestnika usuwamy przed rozpoczęciem biegu więc tabele takie jak uczestnik_punkt sa puste
     DELETE FROM konta WHERE konta.id_konta = OLD.id_konta;
END $$
DELIMITER ;