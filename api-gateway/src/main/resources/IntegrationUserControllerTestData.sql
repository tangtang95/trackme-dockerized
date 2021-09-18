-- Password that matches "password1" with BCryptEncoder
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name, password, username) VALUES ('user1', 'Verona', '1999-01-01', 'ITALY', 'Frank', 'Rossi', '$2a$10$zuFzQYdPuK1A05BUz.CsUuNfA0RlVzG1DYHtXcVn2NirAzS2uoqTG', 'username1');

-- Password that matches "password2" with BCryptEncoder
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name, password, username) VALUES ('user2', 'Milano', '1997-02-05', 'FRANCE', 'Giulio', 'Pate', '$2a$10$o2nP/rbQducIX8iV6.Iefej2tygSMpYLkyraF/U0.UYo.yAAptxE6', 'username2');
