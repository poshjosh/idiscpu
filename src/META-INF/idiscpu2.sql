drop table if exists `loosebox_idisc`.archivedfeed;
create table `loosebox_idisc`.archivedfeed
(
    archivedfeedid INTEGER(8) AUTO_INCREMENT not null primary key,
    feedid INTEGER(8) not null UNIQUE,
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

