#-----load location-------
LOAD DATA LOCAL INFILE '~path/location.csv' INTO TABLE Location FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

#-----load user ----------
LOAD DATA LOCAL INFILE '~path/user.csv' INTO TABLE User FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

# -----load Items --------
LOAD DATA LOCAL INFILE '~path/items.csv' INTO TABLE Items FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

# -----load Bids --------
LOAD DATA LOCAL INFILE '~path/bids.csv' INTO TABLE Bids FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

# -----load AssociateBidder --------
LOAD DATA LOCAL INFILE '~path/AssociateBidder.csv' INTO TABLE AssociateBidder FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

# -----load AssociateCategory --------
LOAD DATA LOCAL INFILE '~path/AssociateCategory.csv' INTO TABLE AssociateCategory FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
