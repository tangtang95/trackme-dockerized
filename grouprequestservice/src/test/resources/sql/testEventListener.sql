INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (1, 'COUNT', '2000-01-01', 'ALL', 'ACCEPTED', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (3, 'AVG', '2000-05-05', 'PRESSURE_MIN', 'REFUSED', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (2, 'MAX', '2000-10-11', 'HEART_BEAT', 'UNDER_ANALYSIS', 2);

INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(1, 'HEART_BEAT', 'GREATER', 200, 1);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(2, 'BLOOD_OXYGEN_LEVEL', 'GREATER', 90, 1);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(3, 'HEART_BEAT', 'LESS', 80, 2);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(4, 'BLOOD_OXYGEN_LEVEL', 'LESS', 80, 3);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(5, 'BLOOD_OXYGEN_LEVEL', 'GREATER', 51, 3);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(6, 'PRESSURE_MIN', 'LESS', 80, 3);