CREATE VIEW AnnualTopCustomers AS
    SELECT 
        Person.fname, Person.lname, Customer.CustomerID, SUM(Orders.TotalBalance)
    FROM
        Person,
        Customer,
        Orders,
        Payment
	WHERE Person.ID = Customer.CustomerID AND Orders.CustomerID = Customer.CustomerID AND Payment.OrderID = Orders.OrderID AND Payment.DatePaid > DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
    GROUP BY Person.fname, Person.lname, Customer.CustomerID
    ORDER BY SUM(Orders.TotalBalance) DESC
    LIMIT 3;
    
SELECT * FROM AnnualTopCustomers;

-- VIEW b. Popular Restaurant Type
CREATE VIEW PopularRestaurantType AS
	SELECT Type.Type, COUNT(*) -- added count(*)
    FROM Type, Orders, Payment
    WHERE Orders.addressID = Type.addressID AND Payment.OrderID = Orders.OrderID AND Payment.DatePaid > DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
    GROUP BY Type.Type
    ORDER BY COUNT(*) DESC
    LIMIT 1;
    
SELECT * FROM PopularRestaurantType;
    
-- View c. Potential Silver Members
CREATE VIEW PotentialSilverMembers AS
	SELECT Person.fname, Person.minit, Person.lname, Customer.CustomerID, COUNT(*)/2 as totalOrders
    FROM Customer, Orders, Payment, SilverMember, Person
    WHERE Customer.CustomerID = Orders.CustomerID AND Payment.OrderID = Orders.OrderID AND Person.ID = Customer.CustomerID
    AND Payment.DatePaid > DATE_SUB(CURDate(), INTERVAL 1 MONTH) AND Customer.CustomerID NOT IN (SELECT ID FROM SilverMember) 
    GROUP BY CustomerID
    HAVING COUNT(*)/2 >= 10;
    
SELECT * FROM PotentialSilverMembers; 

SELECT CustomerID, COUNT(*) FROM Orders GROUP BY CustomerID;

-- View d. Best Area Manager
CREATE VIEW BestAreaManager AS
	SELECT Person.fname, Person.minit, Person.lname, AreaManagerID, COUNT(*) -- added COUNT(*)
    FROM AreaManager, Shop, Contract, Person
	WHERE Person.ID = AreaManager.ID AND AreaManager.ID = Contract.areaManagerID AND Contract.addressID = Shop.addressID AND Contract.contractStartDate > DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
    GROUP BY AreaManagerID
    ORDER BY COUNT(*) DESC
    LIMIT 1;
    
SELECT * FROM BestAreaManager;

-- CHECK
CREATE VIEW PopularRestaurantByType AS
	SELECT rcount.sn, rcount.ty, max(cnt)
	FROM(
		SELECT Type.Type as ty, Shop.ShopName as sn, Count(*) as cnt
		FROM Orders, Type, Shop, Restaurant
		WHERE Restaurant.AddressID = Type.AddressID AND Orders.AddressID = Type.AddressID AND Shop.AddressID = Restaurant.AddressID
		Group BY Type.Type, Shop.ShopName
		ORDER BY cnt DESC) as rcount, Shop, Type
	GROUP BY rcount.sn, rcount.ty;
    
SELECT * FROM PopularRestaurantByType;
-- ***** Queries *****
-- 1. area manager that manges most deliverers
SELECT AreaManagerID, COUNT(*)
FROM dbproject.Deliverer d
GROUP BY AreaManagerID
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 2. 
SELECT AVG(totalOrders)
FROM PotentialSilverMembers;

-- 3. 
SELECT Shop.shopName, Person.fname, Person.minit, Person.lname, Customer.CustomerID
FROM Customer, Shop, Orders, Type, Person
WHERE Customer.CustomerID = Orders.CustomerID AND Orders.AddressID = Shop.AddressID AND Shop.AddressID = Type.AddressID AND Person.ID = Customer.CustomerID
AND Type.Type = (SELECT Type FROM PopularRestaurantType)
GROUP BY Shop.shopName, Person.fname, Person.minit, Person.lname, Customer.CustomerID;

-- 4.
SELECT p.id, p.fname, p.minit, p.lname 
FROM Customer c, Person p, SilverMember sm
WHERE c.customerID= p.id AND p.id=sm.id 
AND (DATE_SUB(  sm.silverEarnDate  , INTERVAL 1 MONTH) <= c.joinDate);

-- 5. 
SELECT o.delivererID, pe.fname, pe.minit, pe.lname, COUNT(*)
FROM Orders o, Payment p, Person pe
WHERE o.OrderID= p.OrderID AND pe.id=o.delivererID
AND p.datePaid >  (DATE_SUB( NOW(3)  , INTERVAL 1 MONTH)) 
GROUP BY o.delivererID, pe.fname, pe.minit, pe.lname
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 6.
SELECT ap.addressID, s.shopName, COUNT(*)
FROM Orders o, AppliedPromotions ap, Payment p, Shop s
WHERE p.orderID=o.orderID AND o.OrderID= ap.OrderID AND s.addressID= o.addressID
AND p.datePaid >  (DATE_SUB( NOW(3)  , INTERVAL 1 MONTH)) 
GROUP BY ap.addressID, s.shopName
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 7.
SELECT sub.id, sub.fname, sub.minit, sub.lname FROM 
(SELECT p.id,p.fname, p.minit, p.lname, COUNT(distinct o.addressID) as countdis FROM Orders o, Type t, Person p WHERE p.id= o.customerID AND
o.addressID= t.addressID AND t.type= "Fast Food" GROUP BY p.id) as sub
WHERE sub.countdis= 
(SELECT COUNT(*) FROM Restaurant r, Type t WHERE r.addressID=t.addressID AND t.type= "Fast Food");

-- 8.
SELECT Shop.shopName, Shop.addressID, Person.fname, Person.minit, Person.lname, lst.cid, lst.Cost
FROM(
	SELECT Orders.addressID as adr, Customer.customerID as cid, Orders.totalBalance as Cost
	FROM Orders, Customer
	WHERE Orders.customerID = Customer.customerID) as lst, Shop, Person
WHERE Shop.addressID = lst.adr AND Person.ID = lst.cid
ORDER BY Shop.shopName;

-- 9.
SELECT r.rArea, COUNT(*)
FROM Restaurant r
GROUP BY r.rArea
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 10.
SELECT s.ShopName, sc.openingTimes, sc.closingTimes, COUNT(*)
FROM Orders o, Shop s, Schedules sc
WHERE o.AddressID=s.AddressID AND sc.addressID= s.addressID
GROUP BY s.ShopName, sc.openingTimes, sc.closingTimes, s.shopName
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 11. 
SELECT e.id, p.fname, p.minit, p.lname
FROM Employee e, Person p
WHERE e.id = p.id AND e.GoldMemberID is NOT NULL;

-- 12.
SELECT shop.ShopName, COUNT(*)
FROM Sells, Shop
WHERE sells.addressID = shop.addressID
GROUP BY shop.shopName
ORDER BY COUNT(*) DESC
LIMIT 1;

-- 13.
SELECT Sells.cost, Product.ProductName, Shop.ShopName
FROM Sells, Product, Shop
WHERE Sells.ProductName = Product.ProductName and Shop.AddressID = Sells.AddressID
ORDER BY Product.ProductName;