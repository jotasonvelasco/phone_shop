CREATE TABLE orders (
  id bigserial PRIMARY KEY,
  customer_name varchar(40) NOT NULL,
  customer_surname varchar(40) NOT NULL,
  customer_email varchar(40) NOT NULL,
  total_price numeric NOT NULL
);

CREATE TABLE order_line_items (
  id bigserial PRIMARY KEY,
  order_id bigserial REFERENCES orders (id),
  phone_id integer NOT NULL,
  quantity integer NOT NULL,
  subtotal numeric NOT NULL
);
