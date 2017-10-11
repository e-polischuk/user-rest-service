DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users 
(
	id int(10) NOT NULL AUTO_INCREMENT,
	usr_name VARCHAR(30) NOT NULL,
	usr_surname VARCHAR(30) NOT NULL,
	PRIMARY KEY (id)
);
DROP ALIAS IF EXISTS saveUser;
CREATE ALIAS saveUser FOR "com.user.dao.H2UserDao.saveUser";
INSERT INTO users (usr_name, usr_surname) VALUES('Ivan', 'Petrov');
INSERT INTO users (usr_name, usr_surname) VALUES('Van', 'Lee');
INSERT INTO users (usr_name, usr_surname) VALUES('Tom', 'Smith');