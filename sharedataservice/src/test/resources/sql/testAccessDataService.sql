INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user1', 'milan', '1990-01-01', 'italy', 'mattia', 'zhao');
INSERT INTO user(ssn, birth_city, birth_date, birth_nation, first_name, last_name) VALUES
  ('user2', 'brescia', '1990-03-11', 'italy', 'riccardo', 'zhao');

INSERT INTO health_data(id, timestamp, user_ssn, heart_beat, pressure_min, pressure_max, blood_oxygen_level) VALUES
  (1, '2010-01-01 06:00:00','user1', 50, 60, 100, 75);
INSERT INTO health_data(id, timestamp, user_ssn, heart_beat, pressure_min, pressure_max, blood_oxygen_level) VALUES
  (2, '2010-01-01 08:00:00','user1', 70, 68, 101, 90);
INSERT INTO health_data(id, timestamp, user_ssn, heart_beat, pressure_min, pressure_max, blood_oxygen_level) VALUES
  (3, '2010-01-01 10:00:00','user1', 120, 65, 110, 79);
INSERT INTO health_data(id, timestamp, user_ssn, heart_beat, pressure_min, pressure_max, blood_oxygen_level) VALUES
  (4, '2010-01-01 11:00:00','user2', 100, 70, 120, 78);

INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (1, '2010-01-01 06:00:00','user1', 50.0, 60.0);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (2, '2010-01-01 08:00:00','user1', 70.0, 68.2);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (3, '2010-01-01 10:00:00','user1', -10.0, 65.42);
INSERT INTO position_data(id, timestamp, user_ssn, latitude, longitude) VALUES
  (4, '2010-01-01 11:00:00','user2', 30.3, 70.2);

INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(1, '2010-01-02', '2010-01-01', 1, '2010-01-02 00:00:00','user1');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(4, '2000-01-01', '2000-01-01', 2, '2000-01-01 00:00:00','user1');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(5, '2000-12-01', '2000-01-01', 2, '2000-01-01 00:00:00','user2');
INSERT INTO individual_request(id, end_date, start_date, third_party_id, creation_timestamp, user_ssn) VALUES(6, '2000-01-01', '2010-01-01', 4, '2000-01-01 00:00:00','user2');

INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, third_party_id) VALUES (1, 'COUNT', '2000-01-01 00:00:00', 'ALL', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, third_party_id) VALUES (3, 'AVG', '2000-05-05 00:00:00', 'PRESSURE_MIN', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, third_party_id) VALUES (2, 'MAX', '2000-10-11 00:00:00', 'HEART_BEAT', 2);