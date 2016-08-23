-- --------------------------------------------------
-- Creating authantication related tables
-- --------------------------------------------------

CREATE TABLE IF NOT EXISTS `aspnetroles` (
  `Id` varchar(128) NOT NULL,
  `Name` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `unque_name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `aspnetuserclaims` (
  `Id` int(11) NOT NULL,
  `UserId` varchar(128) NOT NULL,
  `ClaimType` text,
  `ClaimValue` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `aspnetuserlogins` (
  `LoginProvider` varchar(127) NOT NULL,
  `ProviderKey` varchar(127) NOT NULL,
  `UserId` varchar(127) NOT NULL,
  KEY `IX_UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `aspnetuserroles` (
  `UserId` varchar(128) NOT NULL,
  `RoleId` varchar(128) NOT NULL,
  PRIMARY KEY (`UserId`,`RoleId`),
  KEY `Index 1` (`UserId`),
  KEY `Index 2` (`RoleId`),
  CONSTRAINT `FK_aspnetuserroles_aspnetroles` FOREIGN KEY (`RoleId`) REFERENCES `aspnetroles` (`Id`),
  CONSTRAINT `FK_aspnetuserroles_aspnetusers` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `aspnetusers` (
  `Id` varchar(127) NOT NULL,
  `DisplayName` text,
  `Email` varchar(255) DEFAULT NULL,
  `EmailConfirmed` bit(1) NOT NULL,
  `PasswordHash` text,
  `SecurityStamp` text,
  `PhoneNumber` text,
  `PhoneNumberConfirmed` bit(1) NOT NULL,
  `TwoFactorEnabled` bit(1) NOT NULL,
  `LockoutEndDateUtc` datetime DEFAULT NULL,
  `LockoutEnabled` bit(1) NOT NULL,
  `AccessFailedCount` int(11) NOT NULL,
  `UserName` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UserNameIndex` (`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `__migrationhistory` (
  `MigrationId` varchar(150) NOT NULL,
  `ContextKey` varchar(300) NOT NULL,
  `Model` blob NOT NULL,
  `ProductVersion` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table `Categories`
CREATE TABLE Categories (
    `ID` int NOT NULL AUTO_INCREMENT,
    `Name` text NOT NULL,
    `Language` char(2)  NULL,
    `IsPublic` bit  NOT NULL,
    `UserID` varchar(128)  NOT NULL,
	PRIMARY KEY(`ID`),
	KEY `IX_FK_AspNetUserCategory` (`UserID`),
	CONSTRAINT `FK_AspNetUserCategory` FOREIGN KEY (`UserID`) REFERENCES `aspnetusers` (`Id`)
);

-- Creating table `FlashCards`
CREATE TABLE FlashCards (
    `ID` int NOT NULL AUTO_INCREMENT,
    `CategoryID` int  NOT NULL,
    `Front` text  NOT NULL,
    `Back` text  NOT NULL,
    `Language` char(2)  NOT NULL,
	PRIMARY KEY(`ID`),
	KEY `IX_FK_CategoryFlashCard` (`CategoryID`),
	CONSTRAINT `FK_CategoryFlashCard` FOREIGN KEY (`CategoryID`) REFERENCES `Categories` (`ID`)
);

-- Creating table `Shares`
CREATE TABLE Shares (
    `SharedUsers_Id` varchar(128)  NOT NULL,
    `SharedCategories_ID` int  NOT NULL,
	PRIMARY KEY(`SharedUsers_Id`, `SharedCategories_ID`),
	KEY `IX_FK_Shares_Category` (`SharedCategories_ID`),
	CONSTRAINT `FK_Shares_AspNetUser` FOREIGN KEY (`SharedUsers_Id`) REFERENCES `aspnetusers` (`Id`),
	CONSTRAINT `FK_Shares_Category` FOREIGN KEY (`SharedCategories_ID`) REFERENCES `Categories` (`ID`)
);