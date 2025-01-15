--ROLE
INSERT INTO public.roles (name, description)
VALUES ('MANAGER', 'Quản lý'),
       ('STAFF', 'Nhân viên'),
       ('CUSTOMER', 'Khách hàng');

--PERMISSION
INSERT INTO public.permissions (name, api_path, method, module)
VALUES  ('Create a customer', '/api/v1/customers', 'POST', 'CUSTOMER'),
        ('Create a role', '/api/v1/roles', 'POST', 'ROLE'),
        ('Create a permission', '/api/v1/permissions', 'POST', 'PERMISSION'),
        ('Create a user', '/api/v1/users', 'POST', 'USER'),
        ('Update a user', '/api/v1/users', 'PUT', 'USER'),
        ('Delete a user', '/api/v1/users', 'DELETE', 'USER'),
        ('Create a product', '/api/v1/products', 'POST', 'PRODUCT'),
        ('Update a product', '/api/v1/products', 'PUT', 'PRODUCT'),
        ('Delete a product', '/api/v1/products', 'DELETE', 'PRODUCT'),
        ('Create a category', '/api/v1/categories', 'POST', 'CATEGORY'),
        ('Update a category', '/api/v1/categories', 'PUT', 'CATEGORY'),
        ('Delete a category', '/api/v1/categories', 'DELETE', 'CATEGORY'),
        ('Create a order', '/api/v1/orders', 'POST', 'ORDER'),
        ('Update a order', '/api/v1/orders', 'PUT', 'ORDER'),
        ('Delete a order', '/api/v1/orders', 'DELETE', 'ORDER'),
        ('Create a order detail', '/api/v1/order-details', 'POST', 'ORDER_DETAIL'),
        ('Update a order detail', '/api/v1/order-details', 'PUT', 'ORDER_DETAIL'),
        ('Delete a order detail', '/api/v1/order-details', 'DELETE', 'ORDER_DETAIL'),
        ('Create a staff', '/api/v1/staffs', 'POST', 'STAFF'),
        ('Update a staff', '/api/v1/staffs', 'PUT', 'STAFF'),
        ('Delete a staff', '/api/v1/staffs', 'DELETE', 'STAFF');

--ROLE_PERMISSION FOR STAFF
INSERT INTO public.permission_role (role_id, permission_id)
VALUES (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5);

--ROLE_PERMISSION FOR CUSTOMER
INSERT INTO public.permission_role (role_id, permission_id)
VALUES (3, 6);

--CUSTOMER
INSERT INTO public.customers (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'customer1@gmail.com', 'MALE', 'I am', 'CUSTOMER1',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-01'),
       (gen_random_uuid(), 'customer2@gmail.com', 'FEMALE', 'I am', 'CUSTOMER2',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-02'),
       (gen_random_uuid(), 'customer3@gmail.com', 'FEMALE', 'I am', 'CUSTOMER3',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-03'),
       (gen_random_uuid(), 'customer4@gmail.com', 'MALE', 'I am', 'CUSTOMER4',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-04');

--MANAGER
INSERT INTO public.staffs (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES  (gen_random_uuid(), 'manager@gmail.com', 'MALE', 'I am', 'MANAGER',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 1, true, '2003-08-11');

--EMPLOYEE
INSERT INTO public.staffs (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'staff1@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff3@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff5@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff8@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff2@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff4@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff7@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff6@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff9@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03'),
       (gen_random_uuid(), 'staff10@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03');