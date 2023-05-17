-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: marchapp
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `konta`
--

DROP TABLE IF EXISTS `konta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `konta` (
  `id_konta` int NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `hasło` char(50) NOT NULL,
  `rola_id` int NOT NULL,
  PRIMARY KEY (`id_konta`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `hasło_UNIQUE` (`hasło`),
  KEY `rola_foreign_idx` (`rola_id`),
  CONSTRAINT `rola_foreign` FOREIGN KEY (`rola_id`) REFERENCES `role` (`id_roli`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `konta`
--

LOCK TABLES `konta` WRITE;
/*!40000 ALTER TABLE `konta` DISABLE KEYS */;
/*!40000 ALTER TABLE `konta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personel`
--

DROP TABLE IF EXISTS `personel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personel` (
  `id_osoby` int NOT NULL AUTO_INCREMENT,
  `id_konta` int NOT NULL,
  `imie` varchar(45) NOT NULL,
  `nazwisko` varchar(60) NOT NULL,
  `nr_telefonu` varchar(9) NOT NULL,
  `mail` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_osoby`),
  KEY `konto_foreign_idx` (`id_konta`),
  CONSTRAINT `konto_foreign` FOREIGN KEY (`id_konta`) REFERENCES `konta` (`id_konta`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personel`
--

LOCK TABLES `personel` WRITE;
/*!40000 ALTER TABLE `personel` DISABLE KEYS */;
/*!40000 ALTER TABLE `personel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `punkty_kontrolne`
--

DROP TABLE IF EXISTS `punkty_kontrolne`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `punkty_kontrolne` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kolejność` int NOT NULL,
  `online` tinyint DEFAULT NULL,
  `nazwa` varchar(45) DEFAULT NULL,
  `kilometr` int DEFAULT NULL,
  `współrzędne_geograficzne` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kolejność_UNIQUE` (`kolejność`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `punkty_kontrolne`
--

LOCK TABLES `punkty_kontrolne` WRITE;
/*!40000 ALTER TABLE `punkty_kontrolne` DISABLE KEYS */;
/*!40000 ALTER TABLE `punkty_kontrolne` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `punkty_online`
--

DROP TABLE IF EXISTS `punkty_online`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `punkty_online` (
  `id_punktu` int NOT NULL,
  `data_ost_aktywności` varchar(45) NOT NULL,
  KEY `punkt2_foreign_idx` (`id_punktu`),
  CONSTRAINT `punkt2_foreign` FOREIGN KEY (`id_punktu`) REFERENCES `punkty_kontrolne` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `punkty_online`
--

LOCK TABLES `punkty_online` WRITE;
/*!40000 ALTER TABLE `punkty_online` DISABLE KEYS */;
/*!40000 ALTER TABLE `punkty_online` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id_roli` int NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(30) NOT NULL,
  `poziom_uprawnień` varchar(30) NOT NULL,
  PRIMARY KEY (`id_roli`),
  UNIQUE KEY `nazwa_UNIQUE` (`nazwa`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uczestnicy`
--

DROP TABLE IF EXISTS `uczestnicy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uczestnicy` (
  `nr_startowy` int NOT NULL,
  `id_konta` int NOT NULL,
  `imie` varchar(60) DEFAULT NULL,
  `nazwisko` varchar(60) DEFAULT NULL,
  `pseudonim` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`nr_startowy`),
  UNIQUE KEY `id_konta_UNIQUE` (`id_konta`),
  CONSTRAINT `id_konta_foreign` FOREIGN KEY (`id_konta`) REFERENCES `konta` (`id_konta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uczestnicy`
--

LOCK TABLES `uczestnicy` WRITE;
/*!40000 ALTER TABLE `uczestnicy` DISABLE KEYS */;
/*!40000 ALTER TABLE `uczestnicy` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_deleting_participant` AFTER DELETE ON `uczestnicy` FOR EACH ROW BEGIN
	 #jeśli usuwamy uczestnika to powinniśmy usunąć jego konto
     #zakładam że uczestnika usuwamy przed rozpoczęciem biegu więc tabele takie jak uczestnik_punkt sa puste
     DELETE FROM konta WHERE konta.id_konta = OLD.id_konta;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `uczestnicy_do_akceptacji`
--

DROP TABLE IF EXISTS `uczestnicy_do_akceptacji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uczestnicy_do_akceptacji` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imie` varchar(45) NOT NULL,
  `nazwisko` varchar(60) NOT NULL,
  `pseudonim` varchar(45) DEFAULT NULL,
  `nr_telefonu` varchar(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uczestnicy_do_akceptacji`
--

LOCK TABLES `uczestnicy_do_akceptacji` WRITE;
/*!40000 ALTER TABLE `uczestnicy_do_akceptacji` DISABLE KEYS */;
/*!40000 ALTER TABLE `uczestnicy_do_akceptacji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uczestnik_punkt`
--

DROP TABLE IF EXISTS `uczestnik_punkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uczestnik_punkt` (
  `id_uczestnika` int NOT NULL,
  `id_punktu` int NOT NULL,
  `data` varchar(45) NOT NULL,
  KEY `id_uczestnika_foreign_idx` (`id_uczestnika`),
  KEY `id_punktu_foreign_idx` (`id_punktu`),
  CONSTRAINT `id_punktu_foreign` FOREIGN KEY (`id_punktu`) REFERENCES `punkty_kontrolne` (`id`),
  CONSTRAINT `id_uczestnika_foreign` FOREIGN KEY (`id_uczestnika`) REFERENCES `uczestnicy` (`nr_startowy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uczestnik_punkt`
--

LOCK TABLES `uczestnik_punkt` WRITE;
/*!40000 ALTER TABLE `uczestnik_punkt` DISABLE KEYS */;
/*!40000 ALTER TABLE `uczestnik_punkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wolontariusz_punkt`
--

DROP TABLE IF EXISTS `wolontariusz_punkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wolontariusz_punkt` (
  `id_wolontariusza` int NOT NULL,
  `id_punktu` int NOT NULL,
  KEY `wolontariusz_foreign_idx` (`id_wolontariusza`),
  KEY `punkt_foregin_idx` (`id_punktu`),
  CONSTRAINT `punkt_foregin` FOREIGN KEY (`id_punktu`) REFERENCES `punkty_kontrolne` (`id`),
  CONSTRAINT `wolontariusz_foreign` FOREIGN KEY (`id_wolontariusza`) REFERENCES `personel` (`id_osoby`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wolontariusz_punkt`
--

LOCK TABLES `wolontariusz_punkt` WRITE;
/*!40000 ALTER TABLE `wolontariusz_punkt` DISABLE KEYS */;
/*!40000 ALTER TABLE `wolontariusz_punkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'marchapp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-22 10:48:44
