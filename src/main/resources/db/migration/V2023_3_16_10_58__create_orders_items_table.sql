create table orders_items
(
    order_entity_order_id bigint not null,
    items_item_id         bigint not null,
    constraint UK_9uu7j0okufkkanam94v8gb2qa
        unique (items_item_id),
    constraint FKpgyabgbnxabjjjfxqtsnvu12k
        foreign key (items_item_id) references items (item_id),
    constraint FKqi5tq982ov8djw4ptorhhrln0
        foreign key (order_entity_order_id) references orders (order_id)
);