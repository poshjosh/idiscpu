alter table `loosebox_idisc`.`site` add column `countryid` SMALLINT(3) NULL after `iconurl`;
alter table `loosebox_idisc`.`site` add foreign key (countryid) references country(countryid) on delete cascade on update cascade;
