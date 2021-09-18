INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user1', 'milan', '1990-01-01', 'italy', 'mattia', 'zhao');
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user2', 'brescia', '1990-03-11', 'italy', 'riccardo', 'zhao');

INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(1, '2000-01-01', '2010-01-01', 1, '2000-01-01 00:00:00','user1');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(4, '2000-01-01', '2000-01-01', 2, '2000-01-01 00:00:00','user1');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(5, '2000-12-01', '2000-01-01', 2, '2000-01-01 00:00:00','user2');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(6, '2000-01-01', '2010-01-01', 4, '2000-01-01 00:00:00','user2');