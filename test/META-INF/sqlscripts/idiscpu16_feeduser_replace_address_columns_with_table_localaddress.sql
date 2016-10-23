-- If foreign key is not named `feedhit_ibfk_2` use query SHOW CREATE TABLE `feeduser` to find out the actual name --
alter table `loosebox_idisc`.`feeduser` drop foreign key `feeduser_ibfk_2`;
alter table `loosebox_idisc`.`feeduser` drop column `country`;
alter table `loosebox_idisc`.`feeduser` drop column `stateOrRegion`;
alter table `loosebox_idisc`.`feeduser` drop column `city`;
alter table `loosebox_idisc`.`feeduser` drop column `county`;
alter table `loosebox_idisc`.`feeduser` drop column `streetAddress`;
alter table `loosebox_idisc`.`feeduser` drop column `postalCode`;
alter table `loosebox_idisc`.`feeduser` add column `localaddressid` INTEGER(8) NULL after `fax`;
alter table `loosebox_idisc`.`feeduser` add foreign key (localaddressid) references localaddress(localaddressid) on delete cascade on update cascade;
