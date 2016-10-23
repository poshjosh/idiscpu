drop table if exists `loosebox_idisc`.applaunchlog;
create table `loosebox_idisc`.applaunchlog (

    applaunchlogid INTEGER(8) AUTO_INCREMENT not null primary key,
    installationid INTEGER(8) null,
    launchtime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB;
