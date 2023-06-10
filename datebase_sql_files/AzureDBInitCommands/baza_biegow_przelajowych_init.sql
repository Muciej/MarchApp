CREATE TABLE events (
  event_id int IDENTITY(1,1) NOT NULL,
  name VARCHAR(70) NOT NULL,
  db_name VARCHAR(45) NOT NULL,
  is_started bit NOT NULL,
  PRIMARY KEY (event_id));

drop table if exists info;
CREATE TABLE info(
    id_event INT NOT NULL,
    data_rozpoczęcia date,
    godzina_rozp time,
    data_zakonczenia date,
    godzina_zakonczenia time,
    lokalizacja VARCHAR(20),
    PRIMARY KEY (id_event));