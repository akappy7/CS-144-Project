#-----load location-------
LOAD DATA LOCAL INFILE 'location.csv' INTO TABLE Location FIELDS TERMINATED BY '|*|';

#-----load user ----------
LOAD DATA LOCAL INFILE 'user.csv' INTO TABLE User FIELDS TERMINATED BY '|*|';

# -----load Items --------
LOAD DATA LOCAL INFILE 'item.csv' INTO TABLE Items FIELDS TERMINATED BY '|*|';

# -----load Bids --------
LOAD DATA LOCAL INFILE 'bids.csv' INTO TABLE Bids FIELDS TERMINATED BY '|*|';

# -----load AssociateBidder --------
LOAD DATA LOCAL INFILE 'bidder.csv' INTO TABLE AssociateBidder FIELDS TERMINATED BY '|*|';

# -----load AssociateCategory --------
LOAD DATA LOCAL INFILE 'category.csv' INTO TABLE AssociateCategory FIELDS TERMINATED BY '|*|';
