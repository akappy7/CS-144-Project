CREATE TABLE ItemLocation (
        ItemID INTEGER,
        Coord POINT NOT NULL,
        FOREIGN KEY (ItemID)
          REFERENCES Items(ID)
          ON DELETE CASCADE,
        PRIMARY KEY (ItemID)
) ENGINE=MyISAM;

INSERT INTO ItemLocation (ItemID, Coord)
SELECT a.ID, POINT(b.Latitude, b.Longitude)
FROM Items as a, Location as b
WHERE a.LocationID = b.ID AND b.Longitude IS NOT NULL AND b.Latitude IS NOT NULL;

CREATE SPATIAL INDEX sp_index ON ItemLocation(Coord);
