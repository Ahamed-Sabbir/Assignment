INSERT INTO authority_table (authority_id, authority)
VALUES(UUID '46fa4080-4159-465e-93bd-adeee251361d', 'ROLE_SUPREME_ADMIN');

INSERT INTO authority_table (authority_id, authority)
VALUES(UUID 'e0ff6fb9-b3bd-4dee-b810-264e9e1716ff', 'ROLE_ADMIN');

INSERT INTO authority_table (authority_id, authority)
VALUES (UUID '71e4b0ac-129b-4e3f-9d0c-0405aea69aa7', 'ROLE_USER');

INSERT INTO user_table (user_id, first_name, last_name, username, password, is_active, created_by, authority_id)
VALUES (UUID 'aa53c071-1b81-4347-ac93-88fd5a6b5df4', 'sabbir', 'ahamed', 'sabbir', '$2a$10$YyeI6jq4plI0algXyt5JcOhISjpYBUOCSe4Jcu/tUmJteg6IBxZCC', false, NULL, UUID '46fa4080-4159-465e-93bd-adeee251361d');



