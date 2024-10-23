-- Her opretter vi byer med postnummer
INSERT INTO City (city_id, c_name) VALUES (1000, 'København');
INSERT INTO City (city_id, c_name) VALUES (2000, 'Frederiksberg');
INSERT INTO City (city_id, c_name) VALUES (4000, 'Roskilde');
INSERT INTO City (city_id, c_name) VALUES (4700, 'Næstved');
INSERT INTO City (city_id, c_name) VALUES (8000, 'Aarhus');
INSERT INTO City (city_id, c_name) VALUES (9000, 'Aalborg');
INSERT INTO City (city_id, c_name) VALUES (8600, 'Silkeborg');
INSERT INTO City (city_id, c_name) VALUES (8660, 'Skanderborg');
INSERT INTO City (city_id, c_name) VALUES (5000, 'Odense');

-- Her opretter vi attraktioner
INSERT INTO Attraction (a_name, city_id, description, fee) VALUES ('Tivoli', 1000, 'Verdens ældste forlystelsespark!', 180);
INSERT INTO Attraction (a_name, city_id, description, fee) VALUES ('Roskilde Festival', 4000, 'Kæmpe stor popfestival fyldt med musik', 2300);
INSERT INTO Attraction (a_name, city_id, description, fee) VALUES ('HC Andersens Hus', 5000, 'Ham der har skrevet den grimme ælling', 0);

-- Vis alle attraktioner
SELECT * FROM Attraction;

-- Her opretter vi tags
INSERT INTO Tag (tag_name) VALUES ('Børnevenligt');
INSERT INTO Tag (tag_name) VALUES ('Forlystelsespark');
INSERT INTO Tag (tag_name) VALUES ('Musik');
INSERT INTO Tag (tag_name) VALUES ('Restauranter');
INSERT INTO Tag (tag_name) VALUES ('Gratis');
INSERT INTO Tag (tag_name) VALUES ('Kultur');

SELECT * FROM Tag;

-- Her tilføjer vi tags til attraktionerne
-- Tivoli
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (1, 1);
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (1, 2);
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (1, 3);
-- Roskilde Festival
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (2, 3);
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (2, 4);
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (2, 6);
-- HC Andersens Hus
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (3, 5);
INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (3, 6);

SELECT * FROM Attraction_tag;