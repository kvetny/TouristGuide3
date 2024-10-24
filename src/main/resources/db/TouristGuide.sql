CREATE DATABASE IF NOT EXISTS TouristGuide3;

USE TouristGuide3;

DROP TABLE IF EXISTS Attraction_tag;
DROP TABLE IF EXISTS Attraction;
DROP TABLE IF EXISTS Tag;
DROP TABLE IF EXISTS City;

CREATE TABLE City (
    city_id INT,
    c_name VARCHAR(30),
    PRIMARY KEY (city_id)
);

CREATE TABLE Attraction (
    id INT NOT NULL AUTO_INCREMENT,
    a_name VARCHAR(30),
    city_id INT,
    description VARCHAR(255),
    fee INT,
    PRIMARY KEY (id),
    FOREIGN KEY (city_id) REFERENCES City(city_id)
);

CREATE TABLE Tag (
    id INT NOT NULL AUTO_INCREMENT,
    tag_name VARCHAR(30),
    PRIMARY KEY (id)
);

CREATE TABLE Attraction_tag (
    attraction_id INT,
    tag_id INT,
    PRIMARY KEY (attraction_id, tag_id),
    FOREIGN KEY (attraction_id) REFERENCES Attraction(id),
    FOREIGN KEY (tag_id) REFERENCES Tag(id)
);
