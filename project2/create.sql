CREATE TABLE Location (
        ID INTEGER,
        Longitude DOUBLE(15,10),
        Latitude DOUBLE(15,10),
        Country varchar(100),
        Location varchar(200),
        primary key (ID)
      );
CREATE TABLE User (
        ID varchar(200),
        RatingBidder INTEGER,
        RatingSeller INTEGER,
        primary key (ID)
      );
CREATE TABLE Items (
        ID INTEGER,
        Currently DOUBLE(15,2),
        BuyPrice DOUBLE(15,2),
        FirstBid DOUBLE(15,2),
        NumberBids INTEGER,
        LocationID INTEGER,
        FOREIGN KEY (LocationID)
          REFERENCES Location(ID)
          ON DELETE CASCADE,
        Started DATETIME,
        Ends DATETIME,
        Seller varchar(200),
        FOREIGN KEY (Seller)
          REFERENCES User(ID)
          ON DELETE CASCADE,
        Description varchar(1000),
        primary key (ID)
      );
CREATE TABLE Bids (
        UserID varchar(200),
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
        UserID varchar(200),
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
        primary key (ItemID, Category)
      );
