package com.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.dao.api.PojoDaoFactory;

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
     * The embedded database H2 no support actual stored procedures, so this
     * method imitates it for {@code CallableStatement} work.
     * 
     * @param name
     * @param surname
     * @return a generated primary key (id)
     */
    public static int saveUser(String name, String surname) {
	int id = 0;
	PreparedStatement pstmt = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
	    cs.setAutoCommit(false);
	    pstmt = cs.prepareStatement(ps.getProperty("insert"));
	    pstmt.setString(1, name);
	    pstmt.setString(2, surname);
	    pstmt.executeUpdate();
	    stmt = cs.createStatement();
	    rs = stmt.executeQuery(ps.getProperty("lastId"));
	    cs.commit();
	    if (rs.next())
		id = rs.getInt(ps.getProperty("id"));
	} catch (SQLException e) {
	    logger.error(e);
	    try {
		cs.rollback();
	    } catch (SQLException e1) {
		logger.error(e1);
	    }
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (SQLException e2) {
		logger.error(e2);
	    }
	    try {
		if (stmt != null) stmt.close();
	    } catch (SQLException e3) {
		logger.error(e3);
	    }
	    try {
		if (rs != null) rs.close();
	    } catch (SQLException e4) {
		logger.error(e4);
	    }
	    try {
		cs.setAutoCommit(true);
	    } catch (SQLException e5) {
		logger.error(e5);
	    }
	}
	return id;
    }
}
