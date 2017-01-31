CREATE TABLE Location (
        ID INTEGER,
        Longitude INTEGER,
        Latitude INTEGER,
        Country varchar(200),
        Location varchar(200),
        primary key (ID)
      );
CREATE TABLE User (
        ID INTEGER,
        RatingSeller INTEGER,
        RatingBidder INTEGER,
        primary key (ID)
      );
CREATE TABLE Items (
        ID INTEGER,
        Currently INTEGER,
        BuyPrice INTEGER,
        FirstBid INTEGER,
        NumberBids INTEGER,
        LocationID INTEGER,
        FOREIGN KEY (LocationID)
          REFERENCES Location(ID)
          ON DELETE CASCADE,
        Started DATETIME,
        Ends DATETIME,
        Seller INTEGER,
        FOREIGN KEY (Seller)
          REFERENCES User(ID)
          ON DELETE CASCADE,
        Description varchar(500),
        primary key (ID)
      );
CREATE TABLE Bids (
        UserID INTEGER,
        FOREIGN KEY (UserID)
          REFERENCES User(ID)
          ON DELETE CASCADE,
        Time DATETIME,
        ItemID INTEGER,
        FOREIGN KEY (ItemID)
          REFERENCES Items(ID)
          ON DELETE CASCADE,
        Amount DOUBLE(15,2),
        primary key (Time, UserID, ItemID)
     );
Create TABLE AssociateBidder(
        UserID INTEGER,
        FOREIGN KEY (UserID)
          REFERENCES User(ID)
          ON DELETE CASCADE,
        LocationID INTEGER,
        FOREIGN KEY (LocationID)
          REFERENCES Location(ID)
          ON DELETE CASCADE,
        primary key (UserID)
     );
Create TABLE AssociateCategory(
        ItemID INTEGER,
        FOREIGN KEY (ItemID)
          REFERENCES Items(ID)
          ON DELETE CASCADE,
        Category varchar(200),
        primary key (ItemID)
      );
