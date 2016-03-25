drop table if exists `loosebox_idisc`.feedhitcount;
drop table if exists `loosebox_idisc`.feeduser;
drop table if exists `loosebox_idisc`.userstatus;

create table `loosebox_idisc`.feeduser
(
    feeduserid INTEGER(8) AUTO_INCREMENT not null primary key,
    emailAddress VARCHAR (100) not null UNIQUE,
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
        ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

create table `loosebox_idisc`.feedhit
(
    feedhitid INTEGER(8) AUTO_INCREMENT not null primary key,
    feeduserid INTEGER(8) null,
    feedid INTEGER(8) not null,
    hittime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (feeduserid) 
        REFERENCES feeduser(feeduserid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (feedid) 
        REFERENCES feed(feedid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB;

