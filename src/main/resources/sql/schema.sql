CREATE EXTENSION IF NOT EXISTS unaccent;

--CUSTOMER
ALTER TABLE public.customers
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--STAFF
ALTER TABLE public.staffs
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--SCORE
ALTER TABLE public.scores
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--ADDRESS
ALTER TABLE public.addresses
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--BUYING_PRICE
ALTER TABLE public.buying_prices
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--SELLING_PRICE
ALTER TABLE public.selling_prices
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--WEIGHT
ALTER TABLE public.weights
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--ROLE
DROP SEQUENCE IF EXISTS roles_seq CASCADE;
CREATE SEQUENCE roles_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.roles
    ALTER COLUMN role_id SET DEFAULT nextval('roles_seq'),
    ALTER COLUMN is_activated SET DEFAULT true,
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PERMISSION
DROP SEQUENCE IF EXISTS permissions_seq CASCADE;
CREATE SEQUENCE permissions_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.permissions
    ALTER COLUMN permission_id SET DEFAULT nextval('permissions_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--ITEM
DROP SEQUENCE IF EXISTS items_seq CASCADE;
CREATE SEQUENCE items_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.items
    ALTER COLUMN item_id SET DEFAULT nextval('items_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PRODUCT
DROP SEQUENCE IF EXISTS products_seq CASCADE;
CREATE SEQUENCE products_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.products
    ALTER COLUMN product_id SET DEFAULT nextval('products_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PRODUCT_IMAGE
DROP SEQUENCE IF EXISTS product_images_seq CASCADE;
CREATE SEQUENCE product_images_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.product_images
    ALTER COLUMN product_image_id SET DEFAULT nextval('product_images_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--CART
DROP SEQUENCE IF EXISTS carts_seq CASCADE;
CREATE SEQUENCE carts_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.carts
    ALTER COLUMN cart_id SET DEFAULT nextval('carts_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--CART_DETAIL
DROP SEQUENCE IF EXISTS cart_details_seq CASCADE;
CREATE SEQUENCE cart_details_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.cart_details
    ALTER COLUMN cart_detail_id SET DEFAULT nextval('cart_details_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--SELLING_ORDER
-- ALTER TABLE public.selling_orders
--     ALTER COLUMN customer_id DROP NOT NULL;

--SELLING_ORDER_DETAIL
DROP SEQUENCE IF EXISTS selling_order_details_seq CASCADE;
CREATE SEQUENCE selling_order_details_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.selling_order_details
    ALTER COLUMN selling_order_detail_id SET DEFAULT nextval('selling_order_details_seq');

--ORDER_STATUS
DROP SEQUENCE IF EXISTS order_statuses_seq CASCADE;
CREATE SEQUENCE order_statuses_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.order_statuses
    ALTER COLUMN order_status_id SET DEFAULT nextval('order_statuses_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--PAYMENTMETHOD
DROP SEQUENCE IF EXISTS payment_methods_seq CASCADE;
CREATE SEQUENCE payment_methods_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.payment_methods
    ALTER COLUMN payment_method_id SET DEFAULT nextval('payment_methods_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--TRANSACTION
ALTER TABLE public.transactions
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--VOUCHER
DROP SEQUENCE IF EXISTS vouchers_seq CASCADE;
CREATE SEQUENCE vouchers_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.vouchers
    ALTER COLUMN voucher_id SET DEFAULT nextval('vouchers_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

--USED_VOUCHER
DROP SEQUENCE IF EXISTS used_vouchers_seq CASCADE;
CREATE SEQUENCE used_vouchers_seq
    START WITH 1
    INCREMENT BY 1;
ALTER TABLE public.used_vouchers
    ALTER COLUMN used_voucher_id SET DEFAULT nextval('used_vouchers_seq'),
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;