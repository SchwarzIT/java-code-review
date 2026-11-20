INSERT INTO coupons (code, discount, min_basket_value)
VALUES ('TEST1', 10.00, 50.00),
       ('TEST2', 15.00, 100.00),
       ('TEST3', 20.00, 200.00);

INSERT INTO baskets (amount)
VALUES (50.00),
       (100.00),
       (200.00);
