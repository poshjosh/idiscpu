drop table if exists `loosebox_idisc`.localaddress;

create table `loosebox_idisc`.localaddress
(
    localaddressid INTEGER(8) AUTO_INCREMENT not null primary key,
    country SMALLINT(3) null,
    stateOrRegion VARCHAR(50) null,
    city VARCHAR(50) null,
    county VARCHAR(50) null,
    streetAddress VARCHAR(50) null,
    postalCode VARCHAR(25) null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (country) 
        REFERENCES country(countryid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
