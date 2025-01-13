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
        ('Create a user', '/api/v1/users', 'POST', 'USER');

--ROLE_PERMISSION
-- INSERT INTO public.permission_role (permission_id, role_id)
-- VALUES (2, 2);

--CUSTOMER
INSERT INTO public.customers (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'customer@gmail.com', 'MALE', 'I am', 'CUSTOMER',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-01');

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