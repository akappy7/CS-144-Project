Part B: Design your relational schema

  1. Relation "Item": {ItemID, Currently, Buy Price, First Bid, # of bids,
                      locationID, Started, Ends, Seller, Description}

               Keys for Item: {ItemID}

               Foreign Keys for Item: {locationID, Seller (FK to user)}

     Relation "Bids": {Bidder, Time, ItemID, Amount}

              Keys for Bids: {Bidder, Time, ItemID}

              Foreign Keys for Bids: {Bidder (FK to user), ItemID}

     Relation "Associate Bidder": {UserID, LocationID}

              Keys for Associate Bidder: {UserID, LocationID}

              Foreign Keys for Associate Bidder: {UserID, LocationID}

     Relation "Associate Category": {ItemID, CategoryName}

              Keys for Associate Category: {ItemID, CategoryName}

              Foreign Keys for Associate Category: {ItemID}

     Relation "User": {UserID, Rating Bidder, Rating Seller}

              Keys for User: {UserID}

              Foreign Keys for User: {}

     Relation "Location": {LocationID, Longitude, Latitude, Country, Location}

              Keys for Location: {LocationID}

              Foreign Keys for Location: {}

  2. All the nontrivial functional dependencies in the relations above specify
     keys in them.

  3. The relation specified above those in fact comply with the Boyce Codd
     Normal Form because all relation dependencies have keys on the left hand
     side. There do not exist functional dependencies with non-key attributes
     on the left hand side.

 4. On inspection, there did not seem to be any multivariate dependencies
    in the relations (in addition the relation was in BCNF from question 3)
    which led to the conclusion that the relation satisfied the 4NF.
