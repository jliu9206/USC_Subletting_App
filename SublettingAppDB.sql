-- -- drop table statements for SQL testing
DROP TABLE Ratings;
DROP TABLE FavoriteProperties;
DROP TABLE Post;
DROP TABLE Renters;
DROP TABLE Subletters;
DROP TABLE PropType;
DROP TABLE Login;
DROP TABLE UserType;
DROP TABLE ReviewDirection;


CREATE TABLE UserType (
    TypeID INT PRIMARY KEY,
    TypeName VARCHAR(50)
);

INSERT INTO UserType (TypeID, TypeName) VALUES
(1, 'Subletter'),
(2, 'Renter'),
(3, 'Administrator');

CREATE TABLE Login ( /* stores information for all users */
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256) UNIQUE,
    PasswordHash VARCHAR(256),
    Email VARCHAR(55) UNIQUE,
    FirstName VARCHAR(55),
    LastName VARCHAR(55),
    TypeID INT,
    FOREIGN KEY (TypeID) REFERENCES UserType(TypeID)
);

CREATE TABLE Subletters (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256) UNIQUE,
    FOREIGN KEY (Username) REFERENCES Login(Username)
);

CREATE TABLE Renters (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256) UNIQUE,
    FOREIGN KEY (Username) REFERENCES Login(Username)
);

CREATE TABLE PropType (
    TypeID INT PRIMARY KEY,
    TypeName VARCHAR(50)
);

INSERT INTO PropType (TypeID, TypeName) VALUES
	(1, 'Apartment'),
	(2, 'House'),
	(3, 'Room');

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
	FOREIGN KEY (Renter) REFERENCES Renters(ID)
);


CREATE TABLE Ratings (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(256),
    NumberOfStars INT,
    Comments TEXT,
	PostID INT,
    FOREIGN KEY (PostID) REFERENCES Post(ID)
);

CREATE TABLE FavoriteProperties (
    PostID INT,
    SubletID INT,
    FOREIGN KEY (PostID) REFERENCES Post(ID),
    FOREIGN KEY (SubletID) REFERENCES Subletters(ID)
);

-- CREATE TABLE Login ( /* stores information for all users */
--     ID INT AUTO_INCREMENT PRIMARY KEY,
--     Username VARCHAR(256) UNIQUE,
--     PasswordHash VARCHAR(256),
--     Email VARCHAR(55) UNIQUE,
--     FirstName VARCHAR(55),
--     LastName VARCHAR(55),
--     TypeID INT,
--     FOREIGN KEY (TypeID) REFERENCES UserType(TypeID)
-- );

-- SELECT 