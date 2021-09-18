-- Password that matches "password1" with BCryptEncoder
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name, password, username) VALUES ('user1', 'Verona', '1999-01-01', 'ITALY', 'Frank', 'Rossi', '$2a$10$zuFzQYdPuK1A05BUz.CsUuNfA0RlVzG1DYHtXcVn2NirAzS2uoqTG', 'username1');

-- Password that matches "password2" with BCryptEncoder
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name, password, username) VALUES ('user2', 'Milano', '1997-02-05', 'FRANCE', 'Giulio', 'Pate', '$2a$10$o2nP/rbQducIX8iV6.Iefej2tygSMpYLkyraF/U0.UYo.yAAptxE6', 'username2');

INSERT INTO third_party_customer(id, email, password) VALUES (1, 'tp1@provider.com', '$2a$10$73Wfl/7OQKJNBHTly2luHOG.wroS6vyrZlScAbbBJvIXMuH4SrWl.');
INSERT INTO third_party_customer(id, email, password) VALUES (2, 'tp2@provider.com', '$2a$10$eKfO2.XzzoMWBOPvd/CBIuyQNS4qzcn05GNSJad.5dxBoIhP0nbUe');
INSERT INTO third_party_customer(id, email, password) VALUES (3, 'tp3@provider.com', '$2a$10$TrfqML/e65PcLYME1QMRPehThBrvzhgCic5EWlfUW.7jy07rle7Du');
INSERT INTO third_party_customer(id, email, password) VALUES (4, 'tp4@provider.com', '$2a$10$t5/IbKyjrRKIZmxygiHhfOU8xf.A1c4hQZSsOj9R6NVYp7e8RXrbe');

INSERT INTO company_detail(third_party_customer, address, company_name, duns_number) VALUES (1, 'address1', 'company1', '555');
INSERT INTO company_detail(third_party_customer, address, company_name, duns_number) VALUES (2, 'address2', 'company2', '444');

INSERT INTO private_third_party_detail(third_party_customer, birth_date, name, ssn, surname, birth_city) VALUES (3, '2000-01-01', 'Jack', 'tp3ssn', 'Jones', 'Roma');
INSERT INTO private_third_party_detail(third_party_customer, birth_date, name, ssn, surname, birth_city) VALUES (4, '1784-06-10', 'Julio', 'tp4ssn', 'Ramirez', 'London');