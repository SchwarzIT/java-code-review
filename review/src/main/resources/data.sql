DROP TABLE IF EXISTS coupons;

CREATE TABLE coupons (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR_IGNORECASE(250) NOT NULL UNIQUE,
  discount NUMBER(10,2) NOT NULL,
  min_basket_value NUMBER(10,2) DEFAULT NULL
);

INSERT INTO coupons (code, discount, min_basket_value) VALUES
  ('TEST1', 10.00, 50.00),
  ('TEST2', 15.00, 100.00),
  ('TEST3', 20.00, 200.00);
