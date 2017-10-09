package com.user.dao;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configures {@link AbstractUserDao} for database MySQL.
 * See comments in 'mysql.properties' file in 'resource'
 * folder.
 * 
 * @author e-polischuk
 *
 */
public class MySQLUserDao extends AbstractUserDao {
    private static final Logger logger = LogManager.getLogger(MySQLUserDao.class);

    /**
     * Constructor without parameters for {@link PojoDaoFactory}
     */
    public MySQLUserDao() {
	super("mysql.properties");
	try(Statement stmnt = connection.createStatement()) {
	    stmnt.execute("DROP TABLE IF EXISTS users");
	    stmnt.execute("CREATE TABLE IF NOT EXISTS users " + 
		    "(id int(10) NOT NULL AUTO_INCREMENT, " +
		    "usr_name VARCHAR(30) NOT NULL, " +
		    "usr_surname VARCHAR(30) NOT NULL, " +
		    "PRIMARY KEY (id))");
	    stmnt.execute("DROP PROCEDURE IF EXISTS createUser");
	    stmnt.execute("CREATE PROCEDURE createUser " +
		    "(OUT uid int(10),IN un VARCHAR(30),IN usn VARCHAR(30)) " +
		    "BEGIN " +
		    "INSERT INTO users (usr_name,usr_surname) VALUES(un,usn); " +
		    "SELECT MAX(id) INTO uid FROM users; " +
		    "END");
	} catch (SQLException e) {
	    logger.error(e);
	}
    }

}
