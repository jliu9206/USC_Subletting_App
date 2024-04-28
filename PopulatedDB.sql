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
    FOREIGN KEY (RenterID) REFERENCES Renters(ID),
    FOREIGN KEY (SubletID) REFERENCES Subletters(ID),
    FOREIGN KEY (ReviewDirectionID) REFERENCES ReviewDirection(DirectionID)
);

CREATE TABLE FavoriteProperties (
    PostID INT,
    SubletID INT,
    FOREIGN KEY (PostID) REFERENCES Post(ID),
    FOREIGN KEY (SubletID) REFERENCES Subletters(ID)
);


-- everything below is SAMPLE population data for initial testing

INSERT INTO Login (Username, PasswordHash, Email, FirstName, LastName, TypeID) VALUES
('user1', 'password123', 'ttrojan@usc.edu', 'Tommy', 'Trojan', 2),
('user2', 'password456', 'woofwoof@usc.edu', 'George', 'Tirebiter', 1);

INSERT INTO Renters (Username) VALUES
('user1');

INSERT INTO Subletters (Username) VALUES
('user2');

INSERT INTO Post (Title, PropertyType, Address, MonthlyPrice, NumberOfBedrooms, NumberOfBathrooms, Size, AvailabilityStart, AvailabilityEnd, Description, Renter) VALUES
('C&I Apartment', 1, '3131 S. Hoover St.', 1500.00, 2, 1, 1200.00, '2024-05-01', '2024-06-01', 'This is an apartment in Cowlings and Ilium College', 1),
('Freshman Dorm Room', 3, '3096 McClintock Ave.', 1000.00, 1, 1, 800.00, '2024-05-01', '2024-06-01', 'This is a room in McCarthy Honors College', 2);

INSERT INTO Ratings (NumberOfStars, Comments, RenterID, SubletID, ReviewDirectionID) VALUES
(5, 'Great experience!', 3, 1, 1),
(4, 'Good service.', 4, 2, 2);

INSERT INTO FavoriteProperties (PostID, SubletID) VALUES
(1, 1),
(2, 2);
