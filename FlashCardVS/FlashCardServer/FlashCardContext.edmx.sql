
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 11/21/2015 01:59:07
-- Generated from EDMX file: C:\Users\Bence\Documents\Team Projects\FlashCard\FlashCardVS\FlashCardServer\FlashCardContext.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [aspnet-FlashCardServer-20151012092256];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_CategoryFlashCard]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[FlashCards] DROP CONSTRAINT [FK_CategoryFlashCard];
GO
IF OBJECT_ID(N'[dbo].[FK_AspNetUserCategory]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Categories] DROP CONSTRAINT [FK_AspNetUserCategory];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[Categories]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Categories];
GO
IF OBJECT_ID(N'[dbo].[FlashCards]', 'U') IS NOT NULL
    DROP TABLE [dbo].[FlashCards];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'Categories'
CREATE TABLE [dbo].[Categories] (
    [ID] int IDENTITY(1,1) NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [Language] char(2)  NULL,
    [IsPublic] bit  NOT NULL,
    [UserID] nvarchar(128)  NOT NULL
);
GO

-- Creating table 'FlashCards'
CREATE TABLE [dbo].[FlashCards] (
    [ID] int IDENTITY(1,1) NOT NULL,
    [CategoryID] int  NOT NULL,
    [Front] nvarchar(max)  NOT NULL,
    [Back] nvarchar(max)  NOT NULL,
    [Language] char(2)  NOT NULL
);
GO

-- Creating table 'Shares'
CREATE TABLE [dbo].[Shares] (
    [SharedUsers_Id] nvarchar(128)  NOT NULL,
    [SharedCategories_ID] int  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [ID] in table 'Categories'
ALTER TABLE [dbo].[Categories]
ADD CONSTRAINT [PK_Categories]
    PRIMARY KEY CLUSTERED ([ID] ASC);
GO

-- Creating primary key on [ID] in table 'FlashCards'
ALTER TABLE [dbo].[FlashCards]
ADD CONSTRAINT [PK_FlashCards]
    PRIMARY KEY CLUSTERED ([ID] ASC);
GO

-- Creating primary key on [SharedUsers_Id], [SharedCategories_ID] in table 'Shares'
ALTER TABLE [dbo].[Shares]
ADD CONSTRAINT [PK_Shares]
    PRIMARY KEY CLUSTERED ([SharedUsers_Id], [SharedCategories_ID] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [CategoryID] in table 'FlashCards'
ALTER TABLE [dbo].[FlashCards]
ADD CONSTRAINT [FK_CategoryFlashCard]
    FOREIGN KEY ([CategoryID])
    REFERENCES [dbo].[Categories]
        ([ID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_CategoryFlashCard'
CREATE INDEX [IX_FK_CategoryFlashCard]
ON [dbo].[FlashCards]
    ([CategoryID]);
GO

-- Creating foreign key on [UserID] in table 'Categories'
ALTER TABLE [dbo].[Categories]
ADD CONSTRAINT [FK_AspNetUserCategory]
    FOREIGN KEY ([UserID])
    REFERENCES [dbo].[AspNetUsers]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_AspNetUserCategory'
CREATE INDEX [IX_FK_AspNetUserCategory]
ON [dbo].[Categories]
    ([UserID]);
GO

-- Creating foreign key on [SharedUsers_Id] in table 'Shares'
ALTER TABLE [dbo].[Shares]
ADD CONSTRAINT [FK_Shares_AspNetUser]
    FOREIGN KEY ([SharedUsers_Id])
    REFERENCES [dbo].[AspNetUsers]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [SharedCategories_ID] in table 'Shares'
ALTER TABLE [dbo].[Shares]
ADD CONSTRAINT [FK_Shares_Category]
    FOREIGN KEY ([SharedCategories_ID])
    REFERENCES [dbo].[Categories]
        ([ID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Shares_Category'
CREATE INDEX [IX_FK_Shares_Category]
ON [dbo].[Shares]
    ([SharedCategories_ID]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------