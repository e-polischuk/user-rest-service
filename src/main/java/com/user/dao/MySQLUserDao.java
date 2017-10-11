package com.user.dao;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.dao.api.PojoDaoFactory;

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
	    stmnt.execute("CREATE TABLE IF NOT EXISTS users (" + 
		    $("id") + " int(10) NOT NULL AUTO_INCREMENT, " +
		    $("name") + " VARCHAR(30) NOT NULL, " +
		    $("surname") + " VARCHAR(30) NOT NULL, " +
		    "PRIMARY KEY (id))");
	    stmnt.execute("DROP PROCEDURE IF EXISTS saveUser");
	    stmnt.execute("CREATE PROCEDURE saveUser " +
		    "(OUT uid int(10), IN un VARCHAR(30), IN usn VARCHAR(30)) " +
		    "BEGIN " +
		    	"INSERT INTO users (" + $("name") + ", " + $("surname") +
		    		") VALUES (un, usn); " +
		    	"SELECT MAX(" + $("id") + ") INTO uid FROM users; " +
		    "END");
	} catch (SQLException e) {
	    logger.error(e);
	}
    }

}
