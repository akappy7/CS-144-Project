#------number of users------
select count(*) from User;

#------number of items in New York-------
select count(*) from Items where LocationID IN ( select ID from Location where Location="New York");

#-------Find the number of auctions belonging to exactly four categories.-----
select count(*) from (select * from AssociateCategory group by ItemID having COUNT(*)=4) t;

#-------Find the ID(s) of current (unsold) auction(s) with the highest bid
SELECT ID from Items  where NumberBids > 0 and Currently = (select max(Currently) from Items where NumberBids != 0 and Ends> "2001-12-20 00:00:00");

#------Find the number of sellers whose rating is higher than 1000.
select count(*) from User where RatingSeller > 1000;


#------Find the number of users who are both sellers and bidders.
SELECT count(distinct a.Seller) from Items as a inner join AssociateBidder as b ON a.Seller = b.UserID;

#------Find the number of categories that include at least one item with a bid of more than $100.----
select count(distinct(Category)) from AssociateCategory where ItemID IN ( select ID from Items where NumberBids > 0 and Currently>100);
