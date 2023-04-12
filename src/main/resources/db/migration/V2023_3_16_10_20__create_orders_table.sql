CREATE TABLE if not EXISTS `orders`
(
    `order_id`    bigint(11)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `addressee`   varchar(50)  NOT NULL,
    `address`     varchar(255) NOT NULL,
    `mobile`      varchar(11)  NOT NULL,
    `total_price` decimal(10)  NOT NULL,
    `created_at`  datetime     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;