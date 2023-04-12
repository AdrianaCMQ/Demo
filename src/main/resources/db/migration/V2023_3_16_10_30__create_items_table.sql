CREATE TABLE IF NOT EXISTS `items`
(
    `item_id`    bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` bigint(11)  NOT NULL,
    `quantity`   int(100)    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;