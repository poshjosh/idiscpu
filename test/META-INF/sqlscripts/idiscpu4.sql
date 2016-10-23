alter table `loosebox_idisc`.`feedhit` add column `installationid` INTEGER(8) null after `feedhitid`;

alter table `loosebox_idisc`.`feedhit` add foreign key (installationid) references installation(installationid) on delete cascade on update cascade;

alter table `loosebox_idisc`.`extractedemail` add column `emailstatus` smallint(2) not null default '1' after `timemodified`;

drop table if exists `loosebox_idisc`.emailstatus;
create table `loosebox_idisc`.emailstatus
(
    emailstatusid SMALLINT(2) NOT NULL PRIMARY KEY,
    emailstatus VARCHAR(50) not null
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

insert into `loosebox_idisc`.emailstatus VALUES(1, 'unverified');
insert into `loosebox_idisc`.emailstatus VALUES(2, 'verified');
insert into `loosebox_idisc`.emailstatus VALUES(3, 'bounced');
insert into `loosebox_idisc`.emailstatus VALUES(4, 'disabled_or_discontinued');
insert into `loosebox_idisc`.emailstatus VALUES(5, 'unable_to_relay');
insert into `loosebox_idisc`.emailstatus VALUES(6, 'does_not_exist');
insert into `loosebox_idisc`.emailstatus VALUES(7, 'could_not_be_delivered_to');
insert into `loosebox_idisc`.emailstatus VALUES(8, 'black_listed');
insert into `loosebox_idisc`.emailstatus VALUES(9, 'verification_attempted_but_status_unknown');
insert into `loosebox_idisc`.emailstatus VALUES(10, 'user_opted_out_of_mailinglist');
insert into `loosebox_idisc`.emailstatus VALUES(11, 'registered_user');
insert into `loosebox_idisc`.emailstatus VALUES(12, 'automated_system_email');
insert into `loosebox_idisc`.emailstatus VALUES(13, 'restricted');
insert into `loosebox_idisc`.emailstatus VALUES(14, 'invalid_format');

alter table `loosebox_idisc`.extractedemail add foreign key (emailstatus) references emailstatus(emailstatusid) on delete cascade on update cascade;



