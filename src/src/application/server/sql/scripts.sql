-- create_database.sql

CREATE DATABASE IF NOT EXISTS deslibrary;

-- drop_tables.sql

DROP TABLE IF EXISTS deslibrary.DocumentState;
DROP TABLE IF EXISTS deslibrary.Document;
DROP TABLE IF EXISTS deslibrary.Subscriber;
DROP TABLE IF EXISTS deslibrary.DocumentChangeLog;
DROP TABLE IF EXISTS deslibrary.Dvd;

-- create_tables.sql

CREATE TABLE deslibrary.DocumentState (
    id INT AUTO_INCREMENT,
    label VARCHAR(30) UNIQUE,
    CONSTRAINT PK_DocumentState PRIMARY KEY (id)
);

CREATE TABLE deslibrary.Document (
    id INT AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    idState INT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Document PRIMARY KEY (id),
    CONSTRAINT FK_DocumentState FOREIGN KEY (idState) REFERENCES DocumentState (id)
);

CREATE TABLE deslibrary.DocumentChangeLog (
    id INT NOT NULL,
    idSubscriber INT,
    time TIMESTAMP NOT NULL,
    CONSTRAINT PK_Command PRIMARY KEY (id),
    CONSTRAINT FK_Command_idSubscriber FOREIGN KEY (idSubscriber) REFERENCES Subscriber (id),
    CONSTRAINT FK_Command_idDocument FOREIGN KEY (id) REFERENCES Document (id)
);

CREATE TABLE deslibrary.Subscriber (
    id INTEGER AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    email VARCHAR(100) NOT NULL,
    isBanned BOOLEAN DEFAULT FALSE,
    bannedUntil TIMESTAMP DEFAULT NULL,
    CONSTRAINT PK_Subcriber PRIMARY KEY (id)
);

CREATE TABLE deslibrary.Dvd (
    id INT,
    isForAdult BOOLEAN,
    CONSTRAINT PK_Dvd PRIMARY KEY (id),
    CONSTRAINT FK_Dvd_id FOREIGN KEY (id) REFERENCES Document (id)
);

-- insert_document_state.sql

INSERT INTO deslibrary.DocumentState (id, label) VALUES (1, 'FREE');
INSERT INTO deslibrary.DocumentState (id, label) VALUES (2, 'RESERVED');
INSERT INTO deslibrary.DocumentState (id, label) VALUES (3, 'BORROWED');

-- insert_adult_documents.sql

INSERT INTO deslibrary.Document (id, title) VALUES (1, "Psychose");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (1, true);
INSERT INTO deslibrary.Document (id, title) VALUES (2, "Inglourious Basterds");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (2, true);
INSERT INTO deslibrary.Document (id, title) VALUES (3, "Blade Runner 2049");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (3, true);

-- insert_non_adult_documents.sql

INSERT INTO deslibrary.Document (id, title) VALUES (4, "Limitless");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (4, false);
INSERT INTO deslibrary.Document (id, title) VALUES (5, "L'Âge De Glace");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (5, false);
INSERT INTO deslibrary.Document (id, title) VALUES (6, "Interstellar");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (6, false);

-- insert_adult_subscribers.sql

INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('René', 'Descartes', 'rené.descartes@cogitoergosum.com', '1596-03-31');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Alfred', 'Hitchcock', 'alfred.hitchcock@hollywood.com', '1899-08-13');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Tobey', 'Maguire', 'tobey.maguire@spiderman.com', '2004-12-27');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Guy', 'de Maupassant', 'guy.demaupassant@bouledesuif.com', '2006-01-04');

-- insert_non_adult_subscribers.sql

INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Sydney', 'Sweeney', 'sydney.sweeney@euphoria.com', '2009-08-28');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Napoléon', 'Bonaparte', 'napoléon.bonaparte@victory.com', '2010-03-15');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Gertrude', 'Ederlé', 'gertrude.ederlé@manche.com', '2008-11-22');
INSERT INTO deslibrary.Subscriber (firstName, lastName, email, birthdate) VALUES ('Gustave', 'Eiffel', 'gustave.eiffel@tower.com', '2011-07-09');
