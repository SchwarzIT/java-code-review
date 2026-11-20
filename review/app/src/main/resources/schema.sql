DROP TABLE IF EXISTS baskets;
DROP TABLE IF EXISTS coupons;

CREATE TABLE coupons
(
    code             VARCHAR(250) PRIMARY KEY,
    discount         NUMBER(10,2) NOT NULL,
    min_basket_value NUMBER(10,2) DEFAULT NULL
);

CREATE TABLE baskets
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount           NUMBER(10,2) NOT NULL,
    coupon_code      VARCHAR(250) DEFAULT NULL,
    CONSTRAINT fk_baskets_coupon FOREIGN KEY (coupon_code) REFERENCES coupons (code)
);
