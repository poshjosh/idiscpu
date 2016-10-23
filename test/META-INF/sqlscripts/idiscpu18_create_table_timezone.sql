drop table if exists `loosebox_idisc`.timezone;

create table `loosebox_idisc`.timezone
(
    timezoneid SMALLINT(3) NOT NULL PRIMARY KEY,
    abbreviation CHAR(8) not null,
    timezonename CHAR(64) not null

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

alter table `loosebox_idisc`.`timezone` modify column `timezonename` CHAR(64) not null UNIQUE;

/** Populate the timezone table before running this OR ERROR */
alter table `loosebox_idisc`.`site` add foreign key (timezoneid) references timezone(timezoneid) on delete cascade on update cascade;
