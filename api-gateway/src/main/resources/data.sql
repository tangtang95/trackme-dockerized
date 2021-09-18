INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','THIRD_PARTY', '/grouprequestservice/grouprequests/id');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','THIRD_PARTY', '/grouprequestservice/grouprequests/thirdparties');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('POST', 'THIRD_PARTY', '/grouprequestservice/grouprequests/thirdparties');

INSERT INTO api(http_method, privilege, starting_uri) VALUES('POST', 'USER', '/sharedataservice/datacollection');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','USER','/sharedataservice/dataretrieval/users');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','THIRD_PARTY','/sharedataservice/dataretrieval/grouprequests');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','THIRD_PARTY','/sharedataservice/dataretrieval/individualrequests');

INSERT INTO api(http_method, privilege, starting_uri) VALUES('POST','USER','/individualrequestservice/responses');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET','THIRD_PARTY','/individualrequestservice/requests/thirdparties');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET', 'USER', '/individualrequestservice/requests/users');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('GET', 'ALL', '/individualrequestservice/requests/id');
INSERT INTO api(http_method, privilege, starting_uri) VALUES('POST', 'THIRD_PARTY', '/individualrequestservice/requests');