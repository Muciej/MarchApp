CREATE TABLE punkty_kontrolne (
  id int IDENTITY(1,1) NOT NULL,
  online TINYINT NULL,
  nazwa VARCHAR(45) NULL,
  kilometr INT NULL,
  współrzędne_geograficzne VARCHAR(100) NULL,
  PRIMARY KEY (id));


CREATE TABLE uczestnicy (
  nr_startowy int IDENTITY(1,1) NOT NULL,
  id_konta INT NOT NULL,
  imie VARCHAR(60) NULL,
  nazwisko VARCHAR(60) NULL,
  pseudonim VARCHAR(45) NULL,
  kod_qr  VARCHAR(15) NULL,
  PRIMARY KEY (nr_startowy),
  CONSTRAINT id_konta_unique UNIQUE (id_konta));


CREATE TABLE uczestnicy_do_akceptacji (
  id INT IDENTITY(1,1) NOT NULL,
  imie VARCHAR(45) not NULL,
  nazwisko VARCHAR(60) not NULL,
  pseudonim VARCHAR(45) NULL,
  nr_telefonu VARCHAR(9) NOT NULL,
  PRIMARY KEY (id));


CREATE TABLE role (
  id_roli int IDENTITY(1,1) NOT NULL,
  nazwa VARCHAR(30) NOT NULL,
  poziom_uprawnień VARCHAR(30) NOT NULL,
  PRIMARY KEY (id_roli));

create INDEX index_nazwa ON role (nazwa ASC);

CREATE TABLE konta (
  id_konta int IDENTITY(1,1) NOT NULL,
  login VARCHAR(50) NOT NULL,
  hasło CHAR(100) NOT NULL,
  rola_id INT NOT NULL,
  PRIMARY KEY (id_konta));

create index login_unique on konta (login asc);

CREATE TABLE uczestnik_punkt (
  id_uczestnika INT NOT NULL,
  id_punktu INT NOT NULL,
  data DATETIME NOT NULL);

CREATE TABLE wolontariusz_punkt (
  id_wolontariusza INT NOT NULL,
  id_punktu INT NOT NULL);


CREATE TABLE personel (
  id_osoby int IDENTITY(1,1) NOT NULL,
  id_konta INT NOT NULL,
  imie VARCHAR(45) NOT NULL,
  nazwisko VARCHAR(60) NOT NULL,
  nr_telefonu VARCHAR(9) NOT NULL,
  mail VARCHAR(50) NULL,
  PRIMARY KEY (id_osoby));


CREATE TABLE punkty_online (
  id_punktu INT NOT NULL,
  data_ost_aktywności VARCHAR(45) NOT NULL);

ALTER TABLE uczestnicy
ADD CONSTRAINT id_konta_foreign
  FOREIGN KEY (id_konta)
  REFERENCES konta (id_konta)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

create INDEX rola_foreign_idx on konta (rola_id ASC);

ALTER TABLE konta
ADD CONSTRAINT rola_foreign
  FOREIGN KEY (rola_id)
  REFERENCES role (id_roli)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;



create INDEX id_punktu_foreign_idx on uczestnik_punkt (id_punktu ASC);
create INDEX id_uczestnika_foreign_idx on uczestnik_punkt (id_uczestnika ASC);

-- ALTER TABLE uczestnik_punkt
-- ADD CONSTRAINT id_uczestnika_foreign
--   FOREIGN KEY (id_uczestnika)
--   REFERENCES uczestnicy (nr_startowy)
--   ON DELETE NO ACTION
--   ON UPDATE NO ACTION,
-- ADD CONSTRAINT id_punktu_foreign
--   FOREIGN KEY (id_punktu)
--   REFERENCES punkty_kontrolne (id)
--   ON DELETE NO ACTION
--   ON UPDATE NO ACTION;


ALTER TABLE uczestnik_punkt
ADD CONSTRAINT FK_uczestnik_punkt_uczestnicy
  FOREIGN KEY (id_uczestnika)
  REFERENCES uczestnicy (nr_startowy)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

alter table uczestnik_punkt
ADD CONSTRAINT FK_uczestnik_punkt_punkty_kontrolne
  FOREIGN KEY (id_punktu)
  REFERENCES punkty_kontrolne (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

create INDEX wolontariusz_foreign_idx on wolontariusz_punkt (id_wolontariusza ASC);
create INDEX punkt_foregin_idx on wolontariusz_punkt (id_punktu ASC);

ALTER TABLE wolontariusz_punkt
ADD CONSTRAINT wolontariusz_foreign
  FOREIGN KEY (id_wolontariusza)
  REFERENCES personel (id_osoby)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

alter table wolontariusz_punkt
ADD CONSTRAINT punkt_foregin
  FOREIGN KEY (id_punktu)
  REFERENCES punkty_kontrolne (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


create INDEX konto_foreign_idx on personel (id_konta ASC);

ALTER TABLE personel
ADD CONSTRAINT konto_foreign
  FOREIGN KEY (id_konta)
  REFERENCES konta (id_konta)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

create INDEX punkt2_foreign_idx on punkty_online(id_punktu ASC);
ALTER TABLE punkty_online
ADD CONSTRAINT punkt2_foreign
  FOREIGN KEY (id_punktu)
  REFERENCES punkty_kontrolne (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


-- # DELIMITER $$
-- # CREATE TRIGGER after_deleting_participant AFTER DELETE ONuczestnicy FOR EACH ROW BEGIN
-- # 	 #jeśli usuwamy uczestnika to powinniśmy usunąć jego konto
-- #      #zakładam że uczestnika usuwamy przed rozpoczęciem biegu więc tabele takie jak uczestnik_punkt sa puste
-- #      DELETE FROM konta WHERE konta.id_konta = OLD.id_konta;
-- # END $$
-- # DELIMITER ;

-- insert into baza_biegow_przelajowych.eventy(nazwa, nazwa_bazy, rozpoczete) value
--     ("test_event", "ev_test_event", true);

-- #tworzenie konta administratora dla danej bazy
-- drop user if exists admin_ev_test_event@;
-- create user 'admin_ev_test_event'@ identified by 'admin_ev_test_event';
-- grant all privileges on ev_test_event.* to 'admin_ev_test_event'@;


create view czas_uczestnicy_punkt as
select * from punkty_kontrolne p
        inner join uczestnik_punkt up on p.id = up.id_punktu
        inner join uczestnicy u on up.id_uczestnika = u.nr_startowy;

create view wolontariusze_info_view as
select p.id_osoby, p.id_konta, wp.id_wolontariusza, wp.id_punktu, p.imie, p.nazwisko, p.nr_telefonu, p.mail, k.login, k.hasło
from personel p
inner join konta k on p.id_konta = k.id_konta
left join wolontariusz_punkt wp on p.id_osoby = wp.id_wolontariusza;

CREATE TRIGGER trg_DeleteOtherTable
ON punkty_kontrolne
AFTER DELETE
AS
BEGIN
    DELETE FROM punkty_online
    WHERE punkty_online.id_punktu IN (SELECT id FROM deleted);
END;

CREATE TRIGGER trg_DeleteFromWolonPunkt
    ON punkty_kontrolne
    after delete
    as
begin
    delete from wolontariusz_punkt
    where wolontariusz_punkt.id_punktu in (select id from deleted)
end;