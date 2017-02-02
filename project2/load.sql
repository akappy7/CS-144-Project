#-----load location-------
LOAD DATA LOCAL INFILE 'location.csv' INTO TABLE Location FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';

#-----load user ----------
LOAD DATA LOCAL INFILE 'user.csv' INTO TABLE User FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';

# -----load Items --------
SET FOREIGN_KEY_CHECKS=0;
LOAD DATA LOCAL INFILE 'item.csv' INTO TABLE Items FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';

# -----load Bids --------
LOAD DATA LOCAL INFILE 'bids.csv' INTO TABLE Bids FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';

# -----load AssociateBidder --------
LOAD DATA LOCAL INFILE 'bidder.csv' INTO TABLE AssociateBidder FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';

# -----load AssociateCategory --------
LOAD DATA LOCAL INFILE 'category.csv' INTO TABLE AssociateCategory FIELDS TERMINATED BY '|*|' OPTIONALLY ENCLOSED BY '"';
