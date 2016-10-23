drop table if exists `loosebox_idisc`.commentnotification;
create table `loosebox_idisc`.commentnotification
(
    commentnotificationid INTEGER(8) AUTO_INCREMENT not null primary key,
    commentid INTEGER(8) not null,
    dateusernotified DATETIME not null,
    dateuserread DATETIME null,
    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    extradetails VARCHAR(500) null,

    FOREIGN KEY (commentid) 
        REFERENCES comment(commentid)
        ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
