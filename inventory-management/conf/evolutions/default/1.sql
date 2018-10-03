# --- !Ups

CREATE TABLE phones (
  id integer PRIMARY KEY,
  name varchar(40) NOT NULL,
  description varchar(200),
  image_ref varchar(60),
  price numeric NOT NULL
);

INSERT INTO phones(id, name, description, image_ref, price) VALUES(1, 'Moto G2', 'Mobile phone 4G with 10 Mpx. Not bad.', 'moto-g-2014-1.jpg', 150.52);
INSERT INTO phones(id, name, description, image_ref, price) VALUES(2, 'iPhone X', 'Mobile phone 4G very expensive', 'iphone_x.jpeg', 1100.32);
INSERT INTO phones(id, name, description, image_ref, price) VALUES(3, 'Xperia L1', 'Mobile phone 4G from Sony', 'xperia_l1.jpg', 200.00);

# --- !Downs

DROP TABLE phones;