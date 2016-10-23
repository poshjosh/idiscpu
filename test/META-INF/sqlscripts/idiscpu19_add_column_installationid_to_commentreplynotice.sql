alter table `loosebox_idisc`.`commentreplynotice` add column `installationid` INTEGER(8) NOT NULL DEFAULT '0' after `commentid`;

-- we had to delete all rows in the table commentreplynotices before running this
alter table `loosebox_idisc`.`commentreplynotice` add foreign key (installationid) references installation(installationid) on delete cascade on update cascade;

alter table `loosebox_idisc`.`commentreplynotice` modify column `installationid` INTEGER(8) NOT NULL after `commentid`;

