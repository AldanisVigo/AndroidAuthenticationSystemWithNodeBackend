# This creates the minimal structure for the Database
CREATE DATABASE AndroidLoginService;
USE AndroidLoginService;
CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT,email VARCHAR(256),password VARCHAR(40), PRIMARY KEY(id));
