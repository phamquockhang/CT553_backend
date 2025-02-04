--ROLE
INSERT INTO public.roles (name, description)
VALUES ('MANAGER', 'Quản lý'),
       ('STAFF', 'Nhân viên'),
       ('CUSTOMER', 'Khách hàng');

--PERMISSION
INSERT INTO public.permissions (name, api_path, method, module)
VALUES  ('Get all roles', '/api/v1/roles', 'GET', 'ROLE'),
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
        ('Get a item', '/api/v1/customers/{id}', 'GET', 'CUSTOMER'),
        ('Create a item', '/api/v1/customers', 'POST', 'CUSTOMER'),
        ('Update a item', '/api/v1/customers/{id}', 'PUT', 'CUSTOMER'),
        ('Delete a item', '/api/v1/customers/{id}', 'DELETE', 'CUSTOMER');

--         ('Create a product', '/api/v1/products', 'POST', 'PRODUCT'),
--         ('Update a product', '/api/v1/products', 'PUT', 'PRODUCT'),
--         ('Delete a product', '/api/v1/products', 'DELETE', 'PRODUCT'),
--         ('Create a category', '/api/v1/categories', 'POST', 'CATEGORY'),
--         ('Update a category', '/api/v1/categories', 'PUT', 'CATEGORY'),
--         ('Delete a category', '/api/v1/categories', 'DELETE', 'CATEGORY'),
--         ('Create a order', '/api/v1/orders', 'POST', 'ORDER'),
--         ('Update a order', '/api/v1/orders', 'PUT', 'ORDER'),
--         ('Delete a order', '/api/v1/orders', 'DELETE', 'ORDER'),
--         ('Create a order detail', '/api/v1/order-details', 'POST', 'ORDER_DETAIL'),
--         ('Update a order detail', '/api/v1/order-details', 'PUT', 'ORDER_DETAIL'),
--         ('Delete a order detail', '/api/v1/order-details', 'DELETE', 'ORDER_DETAIL'),

--ROLE_PERMISSION FOR STAFF
INSERT INTO public.permission_role (role_id, permission_id)
VALUES (2, 18),
       (2, 19);

--ROLE_PERMISSION FOR CUSTOMER
-- INSERT INTO public.permission_role (role_id, permission_id)
-- VALUES (3, 6);

--CUSTOMER
INSERT INTO public.customers (customer_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'customer1@gmail.com', 'MALE', 'CUSTOMER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-01'),
       (gen_random_uuid(), 'customer2@gmail.com', 'FEMALE', 'CUSTOMER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-02'),
       (gen_random_uuid(), 'customer3@gmail.com', 'FEMALE', 'CUSTOMER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-03'),
       (gen_random_uuid(), 'customer4@gmail.com', 'MALE', 'CUSTOMER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-04');

--MANAGER
INSERT INTO public.staffs (staff_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'manager@gmail.com', 'MALE', 'MANAGER', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 1, true, '2003-08-11');

--STAFF
INSERT INTO public.staffs (staff_id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'staff1@gmail.com', 'FEMALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-01'),
       (gen_random_uuid(), 'staff2@gmail.com', 'FEMALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-02'),
       (gen_random_uuid(), 'staff3@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff4@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-04'),
       (gen_random_uuid(), 'staff5@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-05'),
       (gen_random_uuid(), 'staff6@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-06'),
       (gen_random_uuid(), 'staff7@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-07'),
       (gen_random_uuid(), 'staff8@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-08'),
       (gen_random_uuid(), 'staff9@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-09'),
       (gen_random_uuid(), 'staff10@gmail.com', 'MALE', 'STAFF', 'I am',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-10');

--SCORE
INSERT INTO public.scores (score_id, customer_id, change_amount, new_value, is_current)
VALUES (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), 100, 100, true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer2@gmail.com'), 200, 200, true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer3@gmail.com'), 30, 30, true);
--        (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), -10, 90, true);

--ADDRESS
INSERT INTO public.addresses (address_id, customer_id, province_id, district_id, ward_code, description, is_default)
VALUES (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer1@gmail.com'), 252, 1782,
        '610201', 'Khóm 5', true),
       (gen_random_uuid(), (SELECT customer_id FROM public.customers WHERE email = 'customer2@gmail.com'), 252, 1782,
        '610201', 'Khóm 6', true);

--ITEM
INSERT INTO public.items (name)
VALUES ('Item 1'),
       ('Item 2'),
       ('Item 3'),
       ('Item 4'),
       ('Item 5'),
       ('Item 6'),
       ('Item 7'),
       ('Item 8'),
       ('Item 9'),
       ('Item 10'),
       ('Item 11'),
       ('Item 12'),
       ('Item 13'),
       ('Item 14'),
       ('Item 15');