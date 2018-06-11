DROP TABLE Product IF EXISTS;

CREATE TABLE Product (
  id INT IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price BIGINT NOT NULL,
  description VARCHAR(255)
);

INSERT INTO Product VALUES (NULL, 'Sword', 5000, 'Simple and effective weapon');
INSERT INTO Product VALUES (NULL, 'Knife', 2500, 'A compact weapon that can remain unspotted');
INSERT INTO Product VALUES (NULL, 'Double Sword', 30000, 'Obligatory for professionals');
INSERT INTO Product VALUES (NULL, 'Bow', 1000, 'A crude bow made of birch wood and worg sinew');
INSERT INTO Product VALUES (NULL, 'Mithril armor', 25000, 'A rare mithril armor - will make you as tough as a hobbit');