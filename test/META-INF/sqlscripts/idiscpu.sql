/**
If you change any table, column names etc here then you have
to rework the persistence layer
*/
drop database if exists `loosebox_idisc`;
create database `loosebox_idisc` CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists `loosebox_idisc`.sitetype;
create table `loosebox_idisc`.sitetype
(
    sitetypeid SMALLINT(2) NOT NULL PRIMARY KEY,
    sitetype VARCHAR(10) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

insert into `loosebox_idisc`.sitetype VALUES(1, 'rss');
insert into `loosebox_idisc`.sitetype VALUES(2, 'web');
insert into `loosebox_idisc`.sitetype VALUES(3, 'timeline');
insert into `loosebox_idisc`.sitetype VALUES(4, 'trend');

drop table if exists `loosebox_idisc`.site;
create table `loosebox_idisc`.site
(
    siteid INTEGER(8) AUTO_INCREMENT not null primary key,
    site VARCHAR (255) not null UNIQUE,
    sitetypeid SMALLINT(2) not null, 
    iconurl VARCHAR (500) null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (sitetypeid) 
        REFERENCES sitetype(sitetypeid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists `loosebox_idisc`.feed;
create table `loosebox_idisc`.feed
(
    feedid INTEGER(8) AUTO_INCREMENT not null primary key,
    siteid INTEGER(8) not null,
    rawid VARCHAR(100) null,
    url VARCHAR (500) null,
    imageurl VARCHAR (500) null, 
    author VARCHAR(100) null,
    title VARCHAR (500) null,
    keywords VARCHAR(1000) null,
    categories VARCHAR(1000) null,
    description VARCHAR(1000) null,
    content TEXT (100000) not null,
    feeddate DATETIME not null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (siteid) 
        REFERENCES site(siteid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists `loosebox_idisc`.gender;
create table `loosebox_idisc`.gender
(
    genderid SMALLINT(2) NOT NULL PRIMARY KEY,
    gender VARCHAR(6) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

insert into `loosebox_idisc`.gender VALUES(1, 'Male');
insert into `loosebox_idisc`.gender VALUES(2, 'Female');
insert into `loosebox_idisc`.gender VALUES(3, 'Other');

drop table if exists `loosebox_idisc`.country;
create table `loosebox_idisc`.country
(
    countryid SMALLINT(3) NOT NULL PRIMARY KEY,
    country VARCHAR(50) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

/** YOU NEED TO MANUALLY POPULATE COUNTRIES */
insert into `loosebox_idisc`.country VALUES(124, 'Nigeria'); 

drop table if exists `loosebox_idisc`.howdidyoufindus;
create table `loosebox_idisc`.howdidyoufindus
(
    howdidyoufindusid SMALLINT(2) NOT NULL PRIMARY KEY,
    howdidyoufindus VARCHAR(40) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

insert into `loosebox_idisc`.howdidyoufindus VALUES(1, 'Through a friend or colleague');
insert into `loosebox_idisc`.howdidyoufindus VALUES(2, 'From the web');
insert into `loosebox_idisc`.howdidyoufindus VALUES(3, 'Magazines or other print media');
insert into `loosebox_idisc`.howdidyoufindus VALUES(4, 'Tv or other electronic media');
insert into `loosebox_idisc`.howdidyoufindus VALUES(5, 'Through our agent');

drop table if exists `loosebox_idisc`.userstatus;
create table `loosebox_idisc`.userstatus
(
    userstatusid SMALLINT(2) NOT NULL PRIMARY KEY,
    userstatus VARCHAR(15) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

insert into `loosebox_idisc`.userstatus VALUES(1, 'Unactivated');
insert into `loosebox_idisc`.userstatus VALUES(2, 'Activated');
insert into `loosebox_idisc`.userstatus VALUES(3, 'Deactivated');
insert into `loosebox_idisc`.userstatus VALUES(4, 'Unregistered');

drop table if exists `loosebox_idisc`.feeduser;
create table `loosebox_idisc`.feeduser
(
    feeduserid INTEGER(8) AUTO_INCREMENT not null primary key,
    emailAddress VARCHAR (100) not null UNIQUE,
    username VARCHAR (100) not null, 
    gender SMALLINT(2) null,
    lastName VARCHAR(100) null,
    firstName VARCHAR(100) null,
    dateOfBirth DATE null,
    phoneNumber1 VARCHAR(25) null,
    phoneNumber2 VARCHAR(25) null,
    fax VARCHAR(25) null,
    country SMALLINT(3) null,
    stateOrRegion VARCHAR(50) null,
    city VARCHAR(50) null,
    county VARCHAR(50) null,
    streetAddress VARCHAR(50) null,
    postalCode VARCHAR(25) null,
    image1 VARCHAR(255) null,
    howDidYouFindUs SMALLINT(2) null,
    userstatus SMALLINT(2) not null default '1',
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (gender) 
        REFERENCES gender(genderid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (country) 
        REFERENCES country(countryid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (howDidYouFindUs) 
        REFERENCES howdidyoufindus(howdidyoufindusid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (userstatus) 
        REFERENCES userstatus(userstatusid)
        ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists `loosebox_idisc`.comment;
create table `loosebox_idisc`.comment
(
    commentid INTEGER(8) AUTO_INCREMENT not null primary key,
    feedid INTEGER(8) not null,
    installationid INTEGER(8) not null,
    repliedto INTEGER(8) null,
    commentSubject VARCHAR (100) null, 
    commentText VARCHAR (1000) not null,
    dateusernotified DATETIME null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (feedid) 
        REFERENCES feed(feedid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (repliedto) 
        REFERENCES comment(commentid)
        ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
