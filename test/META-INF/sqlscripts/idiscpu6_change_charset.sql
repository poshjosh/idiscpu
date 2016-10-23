# https://mathiasbynens.be/notes/mysql-utf8mb4
# For each database:
ALTER DATABASE `loosebox_idisc` CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci;

# For each table:
ALTER TABLE `archivedfeed` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `bookmarkfeed` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `comment` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `country` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `emailstatus` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `extractedemail` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `favoritefeed` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feedhit` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `gender` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `howdidyoufindus` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

# For each index, 255 3-bit chars (under utf8) becomes 191 4-bit chars (under utf8mb4)
ALTER TABLE `installation` MODIFY COLUMN `installationkey` VARCHAR(191) NOT NULL UNIQUE;
ALTER TABLE `installation` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

# For each index, 255 3-bit chars (under utf8) becomes 191 4-bit chars (under utf8mb4)
ALTER TABLE `site` MODIFY COLUMN `site` VARCHAR(191) NOT NULL UNIQUE;
ALTER TABLE `site` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `sitetype` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

# For each column:
ALTER TABLE `archivefeed` CHANGE `rawid` `rawid` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `url` `url` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `imageurl` `imageurl` NULL VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `author` `author` NULL VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `title` `title` NULL VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `keywords` `keywords` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `categories` `categories` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `description` `description` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `content` `content` mediumtext NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `archivefeed` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `comment` CHANGE `commentSubject` `commentSubject` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `comment` CHANGE `commentText` `commentText` VARCHAR(1000) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `comment` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `country` CHANGE `country` `country` VARCHAR(50) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `emailstatus` CHANGE `emailstatus` `emailstatus` VARCHAR(50) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `extractedemail` CHANGE `emailAddress` `emailAddress` VARCHAR(100) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `extractedemail` CHANGE `username` `username` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `extractedemail` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `feed` CHANGE `rawid` `rawid` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `url` `url` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `imageurl` `imageurl` NULL VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `author` `author` NULL VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `title` `title` NULL VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `keywords` `keywords` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `categories` `categories` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `description` `description` NULL VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `content` `content` mediumtext NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feed` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `feeduser` CHANGE `emailAddress` `emailAddress` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `lastName` `lastName` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `firstName` `firstName` VARCHAR(100) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `phoneNumber1` `phoneNumber1` VARCHAR(25) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `phoneNumber2` `phoneNumber2` VARCHAR(25) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `fax` `fax` VARCHAR(25) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `stateOrRegion` `stateOrRegion` VARCHAR(50) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `city` `city` VARCHAR(50) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `county` `county` VARCHAR(50) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `streetAddress` `streetAddress` VARCHAR(50) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `postalCode` `postalCode` VARCHAR(25) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `image1` `image1` VARCHAR(255) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `feeduser` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `gender` CHANGE `gender` `gender` VARCHAR(6) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `howdidyoufindus` CHANGE `howdidyoufindus` `howdidyoufindus` VARCHAR(40) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `installation` CHANGE `installationkey` `installationkey` VARCHAR(191) NOT NULL UNIQUE CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `installation` CHANGE `screenname` `screenname` VARCHAR(100) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `installation` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `site` CHANGE `site` `site` VARCHAR(191) NOT NULL UNIQUE CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `site` CHANGE `iconurl` `iconurl` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
ALTER TABLE `site` CHANGE `extradetails` `extradetails` VARCHAR(500) NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

ALTER TABLE `sitetype` CHANGE `sitetype` `sitetype` VARCHAR(10) NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci;
