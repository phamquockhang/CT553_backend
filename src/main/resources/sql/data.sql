--ROLE
INSERT INTO public.roles (name, description, is_active)
VALUES ('MANAGER', 'Quản lý', true),
       ('EMPLOYEE', 'Nhân viên', true),
       ('CUSTOMER', 'Khách hàng', true);

--PERMISSION
INSERT INTO public.permissions (name, api_path, method, module)
VALUES  ('Create a customer', '/api/v1/customers', 'POST', 'CUSTOMER'),
        ('Create a role', '/api/v1/roles', 'POST', 'ROLE'),
        ('Create a permission', '/api/v1/permissions', 'POST', 'PERMISSION'),
        ('Create a user', '/api/v1/users', 'POST', 'USER');

--ROLE_PERMISSION
-- INSERT INTO public.permission_role (permission_id, role_id)
-- VALUES (2, 2);

--USER
INSERT INTO public.customers (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'customer@gmail.com', 'MALE', 'I am', 'CUSTOMER',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 3, true, '1999-01-01');

--STAFF
INSERT INTO public.staffs (id, email, gender, first_name, last_name, password, role_id, is_activated, dob)
VALUES (gen_random_uuid(), 'staff@gmail.com', 'MALE', 'I am', 'STAFF',
        '$2a$10$MEo.Zw55GDOEVwKtOnJ/TuKNrWVAjluxnWqH96ecqAKUwkFwAVkra', 2, true, '1999-12-03');