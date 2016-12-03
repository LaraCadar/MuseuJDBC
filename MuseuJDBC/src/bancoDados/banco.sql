create database Museu;
use Museu;

create table Usuarios(
userId int auto_increment primary key,
userName varchar(50) not null, 
userBirthDate date not null, 
userSex varchar(10) not null,
userLogin varchar (20)  not null,
serEmail varchar(50) not null,
userPassword varchar(20) not null,
userType varchar(15) not null
);

create table Obras (
objectId int primary key,
objectName varchar(50) not null,
objectType varchar(15) not null,
objectDate date not null, 
objectArtist varchar(10) not null,
objectSource varchar(20) not null,
objectDescription varchar(150) not null,
);