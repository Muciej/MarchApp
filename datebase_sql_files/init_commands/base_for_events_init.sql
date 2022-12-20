CREATE DATABASE `baza_biegow_przelajowych`;
CREATE TABLE `baza_biegow_przelajowych`.`eventy` (
  `id_event` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(70) NOT NULL,
  `nazwa_bazy` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_event`));