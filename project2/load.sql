#-----load location-------
LOAD DATA LOCAL INFILE 'location.dat' INTO TABLE Location FIELDS TERMINATED BY '|*|';

#-----load user ----------
LOAD DATA LOCAL INFILE 'user.dat' INTO TABLE User FIELDS TERMINATED BY '|*|';

# -----load Items --------
LOAD DATA LOCAL INFILE 'item.dat' INTO TABLE Items FIELDS TERMINATED BY '|*|';

# -----load Bids --------
LOAD DATA LOCAL INFILE 'bids.dat' INTO TABLE Bids FIELDS TERMINATED BY '|*|';

# -----load AssociateBidder --------
LOAD DATA LOCAL INFILE 'bidder.dat' INTO TABLE AssociateBidder FIELDS TERMINATED BY '|*|';

# -----load AssociateCategory --------
LOAD DATA LOCAL INFILE 'category.dat' INTO TABLE AssociateCategory FIELDS TERMINATED BY '|*|';
