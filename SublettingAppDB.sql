-- -- drop table statements for SQL testing
-- DROP TABLE Ratings;
-- DROP TABLE Post;
-- DROP TABLE PropType;
-- DROP TABLE Renters;
-- DROP TABLE Subletters;
-- DROP TABLE Login;
-- DROP TABLE UserType;
-- DROP TABLE ReviewDirection;

CREATE TABLE UserType (
    TypeID INT PRIMARY KEY,
    TypeName VARCHAR(50)
);

INSERT INTO UserType (TypeID, TypeName) VALUES
(1, 'Subletter'),
(2, 'Renter'),
(3, 'Administrator');

CREATE TABLE Login (
    LoginID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256) UNIQUE,
    PasswordHash VARCHAR(256),
    Email VARCHAR(55) UNIQUE,
    TypeID INT,
    FOREIGN KEY (TypeID) REFERENCES UserType(TypeID)
);

CREATE TABLE Subletters (
    SubletID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256),
    FirstName VARCHAR(55),
    LastName VARCHAR(55),
    Location VARCHAR(55),
    SubletDetails INT,
    FOREIGN KEY (Username) REFERENCES Login(Username)
);

CREATE TABLE Renters (
    RenterID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256),
    FirstName VARCHAR(55),
    LastName VARCHAR(55),
    Location VARCHAR(55),
    TypeOfRent INT,
    FOREIGN KEY (Username) REFERENCES Login(Username)
);

CREATE TABLE PropType (
    TypeID INT PRIMARY KEY,
    TypeName VARCHAR(50)
);

INSERT INTO PropType (TypeID, TypeName) VALUES
	(1, 'Apartment'),
	(2, 'House'),
	(3, 'Studio'),
	(4, 'Loft');

CREATE TABLE Post (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Title TEXT,
    PropertyType INT,
    Address VARCHAR(256),
    MonthlyPrice DOUBLE,
    NumberOfBedrooms INT,
    NumberOfBathrooms INT,
    Size DOUBLE,
    AvailabilityStart DATE,
    AvailabilityEnd DATE,
    Description TEXT,
    Renter INT,
    FOREIGN KEY (PropertyType) REFERENCES PropType(TypeID),
	FOREIGN KEY (Renter) REFERENCES Renters(RenterID)
);

CREATE TABLE ReviewDirection (
    DirectionID INT AUTO_INCREMENT PRIMARY KEY,
    DirectionName VARCHAR(50)
);

INSERT INTO ReviewDirection (DirectionName) VALUES ('RenterToSubletter'), ('SubletterToRenter');

CREATE TABLE Ratings (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NumberOfStars INT,
    Comments TEXT,
    RenterID INT,
    SubletID INT,
    ReviewDirectionID INT,
    FOREIGN KEY (RenterID) REFERENCES Renters(RenterID),
    FOREIGN KEY (SubletID) REFERENCES Subletters(SubletID),
    FOREIGN KEY (ReviewDirectionID) REFERENCES ReviewDirection(DirectionID)
);