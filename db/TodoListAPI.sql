DROP DATABASE IF EXISTS TodoListAPI;
GO
CREATE DATABASE TodoListAPI;
GO
USE TodoListAPI;
GO
CREATE TABLE dbo.[User]
(
    ID        INT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    Name      NVARCHAR(255)       NOT NULL,
    Email     NVARCHAR(255)       NOT NULL UNIQUE,
    Password  NVARCHAR(255)       NOT NULL,
    CreatedAt DATETIME2 DEFAULT SYSUTCDATETIME(),
    UpdatedAt DATETIME2 DEFAULT SYSUTCDATETIME()
);
GO
CREATE TRIGGER trg_Update_User_UpdatedAt
    ON dbo.[User]
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.[User]
    SET UpdatedAt = SYSUTCDATETIME()
    FROM dbo.[User] u
             INNER JOIN inserted i ON u.ID = i.ID;
END;
GO
CREATE TABLE dbo.Task
(
    ID          INT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    UserID      INT                 NOT NULL,
    Title       NVARCHAR(255)       NOT NULL,
    Description NVARCHAR(500),
    CreatedAt   DATETIME2 DEFAULT SYSUTCDATETIME(),
    UpdatedAt   DATETIME2 DEFAULT SYSUTCDATETIME(),
    CONSTRAINT FK_Task_User FOREIGN KEY (UserID) REFERENCES dbo.[User] (ID) ON DELETE CASCADE
);
GO
CREATE TRIGGER trg_Update_Task_UpdatedAt
    ON dbo.Task
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.Task
    SET UpdatedAt = SYSUTCDATETIME()
    FROM dbo.Task t
             INNER JOIN inserted i ON t.ID = i.ID;
END;
GO