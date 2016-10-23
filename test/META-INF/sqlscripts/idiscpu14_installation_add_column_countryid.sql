alter table `loosebox_idisc`.`installation` add column `countryid` SMALLINT(3) NULL after `screenname`;
alter table `loosebox_idisc`.`installation` add foreign key (countryid) references country(countryid) on delete cascade on update cascade;
