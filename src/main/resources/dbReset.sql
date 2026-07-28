
DELETE FROM order_items;

DELETE FROM orders;

DELETE FROM cart_items;

UPDATE products
SET storage_quantity = 1000;
