INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (1, 'COUNT', '2011-06-06', 'ALL', 'ACCEPTED', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (3, 'AVG', '2010-09-07', 'PRESSURE_MIN', 'REFUSED', 1);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (2, 'MAX', '2005-11-03', 'HEART_BEAT', 'UNDER_ANALYSIS', 2);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (4, 'DISTINCT_COUNT', '2005-11-03', 'ALL', 'REFUSED', 5);
INSERT INTO group_request(id, aggregator_operator, creation_timestamp, request_type, status, third_party_id) VALUES (5, 'MAX', '2015-12-01', 'BIRTH_YEAR', 'ACCEPTED', 4);


INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(1, 'HEART_BEAT', 'GREATER', 200, 1);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(2, 'BLOOD_OXYGEN_LEVEL', 'GREATER', 90, 1);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(3, 'HEART_BEAT', 'LESS', 80, 2);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(4, 'BLOOD_OXYGEN_LEVEL', 'LESS', 80, 3);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(5, 'BLOOD_OXYGEN_LEVEL', 'GREATER', 51, 3);
INSERT INTO filter_statement(id, filter_column, comparison_symbol, value, group_request_id) VALUES(6, 'PRESSURE_MIN', 'LESS', 80, 3);

