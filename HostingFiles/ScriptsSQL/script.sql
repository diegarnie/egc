start transaction;
/*drop user 'admin'@'%';*/
drop database if exists `keysvotes`;
create database `keysvotes`;
use `keysvotes`;
create user 'admin'@'%' identified by 'admin';


grant select, insert, update, delete, create, drop, references, index, alter,
create temporary tables, lock tables, create view, create routine,
alter routine, execute, trigger, show view
on `keysvotes`.* to 'admin'@'%';


CREATE TABLE `keysvotes` (
`idvotation` VARCHAR(128) UNIQUE NOT NULL,
`publicKey` VARCHAR(2048) NOT NULL,
`privateKey` VARCHAR(2048) NOT NULL
);


commit;