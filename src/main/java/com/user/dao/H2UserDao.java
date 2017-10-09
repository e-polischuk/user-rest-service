package com.user.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configures {@link AbstractUserDao} for embedded database H2.
 * 
 * @author e-polischuk
 *
 */
public class H2UserDao extends AbstractUserDao {
    private static final Logger logger = LogManager.getLogger(H2UserDao.class);
    private static Properties ps;
    private static Connection cs;
    
    /**
     * Constructor without parameters for {@link PojoDaoFactory}
     */
    public H2UserDao() {
	super("h2.properties");
	ps = properties;
	cs = connection;
    }
    
    /**
     * The embedded database H2 no support actual stored procedures, 
     * so this method imitates it for {@link CallableStatement} work.
     * 
     * @param name
     * @param surname
     * @return a generated primary key (id)
     */
    public static int saveUser(String name, String surname) {
	try(PreparedStatement pstmt = cs.prepareStatement(ps.getProperty("insert"))) {
	    pstmt.setString(1, name);
	    pstmt.setString(2, surname);
	    pstmt.executeUpdate();
	} catch (SQLException e) {
	    logger.error(e);
	}
	try(Statement stmt = cs.createStatement()) {
	    ResultSet rs = stmt.executeQuery(ps.getProperty("lastId"));
	    if (rs.next())
		return rs.getInt(ps.getProperty("id"));
	} catch (SQLException e) {
	    logger.error(e);
	}
	return 0;
    }
}
