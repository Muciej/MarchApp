DROP DATABASE IF EXISTS `baza_biegow_przelajowych`;
CREATE DATABASE `baza_biegow_przelajowych`;
CREATE TABLE `baza_biegow_przelajowych`.`eventy` (
  `id_event` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(70) NOT NULL,
  `nazwa_bazy` VARCHAR(45) NOT NULL,
  rozpoczete BOOLEAN NOT NULL,
  PRIMARY KEY (`id_event`));

drop table if exists baza_biegow_przelajowych.info;
CREATE TABLE `baza_biegow_przelajowych`.`info`(
    `id_event` INT NOT NULL,
    `data_rozpoczęcia` date,
    `godzina_rozp` time,
    `data_zakonczenia` date,
    `godzina_zakonczenia` time,
    `lokalizacja` VARCHAR(20),
    PRIMARY KEY ('id_event'));