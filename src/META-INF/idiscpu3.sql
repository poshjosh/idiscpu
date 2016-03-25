drop table if exists `loosebox_idisc`.extractedemail;
drop table if exists `loosebox_idisc`.installation;
create table `loosebox_idisc`.installation
(
    installationid INTEGER(8) AUTO_INCREMENT not null primary key,
    installationkey VARCHAR(255) not null UNIQUE,
    emailAddress VARCHAR (100) null,
    firstinstallationdate DATETIME not null,
    lastinstallationdate DATETIME not null,
    firstsubscriptiondate DATETIME null,
    lastsubscriptiondate DATETIME null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists `loosebox_idisc`.extractedemail;
create table `loosebox_idisc`.extractedemail
(
    extractedemailid INTEGER(8) AUTO_INCREMENT not null primary key,
    installationid INTEGER(8) not null,
    emailAddress VARCHAR (100) not null,
    username VARCHAR(100) null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

