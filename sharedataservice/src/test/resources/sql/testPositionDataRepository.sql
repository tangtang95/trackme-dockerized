INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user1', 'milan', '1990-01-01', 'italy', 'mattia', 'zhao');
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user2', 'brescia', '1990-03-11', 'italy', 'riccardo', 'zhao');

INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (1, '2010-01-01 06:00:00','user1', 50.0, 60.0);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (2, '2010-01-01 08:00:00','user1', 70.0, 68.2);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (3, '2010-01-01 10:00:00','user1', -10.0, 65.42);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (4, '2010-01-01 11:00:00','user2', 30.3, 70.2);