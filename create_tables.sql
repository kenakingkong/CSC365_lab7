/*
    CSC 365 LAB 7
    Create And Populate Inn Tables
    Grant user privileges
*/

USE mkong02;

// create tables

CREATE TABLE IF NOT EXISTS lab7_rooms (
    RoomCode char(5) PRIMARY KEY,
    RoomName varchar(30) NOT NULL,
    Beds int(11) NOT NULL,
    bedType varchar(8) NOT NULL,
    maxOcc int(11) NOT NULL,
    basePrice DECIMAL(6,2) NOT NULL,
    decor varchar(20) NOT NULL,
    UNIQUE (RoomName)
);


CREATE TABLE IF NOT EXISTS lab7_reservations (
    CODE int(11) PRIMARY KEY,
    Room char(5) NOT NULL,
    CheckIn date NOT NULL,
    Checkout date NOT NULL,
    Rate DECIMAL(6,2) NOT NULL,
    LastName varchar(15) NOT NULL,
    FirstName varchar(15) NOT NULL,
    Adults int(11) NOT NULL,
    Kids int(11) NOT NULL,
    UNIQUE (Room, CheckIn),
    UNIQUE (Room, Checkout),
    FOREIGN KEY (Room) REFERENCES lab7_rooms (RoomCode)
);

// populate tables

INSERT INTO lab7_rooms SELECT * FROM INN.rooms;

INSERT INTO lab7_reservations SELECT CODE, Room,
    DATE_ADD(CheckIn, INTERVAL 9 YEAR),
    DATE_ADD(Checkout, INTERVAL 9 YEAR),
    Rate, LastName, FirstName, Adults, Kids FROM INN.reservations;


// grant privileges
grant all on INN.reservations to rgadgil@'%';
grant all on INN.rooms to rgadgil@'%';
grant all on lab7_rooms to rgadgil@'%';
grant all on lab7_reservations to rgadgil@'%';
grant all on INN.reservations to wlarsen@'%';
grant all on INN.rooms to wlarsen@'%';
grant all on lab7_rooms to wlarsen@'%';
grant all on lab7_reservations to wlarsen@'%';