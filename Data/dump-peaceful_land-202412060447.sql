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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin tài khoản';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin về phương thức thanh toán';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ nhật ký cập nhật bài đăng. Bài đăng khi được chỉnh sửa thì phải thêm vào đây trước sau đó mới cập nhật bảng Posts.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_logs`
--

LOCK TABLES `post_logs` WRITE;
/*!40000 ALTER TABLE `post_logs` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ thông tin bài đăng mới nhất';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
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
  `category` enum('Nhà riêng','Nhà biệt thự, liền kề','Nhà mặt phố','Shophouse, nhà phố thương mại','Chung cư mini, căn hộ dịch vụ','Condotel','Đất nền dự án','Bán đất','Trang trại, khu nghỉ đưỡng','Kho, nhà xưởng','Bất động sản khác') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Nhà riêng' COMMENT 'Phân loại bất động sản',
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng cập nhật thông tin mới nhất về bất động sản.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `properties`
--

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng lưu trữ tập tin và đường dẫn';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_images`
--

LOCK TABLES `property_images` WRITE;
/*!40000 ALTER TABLE `property_images` DISABLE KEYS */;
INSERT INTO `property_images` VALUES (7,8,'property_imgs/f564d500-3b9b-448f-9dee-d0877abd6cc4_images.jpg');
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
  `action` enum('Đã bán','Đã cho thuê','Người mua bán lại','Hết hợp đồng cho thuê','Cập nhật giá','Thay đổi hình thức','Thay đổi hạn cho thuê','Giảm giá') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Hành động thay đổi',
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng ghi lại nhật ký cập nhật bất động sản, khi cập nhật cần thêm dữ liệu vào bảng này trước.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_logs`
--

LOCK TABLES `property_logs` WRITE;
/*!40000 ALTER TABLE `property_logs` DISABLE KEYS */;
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
  `action` enum('Mua gói môi giới','Mua gói môi giới VIP','Gia hạn gói','Gia hạn bài đăng') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hành động thanh toán',
  `meta` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `purchases_accounts_FK` (`user_id`),
  CONSTRAINT `purchases_accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng ghi lại những lần thanh toán trên trang web.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
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
  CONSTRAINT `contact_request_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về yêu cầu liên hệ.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_contact`
--

LOCK TABLES `requests_contact` WRITE;
/*!40000 ALTER TABLE `requests_contact` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu duyệt bài rao và bất động sản';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_post`
--

LOCK TABLES `requests_post` WRITE;
/*!40000 ALTER TABLE `requests_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `requests_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests_report`
--

DROP TABLE IF EXISTS `requests_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests_report` (
  `id` bigint NOT NULL COMMENT 'Id yêu cầu',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu báo cáo bài đăng không đúng nội dung';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_report`
--

LOCK TABLES `requests_report` WRITE;
/*!40000 ALTER TABLE `requests_report` DISABLE KEYS */;
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
  CONSTRAINT `request_tour_properties_FK` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin về yêu cầu tham quan bất động sản.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_tour`
--

LOCK TABLES `requests_tour` WRITE;
/*!40000 ALTER TABLE `requests_tour` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Bảng chứa thông tin yêu cầu rút tiền';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests_withdraw`
--

LOCK TABLES `requests_withdraw` WRITE;
/*!40000 ALTER TABLE `requests_withdraw` DISABLE KEYS */;
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
  `userId` bigint NOT NULL COMMENT 'Id người dùng',
  `money` bigint NOT NULL COMMENT 'Lượng tiền giao dịch',
  `transaction_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Nội dung giao dịch được người dùng gửi',
  `transaction_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Thông tin giao dịch, từ đâu, đến đâu, nguồn từ ví hay ngân hàng, id giao dịch',
  `status` tinyint NOT NULL COMMENT 'Thành công, Thất bại, Đang treo',
  `meta` text COLLATE utf8mb4_general_ci,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `transaction_account_FK` (`userId`),
  CONSTRAINT `transaction_account_FK` FOREIGN KEY (`userId`) REFERENCES `accounts` (`id`)
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
  `property_id` bigint DEFAULT NOT NULL COMMENT 'Id bất động sản',
  `interested` bit(1) NOT NULL COMMENT '0 - Không thích, 1 - Thích',
  `notification` bit(1) NOT NULL COMMENT 'Gửi thông báo về email khi có cập nhật (chỉ áp dụng với interested=1)',
  `meta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `hide` bit(1) DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `date_begin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_interests`
--

LOCK TABLES `user_interests` WRITE;
/*!40000 ALTER TABLE `user_interests` DISABLE KEYS */;
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

-- Dump completed on 2024-12-06  4:47:49
