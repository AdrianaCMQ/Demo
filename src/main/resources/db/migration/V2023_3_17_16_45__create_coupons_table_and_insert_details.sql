CREATE TABLE if not EXISTS `coupons`
(
    `coupon_id` bigint(11)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title`     varchar(50)  NOT NULL,
    `details`   varchar(255) NOT NULL DEFAULT ''
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `coupons` (`coupon_id`, `title`, `details`)
VALUES
    (1, '满减券', '满1000元减50元'),
    (2, '折扣券', '同品类满3件打8折'),
    (3, '无门槛券20', '直接减免券面价格20'),
    (4, '无门槛券50', '直接减免券面价格20');