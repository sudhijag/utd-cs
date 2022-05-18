-- Schema dbproject
CREATE SCHEMA IF NOT EXISTS dbproject ;
USE dbproject;

-- Table Address

CREATE TABLE IF NOT EXISTS Address (
  addressID INT NOT NULL,
  AddressNo VARCHAR(45) NULL,
  AddressStreet VARCHAR(45) NULL,
  ApartmentNo VARCHAR(45) NULL,
  City VARCHAR(45) NULL,
  Zip VARCHAR(45) NULL,
  PRIMARY KEY (addressID))
;



-- Table Person

CREATE TABLE IF NOT EXISTS Person (
  id VARCHAR(45) NOT NULL,
  fname VARCHAR(45) NULL,
  minit VARCHAR(45) NULL,
  lname VARCHAR(45) NULL,
  AddressID INT NULL,
  PRIMARY KEY (id),
  INDEX asd2_idx (AddressID ASC) ,
  CONSTRAINT asd2
    FOREIGN KEY (AddressID)
    REFERENCES Address (addressID)
    
   )
;



-- Table GoldMember

CREATE TABLE IF NOT EXISTS GoldMember (
  GoldMemberID VARCHAR(45) NOT NULL,
  PRIMARY KEY (GoldMemberID))
;



-- Table Employee

CREATE TABLE IF NOT EXISTS Employee (
  id VARCHAR(45) NOT NULL,
  CHECK (id REGEXP "E[0-9][0-9][0-9]"),
  startDateDesignation DATETIME NULL,
  dateofBirth DATETIME NULL,
  dateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CHECK (dateofBirth < (Date_sub(dateCreated, INTERVAL 16 YEAR))),
  Gender VARCHAR(45) NULL,
  GoldMemberID VARCHAR(45) NULL,
  PRIMARY KEY (id),
  INDEX gm78_idx (GoldMemberID ASC) ,
  CONSTRAINT id
    FOREIGN KEY (id)
    REFERENCES Person (id)
    
   ,
  CONSTRAINT gm78
    FOREIGN KEY (GoldMemberID)
    REFERENCES GoldMember (GoldMemberID)
    
   )
;



-- Table AreaManager

CREATE TABLE IF NOT EXISTS AreaManager (
  id VARCHAR(45) NOT NULL,
  Area VARCHAR(45) NULL,
  PRIMARY KEY (id),
  CONSTRAINT id2
    FOREIGN KEY (id)
    REFERENCES Employee (id)
    
   )
;



-- Table MakerModel

CREATE TABLE IF NOT EXISTS MakerModel (
  maker VARCHAR(45) NOT NULL,
  model VARCHAR(45) NULL,
  PRIMARY KEY (maker))
;



-- Table Vehicle

CREATE TABLE IF NOT EXISTS Vehicle (
  PlateNo INT NOT NULL,
  Color VARCHAR(45) NULL,
  Maker VARCHAR(45) NULL,
  PRIMARY KEY (PlateNo),
  INDEX maker_idx (Maker ASC) ,
  CONSTRAINT maker
    FOREIGN KEY (Maker)
    REFERENCES MakerModel (maker)
    
   )
;



-- Table PhoneNumber

CREATE TABLE IF NOT EXISTS PhoneNumber (
  id VARCHAR(45) NOT NULL,
  number VARCHAR(45) NULL,
  PRIMARY KEY (id),
  CONSTRAINT id22
    FOREIGN KEY (id)
    REFERENCES Person (id)
    
   )
;



-- Table Staff

CREATE TABLE IF NOT EXISTS Staff (
  id VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT person5
    FOREIGN KEY (id)
    REFERENCES Person (id)
    
   )
;



-- Table Customer

CREATE TABLE IF NOT EXISTS Customer (
  customerID VARCHAR(45) NOT NULL,
  joinDate DATETIME NULL,
  PRIMARY KEY (customerID),
  CONSTRAINT Person
    FOREIGN KEY (customerID)
    REFERENCES Person (id)
    
   )
;



-- Table SilverMember

CREATE TABLE IF NOT EXISTS SilverMember (
  id VARCHAR(45) NOT NULL,
  silverEarnDate DATETIME NULL,
  GoldMemberID VARCHAR(45) NULL,
  PRIMARY KEY (id),
  INDEX Gol78_idx (GoldMemberID ASC) ,
  CONSTRAINT Customer0
    FOREIGN KEY (id)
    REFERENCES Customer (customerID)
    
   ,
  CONSTRAINT Gol78
    FOREIGN KEY (GoldMemberID)
    REFERENCES GoldMember (GoldMemberID)
    
   )
;



-- Table Card

CREATE TABLE IF NOT EXISTS Card (
  id INT NOT NULL,
  cardNumber VARCHAR(45) NULL,
  silverCustomerID VARCHAR(45) NULL,
  PRIMARY KEY (id),
  INDEX sc2_idx (silverCustomerID ASC) ,
  CONSTRAINT sc2
    FOREIGN KEY (silverCustomerID)
    REFERENCES SilverMember (id)
    
   )
;



-- Table GoldCard

CREATE TABLE IF NOT EXISTS GoldCard (
  id VARCHAR(45) NOT NULL,
  cardID INT NOT NULL,
  DeliveriesLeft INT NULL,
  PRIMARY KEY (id, cardID),
  CONSTRAINT GoldMember33
    FOREIGN KEY (id)
    REFERENCES GoldMember (GoldMemberID)
    
   )
;



-- Table Shop

CREATE TABLE IF NOT EXISTS Shop (
  addressID INT NOT NULL,
  shopName VARCHAR(45) NULL,
  BusinessPhone VARCHAR(45) NULL,
  PRIMARY KEY (addressID),
  CONSTRAINT tet23
    FOREIGN KEY (addressID)
    REFERENCES Address (addressID)
    
   )
;



-- Table Comment

CREATE TABLE IF NOT EXISTS Comment (
  CustomerID VARCHAR(45) NOT NULL,
  AddressID INT NOT NULL,
  Content VARCHAR(45) NULL,
  Rating INT NULL,
  PRIMARY KEY (CustomerID, AddressID),
  INDEX Shop_idx (AddressID ASC) ,
  CONSTRAINT Customer1
    FOREIGN KEY (CustomerID)
    REFERENCES Customer (customerID)
    
   ,
  CONSTRAINT Shop1
    FOREIGN KEY (AddressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table Restaurant

CREATE TABLE IF NOT EXISTS Restaurant (
  AddressID INT NOT NULL,
  rArea VARCHAR(45) NULL,
  PRIMARY KEY (AddressID),
  CONSTRAINT Shop333
    FOREIGN KEY (AddressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table OrdinaryCustomer

CREATE TABLE IF NOT EXISTS OrdinaryCustomer (
  id VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT Customer33
    FOREIGN KEY (id)
    REFERENCES Customer (customerID)
    
   )
;



-- Table Supermarket

CREATE TABLE IF NOT EXISTS Supermarket (
  AddressID INT NOT NULL,
  PRIMARY KEY (AddressID),
  CONSTRAINT `random`
    FOREIGN KEY (AddressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table Product

CREATE TABLE IF NOT EXISTS Product (
  ProductName VARCHAR(45) NOT NULL,
  PRIMARY KEY (ProductName));



-- Table Sells

CREATE TABLE IF NOT EXISTS Sells (
  AddressID INT NOT NULL,
  ProductName VARCHAR(45) NOT NULL,
  Cost VARCHAR(45) NULL,
  PRIMARY KEY (AddressID, ProductName),
  INDEX Produc3_idx (ProductName ASC) ,
  CONSTRAINT Supermarket56
    FOREIGN KEY (AddressID)
    REFERENCES Supermarket (AddressID)
    
   ,
  CONSTRAINT Produc3
    FOREIGN KEY (ProductName)
    REFERENCES Product (ProductName)
    
   )
;



-- Table Deliverer

CREATE TABLE IF NOT EXISTS Deliverer (
  id VARCHAR(45) NOT NULL,
  AreaManagerID VARCHAR(45) NULL,
  PRIMARY KEY (id),
  INDEX AreaManagerID_idx (AreaManagerID ASC) ,
  CONSTRAINT AreaManagerID
    FOREIGN KEY (AreaManagerID)
    REFERENCES AreaManager (id)
    
   )
;



-- Table Orders

CREATE TABLE IF NOT EXISTS Orders (
  OrderID INT NOT NULL,
  CustomerID VARCHAR(45) NULL,
  AddressID INT NULL,
  delivererID VARCHAR(45) NULL,
  TotalBalance VARCHAR(45) NULL,
  TipsReceived DOUBLE NULL,
  PRIMARY KEY (OrderID),
  INDEX deliv_idx (delivererID ASC) ,
  INDEX cjm2_idx (CustomerID ASC) ,
  CONSTRAINT Rest42
    FOREIGN KEY (AddressID)
    REFERENCES Restaurant (AddressID)
   ,
  CONSTRAINT delihg
    FOREIGN KEY (delivererID)
    REFERENCES Deliverer (id)
   ,
  CONSTRAINT cjm2
    FOREIGN KEY (CustomerID)
    REFERENCES Customer (customerID)
    
   )
;



-- Table OrderContents

CREATE TABLE IF NOT EXISTS OrderContents (
  OrderID INT NOT NULL,
  Contents VARCHAR(45) NULL,
  PRIMARY KEY (OrderID),
  INDEX a_idx (OrderID ASC) ,
  CONSTRAINT a
    FOREIGN KEY (OrderID)
    REFERENCES Orders (OrderID)
    
   )
;


-- Table Schedules

CREATE TABLE IF NOT EXISTS Schedules (
  AddressID INT NOT NULL,
  OpeningTimes VARCHAR(45) NULL,
  ClosingTimes VARCHAR(45) NULL,
  PRIMARY KEY (AddressID),
  CONSTRAINT Shop0
    FOREIGN KEY (AddressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table Payment

CREATE TABLE IF NOT EXISTS Payment (
  OrderID INT NOT NULL,
  PaymentConfirmNo INT NOT NULL,
  Type VARCHAR(45) NULL,
  DatePaid DATETIME NULL,
  PRIMARY KEY (PaymentConfirmNo, OrderID),
  INDEX Ordyk12_idx (OrderID ASC) ,
  CONSTRAINT Ordyk12
    FOREIGN KEY (OrderID)
    REFERENCES Orders (OrderID)
    
   )
;



-- Table Type

CREATE TABLE IF NOT EXISTS Type (
  addressID INT NOT NULL,
  Type VARCHAR(45) NOT NULL,
  PRIMARY KEY (addressID, Type),
  CONSTRAINT resr2
    FOREIGN KEY (addressID)
    REFERENCES Restaurant (AddressID)
    
   )
;



-- Table RegisteredVehicle

CREATE TABLE IF NOT EXISTS RegisteredVehicle (
  PlateNo INT NOT NULL,
  DelivererID VARCHAR(45) NOT NULL,
  PRIMARY KEY (PlateNo, DelivererID),
  INDEX Delv21_idx (DelivererID ASC) ,
  CONSTRAINT Delv21
    FOREIGN KEY (DelivererID)
    REFERENCES Deliverer (id)
    
   ,
  CONSTRAINT vhe2
    FOREIGN KEY (PlateNo)
    REFERENCES Vehicle (PlateNo)
    
   )
;



-- Table Contract

CREATE TABLE IF NOT EXISTS Contract (
  areaManagerID VARCHAR(45) NOT NULL,
  addressID INT NOT NULL,
  contractStartDate DATETIME NULL,
  PRIMARY KEY (areaManagerID, addressID),
  INDEX Sh21_idx (addressID ASC) ,
  CONSTRAINT am53
    FOREIGN KEY (areaManagerID)
    REFERENCES AreaManager (id)
    
   ,
  CONSTRAINT Sh21
    FOREIGN KEY (addressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table ShopPromotion

CREATE TABLE IF NOT EXISTS ShopPromotion (
  addressID INT NOT NULL,
  PromotionCode VARCHAR(45) NOT NULL,
  Description VARCHAR(45) NULL,
  PRIMARY KEY (addressID, PromotionCode),
  CONSTRAINT Sfv
    FOREIGN KEY (addressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table OfferedPromotions

CREATE TABLE IF NOT EXISTS OfferedPromotions (
  addressID INT NOT NULL,
  PromotionCode VARCHAR(45) NOT NULL,
  description VARCHAR(45) NULL,
  PRIMARY KEY (addressID, PromotionCode),
  CONSTRAINT gbfd
    FOREIGN KEY (addressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table Food

CREATE TABLE IF NOT EXISTS Food (
  foodName VARCHAR(45) NOT NULL,
  PRIMARY KEY (foodName),
  CONSTRAINT pbmn
    FOREIGN KEY (foodName)
    REFERENCES Product (ProductName)
    
   )
;



-- Table PrefersFood

CREATE TABLE IF NOT EXISTS PrefersFood (
  customerID VARCHAR(45) NOT NULL,
  foodName VARCHAR(45) NOT NULL,
  PRIMARY KEY (customerID, foodName),
  INDEX foh_idx (foodName ASC) ,
  CONSTRAINT casd
    FOREIGN KEY (customerID)
    REFERENCES Customer (customerID)
    
   ,
  CONSTRAINT foh
    FOREIGN KEY (foodName)
    REFERENCES Food (foodName)
    
   )
;



-- Table Sells_Food

CREATE TABLE IF NOT EXISTS Sells_Food (
  foodName VARCHAR(45) NOT NULL,
  addressID INT NOT NULL,
  PRIMARY KEY (foodName, addressID),
  INDEX sip2_idx (addressID ASC) ,
  CONSTRAINT sip2
    FOREIGN KEY (addressID)
    REFERENCES Supermarket (AddressID)
    
   ,
  CONSTRAINT fomb
    FOREIGN KEY (foodName)
    REFERENCES Food (foodName)
    
   )
;



-- Table ShopsAt

CREATE TABLE IF NOT EXISTS ShopsAt (
  customerID VARCHAR(45) NOT NULL,
  addressID INT NOT NULL,
  PRIMARY KEY (customerID, addressID),
  INDEX adjbm_idx (addressID ASC) ,
  CONSTRAINT chnam
    FOREIGN KEY (customerID)
    REFERENCES Customer (customerID)
    
   ,
  CONSTRAINT adjbm
    FOREIGN KEY (addressID)
    REFERENCES Shop (addressID)
    
   )
;



-- Table Visits

CREATE TABLE IF NOT EXISTS Visits (
  customerID VARCHAR(45) NOT NULL,
  addressID INT NOT NULL,
  PRIMARY KEY (customerID, addressID),
  INDEX abnbm_idx (addressID ASC) ,
  CONSTRAINT nmnjas
    FOREIGN KEY (customerID)
    REFERENCES Customer (customerID)
    
   ,
  CONSTRAINT abnbm
    FOREIGN KEY (addressID)
    REFERENCES Shop (addressID)
    
   )
;

-- AppliedPromotions
ALTER TABLE OfferedPromotions ADD INDEX pk_idx1 (PromotionCode, addressID); -- this is necessary to avoid the error!!

CREATE TABLE IF NOT EXISTS AppliedPromotions (
OrderID INT NOT NULL,
PromotionCode VARCHAR(45) NOT NULL,
addressID INT NOT NULL,
PRIMARY KEY (OrderID, PromotionCode, addressID),
CONSTRAINT Ormn
FOREIGN KEY (OrderID)
REFERENCES Orders (OrderID)
,
CONSTRAINT Rfkg
FOREIGN KEY (PromotionCode, addressID)
REFERENCES OfferedPromotions (PromotionCode, addressID)

);
