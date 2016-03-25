alter table `loosebox_idisc`.`installation` add column `feeduserid` INTEGER(8) null after `emailAddress`;

alter table `loosebox_idisc`.`installation` add foreign key (feeduserid) references feeduser(feeduserid) on delete cascade on update cascade;

alter table `loosebox_idisc`.`installation` drop `emailAddress`;

alter table `loosebox_idisc`.`installation` add column `screenname` VARCHAR(100) null UNIQUE after `feeduserid`;

-- After giving the already existing columns screen names, I ran this
alter table `loosebox_idisc`.`installation` modify column `screenname` VARCHAR(100) NOT NULL UNIQUE;

-- If foreign key is not named `feedhit_ibfk_1` use query SHOW CREATE TABLE `feedhit` to find out the actual name --
alter table `loosebox_idisc`.`feedhit` drop foreign key `feedhit_ibfk_1`;
alter table `loosebox_idisc`.`feedhit` drop `feeduserid`;

create table `loosebox_idisc`.bookmarkfeed
(
    bookmarkfeedid INTEGER(8) AUTO_INCREMENT not null primary key,
    installationid INTEGER(8) not null,
    feedid INTEGER(8) not null,
    datecreated TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (feedid) 
        REFERENCES feed(feedid)
        ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

create table `loosebox_idisc`.favoritefeed
(
    favoritefeedid INTEGER(8) AUTO_INCREMENT not null primary key,
    installationid INTEGER(8) not null,
    feedid INTEGER(8) not null,
    datecreated TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (installationid) 
        REFERENCES installation(installationid)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (feedid) 
        REFERENCES feed(feedid)
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
