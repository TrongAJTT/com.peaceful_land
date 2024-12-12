-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: peaceful_land
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id tài khoản',
  `role` tinyint NOT NULL COMMENT 'Vai trò người dùng: 0 - Thông thường, 1- Môi giới, 2 - Môi giới VIP, 3 - Admin',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Email tài khoản',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Mật khẩu',
  `account_balance` bigint NOT NULL COMMENT 'Số dư trong tài khoản',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Tên tài khoản',
  `birth_date` date DEFAULT NULL COMMENT 'Ngày tháng năm sinh',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Số điện thoại',
  `avatar_url` text COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Đường dẫn ảnh đại diện',
  `status` bit(1) NOT NULL COMMENT 'Tình trạng tài khoản: 1 - hoạt động, 0 - đã khóa',
  `role_expiration` date DEFAULT NULL COMMENT 'Ngày hết hạn vai trò',
  `meta` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_unique_phone` (`phone`),
  UNIQUE KEY `account_unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin tài khoản';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (4,3,'dangvantrong2004@example.com','$2a$12$rMIJ69GrI/yG5K.Lrsksaebbg9OpCUSJW0DvvGeJAZStj64Hbp2d.',354000,'Bành Văn Trọng','2004-03-15','0736284923','avatars/216d3c2c-f9bb-4d51-9b1f-e9226d825601.jpg',_binary '','2025-04-10','',_binary '\0',0,'2024-12-06 03:49:43'),(6,0,'tranthanhmai@example.com','$2a$12$AjLfUnBhCrCeiho4OGcaV.sgyJZqI4aKYWkNOnaqQO3jTdg8HqZOG',300000,'Trần Thanh Mai','2004-10-04','0123123125','template/blank_avatar.webp',_binary '','9999-12-31','',_binary '\0',0,'2024-12-08 07:06:11'),(8,0,'lamnhathao@example.com','$2a$12$i6XsCltlfNwPwh3exP1axOL64oqlsWBmcYIBmbHYiIzY/d/OqYU/y',200000,'Lâm Nhật Hào','2004-10-04','0123123129','template/blank_avatar.webp',_binary '','9999-12-31','',_binary '\0',0,'2024-12-08 16:14:07');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id đợt giảm giá',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `original_price` bigint NOT NULL COMMENT 'Giá gốc trước khi giảm',
  `discount_price` bigint DEFAULT NULL COMMENT 'Mức giá giảm xuống',
  `expiration` datetime DEFAULT NULL COMMENT 'Thời gian hết hạn đợt giảm giá',
  `meta` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `discounts_properties_FK` (`property_id`),
  CONSTRAINT `discounts_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về những đợt giảm giá trong một khoảng thời gian ngắn hạn nhằm kích cầu thị trường.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_methods` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id phương thức',
  `user_id` bigint NOT NULL COMMENT 'Id người dùng',
  `is_wallet` bit(1) NOT NULL COMMENT '0 - Ví điện tử, 1 - Ngân hàng',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Tên ví / ngân hàng',
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Số tài khoản',
  `meta` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_methods_accounts_FK` (`user_id`),
  CONSTRAINT `payment_methods_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin về phương thức thanh toán';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods` DISABLE KEYS */;
INSERT INTO `payment_methods` VALUES (1,4,_binary '\0','MB Bank','123234345','',_binary '\0',0,'2024-12-06 08:30:12'),(4,4,_binary '\0','Agribank','123234345','',_binary '\0',0,'2024-12-06 08:47:10');
/*!40000 ALTER TABLE `payment_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_logs`
--

DROP TABLE IF EXISTS `post_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id nhật ký bài rao',
  `post_id` bigint NOT NULL COMMENT 'Id bài rao',
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tiêu đề bài rao',
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Trạng thái: 1 - còn hạn , 0 - hết hạn',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mô tả bài rao',
  `expiration` date NOT NULL COMMENT 'Ngày hết hạn rao bài',
  `thumbn_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `post_logs_posts_FK` (`post_id`),
  CONSTRAINT `post_logs_posts_FK` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ nhật ký cập nhật bài đăng. Bài đăng khi được chỉnh sửa thì phải thêm vào đây trước sau đó mới cập nhật bảng Posts.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_logs`
--

LOCK TABLES `post_logs` WRITE;
/*!40000 ALTER TABLE `post_logs` DISABLE KEYS */;
INSERT INTO `post_logs` VALUES (3,3,'Chủ cần bán gấp lô đất ở An Tịnh, Trảng Bàng 220m2 full TC giá chỉ 470tr',_binary '','Bán lô đất 220m tại đường Bầu Suối Sâu, An Tịnh, Trảng Bàng chỉ 470 triệu.','2024-12-16','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-06 03:53:53'),(4,3,'Tên bài đăng mới',_binary '','Mô tả mới','2024-12-16','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-08 10:09:23'),(5,3,'Tên bài đăng mới',_binary '','Mô tả mới','2024-12-16','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-08 10:13:50'),(6,4,'Đất SHR ngay chợ Suối Sâu An Tịnh 250m2 ngang 5m dài 50m, giá 530 triệu',_binary '','Tôi cần bán lô đất ngay chơ đêm Trảng Bàng Tây Ninh. 250m² sổ hồng, thổ cư 100%. Vị trí đẹp ngay chợ, KCN Trảng Bàng, từ lộ 22 vào khoảng 200m, rất thích hợp cho anh chị mình mua để ở, đâu tư, kinh doanh buôn bán.','2024-12-18','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-08 13:56:25'),(7,5,'Đất SHR ngay chợ Suối Sâu An Tịnh 250m2 ngang 5m dài 50m, giá 530 triệu',_binary '','Tôi cần bán lô đất ngay chơ đêm Trảng Bàng Tây Ninh. 250m² sổ hồng, thổ cư 100%. Vị trí đẹp ngay chợ, KCN Trảng Bàng, từ lộ 22 vào khoảng 200m, rất thích hợp cho anh chị mình mua để ở, đâu tư, kinh doanh buôn bán.','2024-12-19','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-08 19:38:26'),(8,3,'Tên bài đăng mới',_binary '','Mô tả mới','2024-12-23','post_thumbns/527x584.jpg','',_binary '\0',0,'2024-12-09 20:52:07'),(9,3,'Tên bài đăng mới',_binary '','Mô tả mới','2025-01-06','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-09 21:04:06'),(10,6,'Căn hộ cao cấp 3 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','2024-12-23','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-09 21:13:46'),(11,7,'Căn hộ cao cấp 2 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','2024-12-26','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-12 02:47:00'),(12,8,'Căn hộ cao cấp 15 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','2024-12-26','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-12 03:06:09'),(13,9,'Căn hộ cao cấp 16 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','2024-12-26','template/image_state_not_available.jpg','',_binary '\0',0,'2024-12-12 04:30:18');
/*!40000 ALTER TABLE `post_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id của bài rao',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `title` varchar(150) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tiêu đề bài rao',
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Trạng thái: 1 - còn hạn , 0 - hết hạn',
  `description` longtext COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mô tả bài rao',
  `thumbn_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Đường dẫn lưu thumbnail',
  `expiration` date NOT NULL COMMENT 'Ngày hết hạn rao bài',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `posts_properties_FK` (`property_id`),
  CONSTRAINT `posts_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin bài đăng mới nhất';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (3,9,'Tên bài đăng mới',_binary '','Mô tả mới','template/image_state_not_available.jpg','2025-01-06','',_binary '',0,'2024-12-06 03:53:53'),(4,10,'Đất SHR ngay chợ Suối Sâu An Tịnh 250m2 ngang 5m dài 50m, giá 530 triệu',_binary '','Tôi cần bán lô đất ngay chơ đêm Trảng Bàng Tây Ninh. 250m² sổ hồng, thổ cư 100%. Vị trí đẹp ngay chợ, KCN Trảng Bàng, từ lộ 22 vào khoảng 200m, rất thích hợp cho anh chị mình mua để ở, đâu tư, kinh doanh buôn bán.','post_thumbns/52200007_DangVanTrong_Screenshot_2024-11-04_183016.png','2024-12-18','',_binary '',0,'2024-12-08 13:56:25'),(5,13,'Đất SHR ngay chợ Suối Sâu An Tịnh 250m2 ngang 5m dài 50m, giá 530 triệu',_binary '','Tôi cần bán lô đất ngay chơ đêm Trảng Bàng Tây Ninh. 250m² sổ hồng, thổ cư 100%. Vị trí đẹp ngay chợ, KCN Trảng Bàng, từ lộ 22 vào khoảng 200m, rất thích hợp cho anh chị mình mua để ở, đâu tư, kinh doanh buôn bán.','post_thumbns/527x585.jpg','2024-12-19','',_binary '\0',0,'2024-12-08 19:38:26'),(6,20,'Căn hộ cao cấp 3 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','template/image_state_not_available.jpg','2024-12-23','',_binary '',0,'2024-12-09 21:13:46'),(7,14,'Căn hộ cao cấp 2 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','template/image_state_not_available.jpg','2024-12-26','',_binary '\0',0,'2024-12-12 02:47:00'),(8,15,'Căn hộ cao cấp 15 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','template/image_state_not_available.jpg','2024-12-26','',_binary '\0',0,'2024-12-12 03:06:09'),(9,16,'Căn hộ cao cấp 16 phòng ngủ, nội thất đầy đủ',_binary '','Căn hộ nằm tại vị trí đắc địa, gần các tiện ích như trường học, bệnh viện, trung tâm thương mại.','template/image_state_not_available.jpg','2024-12-26','',_binary '\0',0,'2024-12-12 04:30:18');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `properties` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id của bất động sản',
  `user_id` bigint NOT NULL COMMENT 'Id người đăng bất động sản',
  `offer` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Hình thức: 0 - Mua bán, 1 - Cho thuê',
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Trạng thái: 0 - Đã bán hoặc cho thuê, 1 - sẵn sàng',
  `rental_period` date DEFAULT NULL COMMENT 'Hạn cho thuê (bắt buộc nếu type=1)',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Vị trí (tỉnh, quận/huyện. phường/xã)',
  `location_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Vị trí (số nhà, đường)',
  `map_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Địa chỉ trên bản đồ số',
  `category` enum('Nhà riêng','Nhà biệt thự, liền kề','Nhà mặt phố','Shophouse, nhà phố thương mại','Chung cư mini, căn hộ dịch vụ','Condotel','Đất nền dự án','Bán đất','Trang trại, khu nghỉ dưỡng','Kho, nhà xưởng','Bất động sản khác') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Nhà riêng' COMMENT 'Phân loại bất động sản',
  `price` bigint NOT NULL COMMENT 'Giá tiền',
  `area` int NOT NULL COMMENT 'Diện tích bất động sản (mét vuông)',
  `legal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Giấy tờ pháp lý',
  `bedrooms` tinyint DEFAULT NULL COMMENT 'Số lượng phòng ngủ',
  `toilets` tinyint DEFAULT NULL COMMENT 'Số lượng nhà vệ sinh',
  `entrance` tinyint DEFAULT NULL COMMENT 'Lối vào',
  `frontage` tinyint DEFAULT NULL COMMENT 'Mặt tiền',
  `house_orientation` enum('Bắc','Đông Bắc','Đông','Đông Nam','Nam','Tây Nam','Tây','Tây Bắc') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Hướng nhà',
  `balcony_orientation` enum('Bắc','Đông Bắc','Đông','Đông Nam','Nam','Tây Nam','Tây','Tây Bắc') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Hướng ban công',
  `hide` bit(1) DEFAULT NULL,
  `meta` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `properties_account_FK` (`user_id`),
  CONSTRAINT `properties_account_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng cập nhật thông tin mới nhất về bất động sản.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `properties`
--

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
INSERT INTO `properties` VALUES (9,4,_binary '',_binary '','2025-04-05','Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',998000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '','',0,'2024-12-06 03:51:00'),(10,4,_binary '\0',_binary '',NULL,'Hà Nội, Quận Hoàn Kiếm, Phố Cầu Gỗ','23, Phố Cầu Gỗ','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,'Bắc',_binary '','',0,'2024-12-08 13:05:44'),(13,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '\0','',0,'2024-12-08 19:38:07'),(14,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '\0','',0,'2024-12-09 04:23:36'),(15,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '\0','',0,'2024-12-09 04:27:14'),(16,4,_binary '',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',NULL,NULL,NULL,NULL,NULL,'Đông Bắc',_binary '\0',NULL,NULL,NULL),(17,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,'Đông','Đông',_binary '','',0,'2024-12-09 05:02:25'),(18,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 7, Phường Tân Phong','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '','',0,'2024-12-09 05:07:09'),(19,4,_binary '\0',_binary '',NULL,'Thành phố Hồ Chí Minh, Quận 8, Phường Tân Hưng','12, Huỳnh Tấn Phát','Google Map Url Example','Nhà riêng',230000000,40,'Sổ hồng',2,2,0,0,NULL,NULL,_binary '','',0,'2024-12-09 21:11:18'),(20,6,_binary '',_binary '','2025-12-24','Hà Nội, Quận Cầu Giấy, Phường Dịch Vọng','Tòa nhà Golden Land, số 2 Nguyễn Văn Huyên','https://goo.gl/maps/12345','Nhà riêng',15000000,70,'Sổ hồng',3,2,2,0,'Đông Nam','Tây Bắc',_binary '','',0,'2024-12-09 21:13:02');
/*!40000 ALTER TABLE `properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_images`
--

DROP TABLE IF EXISTS `property_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_images` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id tập tin',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Đường dẫn lưu trữ',
  PRIMARY KEY (`id`),
  KEY `property_images_properties_FK` (`property_id`),
  CONSTRAINT `property_images_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ tập tin và đường dẫn';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_images`
--

LOCK TABLES `property_images` WRITE;
/*!40000 ALTER TABLE `property_images` DISABLE KEYS */;
INSERT INTO `property_images` VALUES (23,13,'property_imgs/616a41f1-f804-425a-9c56-71a711cb9b87_Screenshot 2024-12-09 094836.png'),(27,14,'property_imgs/124124.jpg');
/*!40000 ALTER TABLE `property_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_logs`
--

DROP TABLE IF EXISTS `property_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id nhật ký',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `action` enum('Đã bán','Đã cho thuê','Người mua bán lại','Hết hợp đồng, cho thuê lại','Cập nhật giá','Thay đổi hình thức','Thay đổi hạn cho thuê','Giảm giá','Cập nhật tin rao') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Hành động thay đổi',
  `offer` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Hình thức: 0 - Mua bán, 1 - Cho thuê',
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Trạng thái: 0 - Đã bán hoặc cho thuê, 1 - sẵn sàng',
  `rental_period` date DEFAULT NULL COMMENT 'Hạn cho thuê (bắt buộc nếu type=1)',
  `price` bigint NOT NULL COMMENT 'Giá tiền',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `property_logs_properties_FK` (`property_id`),
  CONSTRAINT `property_logs_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng ghi lại nhật ký cập nhật bất động sản, khi cập nhật cần thêm dữ liệu vào bảng này trước.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_logs`
--

LOCK TABLES `property_logs` WRITE;
/*!40000 ALTER TABLE `property_logs` DISABLE KEYS */;
INSERT INTO `property_logs` VALUES (8,9,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-06 03:51:00'),(12,9,'Đã bán',_binary '\0',_binary '\0',NULL,230000000,'',_binary '\0',0,'2024-12-08 08:16:06'),(13,9,'Đã cho thuê',_binary '\0',_binary '\0',NULL,230000000,'',_binary '\0',0,'2024-12-08 08:22:51'),(14,9,'Người mua bán lại',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-08 08:24:21'),(15,9,'Hết hợp đồng, cho thuê lại',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-08 08:24:50'),(16,9,'Hết hợp đồng, cho thuê lại',_binary '\0',_binary '',NULL,420000000,'',_binary '\0',0,'2024-12-08 08:28:46'),(17,9,'Hết hợp đồng, cho thuê lại',_binary '\0',_binary '',NULL,420000000,'',_binary '\0',0,'2024-12-08 08:29:07'),(18,9,'Hết hợp đồng, cho thuê lại',_binary '\0',_binary '',NULL,420000000,'',_binary '\0',0,'2024-12-08 08:30:09'),(19,9,'Cập nhật giá',_binary '\0',_binary '',NULL,430000000,'',_binary '\0',0,'2024-12-08 08:37:02'),(20,9,'Thay đổi hình thức',_binary '',_binary '',NULL,430000000,'',_binary '\0',0,'2024-12-08 08:37:54'),(21,9,'Cập nhật giá',_binary '',_binary '',NULL,430000000,'',_binary '\0',0,'2024-12-08 08:44:51'),(22,10,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-08 13:05:44'),(25,13,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-08 19:38:07'),(26,14,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-09 04:23:36'),(27,15,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-09 04:27:14'),(28,17,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-09 05:02:25'),(29,18,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-09 05:07:09'),(30,9,'Đã cho thuê',_binary '',_binary '\0',NULL,430000000,'',_binary '\0',0,'2024-12-09 20:16:13'),(31,19,'Cập nhật giá',_binary '\0',_binary '',NULL,230000000,'',_binary '\0',0,'2024-12-09 21:11:18'),(32,20,'Cập nhật giá',_binary '',_binary '','2025-12-24',15000000,'',_binary '\0',0,'2024-12-09 21:13:02'),(33,9,'Đã cho thuê',_binary '',_binary '\0',NULL,430000000,'',_binary '\0',0,'2024-12-10 13:38:00'),(34,9,'Thay đổi hạn cho thuê',_binary '',_binary '\0','2025-04-05',430000000,'',_binary '\0',0,'2024-12-10 13:46:25'),(35,9,'Hết hợp đồng, cho thuê lại',_binary '',_binary '','2025-04-05',998000000,'',_binary '\0',0,'2024-12-12 03:54:59');
/*!40000 ALTER TABLE `property_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id thanh toán',
  `user_id` bigint NOT NULL COMMENT 'Id người dùng',
  `amount` bigint NOT NULL COMMENT 'Lượng tiền giao dịch',
  `action` enum('Mua gói môi giới','Mua gói môi giới VIP','Gia hạn gói','Gia hạn bài đăng','Rút tiền') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hành động thanh toán',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `purchases_accounts_FK` (`user_id`),
  CONSTRAINT `purchases_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng ghi lại những lần thanh toán trên trang web.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (3,4,154000,'Mua gói môi giới VIP','',_binary '\0',0,'2024-12-06 03:50:17'),(4,4,154000,'Mua gói môi giới VIP','',_binary '\0',0,'2024-12-08 09:23:58'),(5,4,100000,'Rút tiền','',_binary '\0',0,'2024-12-09 10:45:34'),(6,4,28000,'Gia hạn bài đăng','',_binary '\0',0,'2024-12-09 21:04:06'),(7,4,99000,'Mua gói môi giới','',_binary '\0',0,'2024-12-11 22:33:19'),(8,4,99000,'Gia hạn gói','',_binary '\0',0,'2024-12-11 22:33:58'),(9,4,99000,'Gia hạn gói','',_binary '\0',0,'2024-12-12 02:18:27'),(10,4,99000,'Gia hạn gói','',_binary '\0',0,'2024-12-12 02:18:52');
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_contact`
--

DROP TABLE IF EXISTS `requests_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_contact` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id yêu cầu',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `user_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tên người yêu cầu',
  `phone` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Số điện thoại',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Địa chỉ email',
  `interest_level` enum('Tôi muốn biết thêm thông tin','Tôi muốn mua / thuê bất động sản này') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mức độ quan tâm',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Tin nhắn',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contact_request_properties_FK` (`property_id`),
  KEY `requests_contact_accounts_FK` (`user_id`),
  CONSTRAINT `contact_request_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`),
  CONSTRAINT `requests_contact_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về yêu cầu liên hệ.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_contact`
--

LOCK TABLES `requests_contact` WRITE;
/*!40000 ALTER TABLE `requests_contact` DISABLE KEYS */;
INSERT INTO `requests_contact` VALUES (3,9,6,'Đặng Trần Anh Quân','0123345139','anhkhoi123@example.com','Tôi muốn biết thêm thông tin','Tôi muốn được liên hệ để biết thêm thông tin về bất dộng sản này. Tôi rất ưng nó. Tiền bạc không thành vấn đề, hãy ưu tiên tôi trước.','',_binary '\0',0,'2024-12-11 01:39:25');
/*!40000 ALTER TABLE `requests_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_post`
--

DROP TABLE IF EXISTS `requests_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id yêu cầu duyệt',
  `post_id` bigint NOT NULL COMMENT 'Id bài rao',
  `expiration` date NOT NULL COMMENT 'Ngày hết hạn yêu cầu',
  `approved` bit(1) DEFAULT NULL COMMENT 'Trạng thái duyệt',
  `deny_message` text COLLATE utf8mb4_general_ci COMMENT 'Lý do bài đăng không được duyệt',
  `meta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `requests_post_posts_FK` (`post_id`),
  CONSTRAINT `requests_post_posts_FK` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu duyệt bài rao và bất động sản';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_post`
--

LOCK TABLES `requests_post` WRITE;
/*!40000 ALTER TABLE `requests_post` DISABLE KEYS */;
INSERT INTO `requests_post` VALUES (9,4,'2024-12-09',_binary '',NULL,'',_binary '\0',0,'2024-12-08 13:57:38'),(10,3,'2024-12-09',_binary '\0','Hết hạn yêu cầu nhưng quản trị viên vẫn chưa xử lý','',_binary '\0',0,'2024-12-08 15:43:57'),(11,5,'2024-12-11',_binary '\0','Hết hạn yêu cầu nhưng quản trị viên vẫn chưa xử lý','',_binary '\0',0,'2024-12-08 19:39:56'),(12,6,'2024-12-13',NULL,NULL,'',_binary '\0',0,'2024-12-11 21:15:30'),(13,6,'2024-12-13',NULL,NULL,'',_binary '\0',0,'2024-12-11 22:20:07'),(14,7,'2024-12-13',_binary '',NULL,'',_binary '\0',0,'2024-12-12 02:49:03'),(15,8,'2024-12-13',_binary '',NULL,'',_binary '\0',0,'2024-12-12 03:06:22'),(16,9,'2024-12-13',_binary '',NULL,'',_binary '\0',0,'2024-12-12 04:33:03');
/*!40000 ALTER TABLE `requests_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_report`
--

DROP TABLE IF EXISTS `requests_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id yêu cầu',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `object` enum('Tin rao','Bản đồ') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Tin rao' COMMENT 'Đối tượng báo cáo',
  `reasons` text COLLATE utf8mb4_general_ci COMMENT 'Lý do báo cáo (Các checkbox)',
  `description` text COLLATE utf8mb4_general_ci COMMENT 'Lý do khác hoặc phản hồi khác',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tên người yêu cầu',
  `phone` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Số điện thoại',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Địa chỉ email',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `request_report_properties_FK` (`property_id`),
  CONSTRAINT `request_report_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu báo cáo bài đăng không đúng nội dung';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_report`
--

LOCK TABLES `requests_report` WRITE;
/*!40000 ALTER TABLE `requests_report` DISABLE KEYS */;
INSERT INTO `requests_report` VALUES (1,9,'Tin rao','Bài rao không đúng sự thật','Nhà tôi gần khu vực này, tôi đã đến địa điểm xem nhưng mà căn nhà chỗ này khác hoàn toàn.','Đặng Trần Trung Anh','0832839572','trantrunganh23@example.com','',_binary '',0,'2024-12-09 19:58:11');
/*!40000 ALTER TABLE `requests_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_tour`
--

DROP TABLE IF EXISTS `requests_tour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_tour` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id yêu cầu',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `user_id` bigint NOT NULL,
  `tour_type` enum('Xem trực tiếp','Trò chuyện video online') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Loại hình tham quan',
  `expected_time` datetime NOT NULL COMMENT 'Thời gian mong muốn',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tên người yêu cầu',
  `phone` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Số điện thoại',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Địa chỉ email',
  `interest_level` enum('Tôi muốn biết thêm thông tin','Tôi muốn mua / thuê bất động sản này') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mức độ quan tâm',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `request_tour_properties_FK` (`property_id`),
  KEY `requests_tour_accounts_FK` (`user_id`),
  CONSTRAINT `request_tour_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`),
  CONSTRAINT `requests_tour_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về yêu cầu tham quan bất động sản.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_tour`
--

LOCK TABLES `requests_tour` WRITE;
/*!40000 ALTER TABLE `requests_tour` DISABLE KEYS */;
INSERT INTO `requests_tour` VALUES (12,9,6,'Xem trực tiếp','2024-12-12 11:00:00','Đặng Trần Anh Quân','0123345139','anhkhoi123@example.com','Tôi muốn mua / thuê bất động sản này','',_binary '\0',0,'2024-12-11 01:47:49');
/*!40000 ALTER TABLE `requests_tour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_withdraw`
--

DROP TABLE IF EXISTS `requests_withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_withdraw` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id yêu cầu',
  `user_id` bigint NOT NULL COMMENT 'Id người dùng',
  `id_pay_method` bigint NOT NULL COMMENT 'Id phương thức thanh toán',
  `amount` bigint NOT NULL COMMENT 'Lượng tiền muốn rút (bội số 50000)',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT 'Trạng thái: 0 - Đang chờ xử lý, 1 - Thành công, 2 - Thất bại',
  `result_message` text COLLATE utf8mb4_general_ci COMMENT 'Kết quả yêu cầu trả về',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `request_withdraw_accounts_FK` (`user_id`),
  KEY `request_withdraw_payment_methods_FK` (`id_pay_method`),
  CONSTRAINT `request_withdraw_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `request_withdraw_payment_methods_FK` FOREIGN KEY (`id_pay_method`) REFERENCES `payment_methods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu rút tiền';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_withdraw`
--

LOCK TABLES `requests_withdraw` WRITE;
/*!40000 ALTER TABLE `requests_withdraw` DISABLE KEYS */;
INSERT INTO `requests_withdraw` VALUES (9,6,4,100000,0,NULL,'',_binary '\0',0,'2024-12-09 07:51:45'),(10,4,4,100000,1,'','',_binary '\0',0,'2024-12-09 08:47:55'),(11,4,4,100000,0,NULL,'',_binary '\0',0,'2024-12-12 19:24:59');
/*!40000 ALTER TABLE `requests_withdraw` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id token',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Mã token',
  `token_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Loại token',
  `expiration_date` datetime DEFAULT NULL COMMENT 'Thời gian hết hạn',
  `revoked` bit(1) NOT NULL COMMENT 'Trạng thái thu hồi',
  `expired` bit(1) NOT NULL COMMENT 'Trạng thái còn hạn',
  `user_id` bigint DEFAULT NULL COMMENT 'Id người dùng',
  PRIMARY KEY (`id`),
  KEY `tokens_accounts_FK` (`user_id`),
  CONSTRAINT `tokens_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin Token, cần sửa lại sau khi triển khai Spring Security';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id giao dịch',
  `user_id` bigint NOT NULL COMMENT 'Id người dùng',
  `money` bigint NOT NULL COMMENT 'Lượng tiền giao dịch',
  `transaction_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Nội dung giao dịch được người dùng gửi',
  `transaction_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Thông tin giao dịch, từ đâu, đến đâu, nguồn từ ví hay ngân hàng, id giao dịch',
  `status` tinyint NOT NULL COMMENT 'Thành công, Thất bại, Đang treo',
  `meta` text COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `transaction_account_FK` (`user_id`),
  CONSTRAINT `transaction_account_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về các lần nạp tiền lên trang web.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_interests`
--

DROP TABLE IF EXISTS `user_interests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_interests` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_id` bigint NOT NULL COMMENT 'Id người dùng',
  `property_id` bigint NOT NULL COMMENT 'Id bất động sản',
  `interested` bit(1) NOT NULL COMMENT '0 - Không thích, 1 - Thích',
  `notification` bit(1) NOT NULL COMMENT 'Gửi thông báo về email khi có cập nhật (chỉ áp dụng với interested=1)',
  `meta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_interests`
--

LOCK TABLES `user_interests` WRITE;
/*!40000 ALTER TABLE `user_interests` DISABLE KEYS */;
INSERT INTO `user_interests` VALUES (9,4,9,_binary '\0',_binary '\0','',_binary '\0',0,'2024-12-11 01:53:39');
/*!40000 ALTER TABLE `user_interests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'peaceful_land'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-12 20:45:13
