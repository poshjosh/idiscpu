drop table if exists `loosebox_idisc`.usersitehitcount;
create table `loosebox_idisc`.usersitehitcount
(
    installationid INTEGER(8) not null,
    siteid INTEGER(8) not null, 
    hitcount INTEGER(8) not null,
    datecreated DATETIME not null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY(installationid, siteid),

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (siteid) 
        REFERENCES site(siteid)
        ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB;


