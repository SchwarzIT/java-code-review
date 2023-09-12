DROP TABLE IF EXISTS coupons;
 
CREATE TABLE coupons (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  code  VARCHAR(250) NOT NULL,
  discount NUMBER(10,2) NOT NULL,
  minBasketValue NUMBER(10,2) DEFAULT NULL
);
 
INSERT INTO coupons (code, discount, minBasketValue) VALUES
  ('TEST1', 10.00, 50.00),
  ('TEST2', 15.00, 100.00),
  ('TEST3', 20.00, 200.00);