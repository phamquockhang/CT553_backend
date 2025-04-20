--ROLE
INSERT INTO public.roles (name, description)
VALUES ('MANAGER', 'Quản lý'),
       ('STAFF', 'Nhân viên'),
       ('CUSTOMER', 'Khách hàng');

--PERMISSION
INSERT INTO public.permissions (name, api_path, method, module)
VALUES ('Get all roles', '/api/v1/roles', 'GET', 'ROLE'),
       ('Get a role', '/api/v1/roles/{id}', 'GET', 'ROLE'),
       ('Create a role', '/api/v1/roles', 'POST', 'ROLE'),
       ('Update a role', '/api/v1/roles/{id}', 'PUT', 'ROLE'),
       ('Delete a role', '/api/v1/roles/{id}', 'DELETE', 'ROLE'),

       ('Get all permissions', '/api/v1/permissions', 'GET', 'PERMISSION'),
       ('Get a permission', '/api/v1/permissions/{id}', 'GET', 'PERMISSION'),
       ('Create a permission', '/api/v1/permissions', 'POST', 'PERMISSION'),
       ('Update a permission', '/api/v1/permissions/{id}', 'PUT', 'PERMISSION'),
       ('Delete a permission', '/api/v1/permissions/{id}', 'DELETE', 'PERMISSION'),

       ('Get all staffs', '/api/v1/staffs', 'GET', 'STAFF'),
       ('Get a staff', '/api/v1/staffs/{id}', 'GET', 'STAFF'),
       ('Create a staff', '/api/v1/staffs', 'POST', 'STAFF'),
       ('Update a staff', '/api/v1/staffs/{id}', 'PUT', 'STAFF'),
       ('Delete a staff', '/api/v1/staffs/{id}', 'DELETE', 'STAFF'),

       ('Get all customers', '/api/v1/customers', 'GET', 'CUSTOMER'),
       ('Get a customer', '/api/v1/customers/{id}', 'GET', 'CUSTOMER'),
       ('Create a customer', '/api/v1/customers', 'POST', 'CUSTOMER'),
       ('Update a customer', '/api/v1/customers/{id}', 'PUT', 'CUSTOMER'),
       ('Delete a customer', '/api/v1/customers/{id}', 'DELETE', 'CUSTOMER'),

       ('Get all items', '/api/v1/items', 'GET', 'ITEM'),
       ('Get a item', '/api/v1/items/{id}', 'GET', 'ITEM'),
       ('Create a item', '/api/v1/items', 'POST', 'ITEM'),
       ('Update a item', '/api/v1/items/{id}', 'PUT', 'ITEM'),
       ('Delete a item', '/api/v1/items/{id}', 'DELETE', 'ITEM'),

       ('Get all products', '/api/v1/products', 'GET', 'PRODUCT'),
       ('Get a product', '/api/v1/products/{id}', 'GET', 'PRODUCT'),
       ('Create a product', '/api/v1/products', 'POST', 'PRODUCT'),
       ('Update a product', '/api/v1/products/{id}', 'PUT', 'PRODUCT'),
       ('Delete a product', '/api/v1/products/{id}', 'DELETE', 'PRODUCT'),

       ('Get all selling orders', '/api/v1/selling_orders', 'GET', 'SELLING ORDER'),
       ('Get a selling order', '/api/v1/selling_orders/{id}', 'GET', 'SELLING ORDER'),
       ('Create a selling order', '/api/v1/selling_orders', 'POST', 'SELLING ORDER'),
       ('Update a selling order', '/api/v1/selling_orders/{id}', 'PUT', 'SELLING ORDER'),

       ('Get all buying orders', '/api/v1/buying_orders', 'GET', 'BUYING ORDER'),
       ('Get a buying order', '/api/v1/buying_orders/{id}', 'GET', 'BUYING ORDER'),
       ('Create a buying order', '/api/v1/buying_orders', 'POST', 'BUYING ORDER'),
       ('Update a buying order', '/api/v1/buying_orders/{id}', 'PUT', 'BUYING ORDER'),

       ('Get all order statuses', '/api/v1/order_statuses', 'GET', 'ORDER STATUS'),
       ('Get all order statuses by order', '/api/v1/order_statuses/{id}', 'GET', 'ORDER STATUS'),
       ('Create a order status', '/api/v1/order_statuses', 'POST', 'ORDER STATUS'),

       ('Get all carts', '/api/v1/carts', 'GET', 'CART'),
       ('Get a cart', '/api/v1/carts/{id}', 'GET', 'CART'),
       ('Create a cart', '/api/v1/carts', 'POST', 'CART'),
       ('Update a cart', '/api/v1/carts/{id}', 'PUT', 'CART'),
       ('Delete a cart', '/api/v1/carts/{id}', 'DELETE', 'CART'),

       ('Get all addresses', '/api/v1/addresses', 'GET', 'ADDRESS'),
       ('Get a address', '/api/v1/addresses/{id}', 'GET', 'ADDRESS'),
       ('Create a address', '/api/v1/addresses', 'POST', 'ADDRESS'),
       ('Update a address', '/api/v1/addresses/{id}', 'PUT', 'ADDRESS'),
       ('Delete a address', '/api/v1/addresses/{id}', 'DELETE', 'ADDRESS'),

       ('Get all scores', '/api/v1/scores', 'GET', 'SCORE'),
       ('Get a score', '/api/v1/scores/{id}', 'GET', 'SCORE'),
       ('Create a score', '/api/v1/scores', 'POST', 'SCORE'),
       ('Update a score', '/api/v1/scores/{id}', 'PUT', 'SCORE'),
       ('Delete a score', '/api/v1/scores/{id}', 'DELETE', 'SCORE'),

       ('Get all product images', '/api/v1/product_images', 'GET', 'PRODUCT IMAGE'),
       ('Get a product image', '/api/v1/product_images/{id}', 'GET', 'PRODUCT IMAGE'),
       ('Create a product image', '/api/v1/product_images', 'POST', 'PRODUCT IMAGE'),
       ('Update a product image', '/api/v1/product_images/{id}', 'PUT', 'PRODUCT IMAGE'),
       ('Delete a product image', '/api/v1/product_images/{id}', 'DELETE', 'PRODUCT IMAGE'),

       ('Get all buying prices', '/api/v1/buying_prices', 'GET', 'BUYING PRICE'),
       ('Get a buying price', '/api/v1/buying_prices/{id}', 'GET', 'BUYING PRICE'),
       ('Create a buying price', '/api/v1/buying_prices', 'POST', 'BUYING PRICE'),
       ('Update a buying price', '/api/v1/buying_prices/{id}', 'PUT', 'BUYING PRICE'),
       ('Delete a buying price', '/api/v1/buying_prices/{id}', 'DELETE', 'BUYING PRICE'),

       ('Get all selling prices', '/api/v1/selling_prices', 'GET', 'SELLING PRICE'),
       ('Get a selling price', '/api/v1/selling_prices/{id}', 'GET', 'SELLING PRICE'),
       ('Create a selling price', '/api/v1/selling_prices', 'POST', 'SELLING PRICE'),
       ('Update a selling price', '/api/v1/selling_prices/{id}', 'PUT', 'SELLING PRICE'),
       ('Delete a selling price', '/api/v1/selling_prices/{id}', 'DELETE', 'SELLING PRICE'),

       ('Get all weights', '/api/v1/weights', 'GET', 'WEIGHT'),
       ('Get a weight', '/api/v1/weights/{id}', 'GET', 'WEIGHT'),
       ('Create a weight', '/api/v1/weights', 'POST', 'WEIGHT'),
       ('Update a weight', '/api/v1/weights/{id}', 'PUT', 'WEIGHT'),
       ('Delete a weight', '/api/v1/weights/{id}', 'DELETE', 'WEIGHT'),

       ('Get all payment methods', '/api/v1/payment_methods', 'GET', 'PAYMENT METHOD'),
       ('Get a payment method', '/api/v1/payment_methods/{id}', 'GET', 'PAYMENT METHOD'),
       ('Create a payment method', '/api/v1/payment_methods', 'POST', 'PAYMENT METHOD'),
       ('Update a payment method', '/api/v1/payment_methods/{id}', 'PUT', 'PAYMENT METHOD'),
       ('Delete a payment method', '/api/v1/payment_methods/{id}', 'DELETE', 'PAYMENT METHOD'),

       ('Get all transactions', '/api/v1/transactions', 'GET', 'TRANSACTION'),
       ('Get a transaction', '/api/v1/transactions/{id}', 'GET', 'TRANSACTION'),
       ('Create a transaction', '/api/v1/transactions', 'POST', 'TRANSACTION'),

       ('Get all vouchers', '/api/v1/vouchers', 'GET', 'VOUCHER'),
       ('Get a voucher', '/api/v1/vouchers/{id}', 'GET', 'VOUCHER'),
       ('Create a voucher', '/api/v1/vouchers', 'POST', 'VOUCHER'),
       ('Update a voucher', '/api/v1/vouchers/{id}', 'PUT', 'VOUCHER'),
       ('Delete a voucher', '/api/v1/vouchers/{id}', 'DELETE', 'VOUCHER'),

       ('Get all conversations', '/api/v1/conversations', 'GET', 'CONVERSATION'),
       ('Get a conversation', '/api/v1/conversations/{id}', 'GET', 'CONVERSATION'),
       ('Create a conversation', '/api/v1/conversations', 'POST', 'CONVERSATION'),
       ('Update a conversation', '/api/v1/conversations/{id}', 'PUT', 'CONVERSATION'),
       ('Delete a conversation', '/api/v1/conversations/{id}', 'DELETE', 'CONVERSATION'),

       ('Get all messages', '/api/v1/messages', 'GET', 'MESSAGE'),
       ('Get a message', '/api/v1/messages/{id}', 'GET', 'MESSAGE'),
       ('Create a message', '/api/v1/messages', 'POST', 'MESSAGE'),
       ('Update a message', '/api/v1/messages/{id}', 'PUT', 'MESSAGE'),
       ('Delete a message', '/api/v1/messages/{id}', 'DELETE', 'MESSAGE');

--ROLE_PERMISSION FOR STAFF
INSERT INTO public.permission_role (role_id, permission_id)
VALUES (2, 18),
       (2, 21),
       (2, 22),
       (2, 26),
       (2, 27),
       (2, 31),
       (2, 32),
       (2, 33),
       (2, 34),
       (2, 85),
       (2, 86),
       (2, 90),
       (2, 91),
       (2, 96),
       (2, 97);

--ROLE_PERMISSION FOR CUSTOMER
-- INSERT INTO public.permission_role (role_id, permission_id)
-- VALUES (3, 6);

--CUSTOMER
INSERT INTO public.customers (customer_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'customer1@gmail.com', 'MALE', 'CUSTOMER 1', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-01'),
       (gen_random_uuid(), 'customer2@gmail.com', 'FEMALE', 'CUSTOMER 2', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-02'),
       (gen_random_uuid(), 'customer3@gmail.com', 'FEMALE', 'CUSTOMER 3', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-03'),
       (gen_random_uuid(), 'customer4@gmail.com', 'MALE', 'CUSTOMER 4', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-04');

--MANAGER
INSERT INTO public.staffs (staff_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'manager@gmail.com', 'MALE', 'MANAGER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 1, true, '2003-08-11');

--STAFF
INSERT INTO public.staffs (staff_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'staff1@gmail.com', 'FEMALE', 'STAFF 1', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-01'),
       (gen_random_uuid(), 'staff2@gmail.com', 'FEMALE', 'STAFF 2', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-02'),
       (gen_random_uuid(), 'staff3@gmail.com', 'MALE', 'STAFF 3', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff4@gmail.com', 'MALE', 'STAFF 4', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-04'),
       (gen_random_uuid(), 'staff5@gmail.com', 'MALE', 'STAFF 5', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-05'),
       (gen_random_uuid(), 'staff6@gmail.com', 'MALE', 'STAFF 6', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-06'),
       (gen_random_uuid(), 'staff7@gmail.com', 'MALE', 'STAFF 7', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-07'),
       (gen_random_uuid(), 'staff8@gmail.com', 'MALE', 'STAFF 8', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-08'),
       (gen_random_uuid(), 'staff9@gmail.com', 'MALE', 'STAFF 9', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-09'),
       (gen_random_uuid(), 'staff10@gmail.com', 'MALE', 'STAFF 10', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-10'),


       (gen_random_uuid(), 'supporter@gmail.com', 'MALE', 'Supporter', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-10');

--SCORE
INSERT INTO public.scores (score_id, customer_id, change_amount, new_value, is_current)
VALUES (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), 100, 100,
        true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer2@gmail.com'), 200, 200,
        true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer3@gmail.com'), 30, 30,
        true);
--        (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), -10, 90, true);

--ADDRESS
INSERT INTO public.addresses (address_id, customer_id, province_id, district_id, ward_code, description, is_default)
VALUES (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), 252, 1782,
        '610201', 'Khóm 1', true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer2@gmail.com'), 252, 1782,
        '610201', 'Khóm 2', true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer3@gmail.com'), 252, 1782,
        '610201', 'Khóm 3', true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer4@gmail.com'), 252, 1782,
        '610201', 'Khóm 4', true);

--ITEM
INSERT INTO public.items (item_name, is_activated)
VALUES --Tôm hùm
       ('Tôm hùm', true),

       --Tôm càng
       ('Tôm càng', true),

       --Tôm thẻ
       ('Tôm thẻ', true),

       --Tôm sú
       ('Tôm sú', true),

       --Cua
       ('Cua', true);

--PRODUCT
INSERT INTO public.products (product_name, product_unit, description, item_id, is_activated)
VALUES
    --Tôm hùm
    ('Tôm hùm Alaska nhỏ', 'con', 'Tôm hùm Alaska nhỏ có trọng lượng từ 450-550g/con', 1, true),
    ('Tôm hùm Alaska vừa', 'con', 'Tôm hùm Alaska vừa có trọng lượng từ 550-900g/con', 1, true),
    ('Tôm hùm Alaska lớn', 'con', 'Tôm hùm Alaska lớn có trọng lượng từ 0,9-3kg/con', 1, true),
    ('Tôm hùm bông vừa', 'con', 'Tôm hùm bông vừa có trọng lượng từ 500-700g/con', 1, true),
    ('Tôm hùm bông lớn', 'con', 'Tôm hùm bông lớn có trọng lượng từ 700-900g/con', 1, true),
    ('Tôm hùm xanh', 'kg', 'Tôm hùm xanh tươi sống có trọng lượng từ 300-400g/con', 1, true),

    --Tôm càng
    ('Tôm càng xanh hàng 2', 'kg', 'Tôm càng xanh hàng 2 có trọng lượng từ 15-24con/kg', 2, true),
    ('Tôm càng xanh hàng 3', 'kg', 'Tôm càng xanh hàng 3 có trọng lượng từ 25-34con/kg', 2, true),
    ('Tôm càng xanh hàng 4', 'kg', 'Tôm càng xanh hàng 4 có trọng lượng từ 35-44con/kg', 2, true),
    ('Tôm càng xanh hàng 5', 'kg', 'Tôm càng xanh hàng 5 có trọng lượng từ 45-54con/kg', 2, true),

    --Tôm thẻ
    ('Tôm thẻ hàng 3', 'kg', 'Tôm thẻ hàng 3 có trọng lượng từ 25-34con/kg', 3, true),
    ('Tôm thẻ hàng 4', 'kg', 'Tôm thẻ hàng 4 có trọng lượng từ 35-44con/kg', 3, true),
    ('Tôm thẻ hàng 5', 'kg', 'Tôm thẻ hàng 5 có trọng lượng từ 45-54con/kg', 3, true),
    ('Tôm thẻ hàng 6', 'kg', 'Tôm thẻ hàng 6 có trọng lượng từ 55-64con/kg', 3, true),

    --Tôm sú
    ('Tôm sú hàng 2', 'kg', 'Tôm sú hàng 2 có trọng lượng từ 15-24con/kg', 4, true),
    ('Tôm sú hàng 3', 'kg', 'Tôm sú hàng 3 có trọng lượng từ 25-34con/kg', 4, true),
    ('Tôm sú hàng 4', 'kg', 'Tôm sú hàng 4 có trọng lượng từ 35-44con/kg', 4, true),
    ('Tôm sú hàng 5', 'kg', 'Tôm sú hàng 5 có trọng lượng từ 45-54con/kg', 4, true),

    --Cua
    ('Cua gạch nhỏ', 'con', 'Cua gạch nhỏ có trọng lượng từ 250-350g/con', 5, true),
    ('Cua gạch vừa', 'con', 'Cua gạch vừa có trọng lượng từ 350-550g/con', 5, true),
    ('Cua gạch lớn', 'con', 'Cua gạch lớn có trọng lượng từ 550-800g/con', 5, true),
    ('Cua tứ nhỏ', 'con', 'Cua tứ nhỏ có trọng lượng từ 250-350g/con', 5, true),
    ('Cua tứ vừa', 'con', 'Cua tứ vừa có trọng lượng từ 350-550g/con', 5, true),
    ('Cua tứ lớn', 'con', 'Cua tứ lớn có trọng lượng từ 550-800g/con', 5, true),
    ('Cua thịt nhỏ', 'con', 'Cua thịt nhỏ có trọng lượng từ 250-350g/con', 5, true),
    ('Cua thịt vừa', 'con', 'Cua thịt vừa có trọng lượng từ 350-550g/con', 5, true),
    ('Cua thịt lớn', 'con', 'Cua thịt lớn có trọng lượng từ 550-800g/con', 5, true),

    ('Cua test 1', 'kg',
     'Test description description description description description description description description description description description description description description description description',
     5, true),
    ('Cua test 2', 'kg',
     'Test description description description description description description description description description description description description description description description description',
     5, true),
    ('Cua test 3', 'kg',
     'Test description description description description description description description description description description description description description description description description',
     5, true),
    ('Cua test 4', 'kg',
     'Test description description description description description description description description description description description description description description description description',
     5, true),
    ('Cua test 5', 'kg',
     'Test description description description description description description description description description description description description description description description description',
     5, true);

--BUYING_PRICE
INSERT INTO public.buying_prices (product_id, buying_price_value, buying_price_fluctuation, is_current)
VALUES
    --Tôm hùm
    (1, 900000, 0, true),
    (2, 1150000, 0, true),
    (3, 1300000, 0, true),

    (4, 1250000, 0, true),
    (5, 1750000, 0, true),

    (6, 1050000, 0, true),

    --Tôm càng
    (7, 270000, 5000, true),
    (8, 220000, 3000, true),
    (9, 195000, 3000, true),
    (10, 150000, 2000, true),

    --Tôm thẻ
    (11, 220000, 2000, true),
    (12, 180000, 2000, true),
    (13, 150000, 1000, true),
    (14, 120000, 1000, true),

    --Tôm sú
    (15, 230000, 3000, true),
    (16, 200000, 2000, true),
    (17, 180000, 1000, true),
    (18, 160000, 1000, true),

    --Cua
    (19, 290000, 0, true),
    (20, 390000, 0, true),
    (21, 590000, 0, true),

    (22, 150000, 0, true),
    (23, 200000, 0, true),
    (24, 250000, 0, true),

    (25, 150000, 0, true),
    (26, 200000, 0, true),
    (27, 250000, 0, true),

    (28, 150000, 0, true),
    (29, 200000, 0, true),
    (30, 250000, 0, true),
    (31, 300000, 0, true),
    (32, 350000, 0, true);

--SELLING_PRICE
INSERT INTO public.selling_prices (product_id, selling_price_value, selling_price_fluctuation, is_current)
VALUES
    --Tôm hùm
    (1, 599000, 0, true),
    (2, 1069000, 0, true),
    (3, 1490000, 0, true),

    (4, 745000, 0, true),
    (5, 1189000, 0, true),

    (6, 1299000, 0, true),

    --Tôm càng
    (7, 299000, 0, true),
    (8, 249000, 0, true),
    (9, 229000, 0, true),
    (10, 209000, 0, true),

    --Tôm thẻ
    (11, 249000, 0, true),
    (12, 199000, 0, true),
    (13, 179000, 0, true),
    (14, 159000, 0, true),

    --Tôm sú
    (15, 269000, 0, true),
    (16, 229000, 0, true),
    (17, 199000, 0, true),
    (18, 179000, 0, true),

    --Cua
    (19, 99000, 0, true),
    (20, 150000, 0, true),
    (21, 409000, 0, true),

    (22, 69000, 0, true),
    (23, 109000, 0, true),
    (24, 209000, 0, true),

    (25, 79000, 0, true),
    (26, 129000, 0, true),
    (27, 329000, 0, true),

    (28, 199000, 0, true),
    (29, 299000, 0, true),
    (30, 399000, 0, true),
    (31, 499000, 0, true),
    (32, 599000, 0, true);

--WEIGHT
INSERT INTO public.weights (product_id, weight_value, is_current)
VALUES --Tôm hùm
       (1, 52.9, true),
       (2, 75.23, true),
       (3, 103.4, true),
       (4, 100.5, true),
       (5, 150.5, true),
       (6, 350.5, true),

       --Tôm càng
       (7, 20.5, true),
       (8, 30.5, true),
       (9, 40.5, true),
       (10, 50.5, true),

       --Tôm thẻ
       (11, 30.5, true),
       (12, 40.5, true),
       (13, 50.5, true),
       (14, 60.5, true),

       --Tôm sú
       (15, 20.5, true),
       (16, 30.5, true),
       (17, 40.5, true),
       (18, 50.5, true),

       --Cua
       (19, 20.5, true),
       (20, 30.5, true),
       (21, 40.5, true),
       (22, 20.5, true),
       (23, 30.5, true),
       (24, 40.5, true),
       (25, 20.5, true),
       (26, 30.5, true),
       (27, 40.5, true),

       (28, 205.3, true),
       (29, 309.5, true),
       (30, 400.5, true),
       (31, 50.5, true),
       (32, 6.5, true);

--CART
INSERT INTO public.carts (customer_id)
VALUES ((SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')),
       ((SELECT customer_id FROM public.customers WHERE email = 'customer2@gmail.com')),
       ((SELECT customer_id FROM public.customers WHERE email = 'customer3@gmail.com'));

--CART_DETAIL
INSERT INTO public.cart_details (cart_id, product_id, quantity)
VALUES ((SELECT cart_id
         FROM public.carts
         WHERE customer_id = (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')), 1, 5),
       ((SELECT cart_id
         FROM public.carts
         WHERE customer_id = (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')), 2, 4),
       ((SELECT cart_id
         FROM public.carts
         WHERE customer_id = (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')), 3, 3),
       ((SELECT cart_id
         FROM public.carts
         WHERE customer_id = (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')), 4, 2),
       ((SELECT cart_id
         FROM public.carts
         WHERE customer_id = (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com')), 5, 1);

--PAYMENT_METHOD
INSERT INTO public.payment_methods (payment_method_name)
VALUES ('VN_PAY'),
       ('COD');

--VOUCHER
INSERT INTO public.vouchers (voucher_code, status, discount_type, discount_value, min_order_value,
                             max_discount, start_date, end_date, usage_limit, used_count)
VALUES ('KSEA-GFJ57', 'ACTIVE', 'PERCENTAGE', 10.00, 100000.00, 50000.00, '2025-03-01', '2025-04-30', 100, 18),
       ('KSEA-76DVL', 'INACTIVE', 'PERCENTAGE', 10.00, 100000.00, 50000.00, '2025-04-01', '2025-04-30', 10, 0),
       ('KSEA-89XYZ', 'ACTIVE', 'AMOUNT', 50000.00, 200000.00, NULL, '2025-03-01', '2025-09-30', 50, 5),
       ('KSEA-12LMN', 'EXPIRED', 'PERCENTAGE', 15.00, 150000.00, 75000.00, '2024-12-01', '2024-12-31', 80, 68),
       ('KSEA-34PQR', 'OUT_OF_USES', 'PERCENTAGE', 20.00, 80000.00, 40000.00, '2025-02-01', '2025-02-10', 20, 20),
       ('KSEA-56TUV', 'DISABLED', 'AMOUNT', 5000.00, 50000.00, NULL, '2025-05-01', '2025-11-30', 30, 0),
       ('KSEA-TEST1', 'INACTIVE', 'AMOUNT', 100000.00, 500000.00, NULL, '2025-04-01', '2025-04-02', 1, 0),
       ('KSEA-TEST2', 'ACTIVE', 'AMOUNT', 100000.00, 500000.00, NULL, '2025-03-15', '2025-04-30', 100, 88),
       ('KSEA-TEST3', 'INACTIVE', 'AMOUNT', 100000.00, 5000000.00, NULL, '2025-04-01', '2025-04-30', 100, 0);
