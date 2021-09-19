CREATE USER 'trackmeadmin'@'%' IDENTIFIED BY 'datecallmeeting95';
CREATE DATABASE share_data_db;
CREATE DATABASE group_request_db;
CREATE DATABASE individual_request_db;
CREATE DATABASE account_service_db;
GRANT ALL PRIVILEGES ON *.* TO 'trackmeadmin'@'%';