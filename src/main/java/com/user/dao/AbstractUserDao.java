package com.user.dao;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.dao.api.PojoDao;
import com.user.pojo.User;

import jersey.repackaged.org.objectweb.asm.Type;

/**
 * Implementation of {@link PojoDao} for a User pojo.
 * 
 * @author e-polischuk
 *
 */
public abstract class AbstractUserDao implements PojoDao<User, Integer> {
    private static final Logger logger = LogManager.getLogger(AbstractUserDao.class);
    /**
     * These properties are used for database configuration,
     * pojo mapping and sql-requests declaration.
     */
    protected Properties properties = new Properties();
    /**
     * Just one connection to the configured data base.
     */
    protected Connection connection;

    
    protected AbstractUserDao(String propertiesFileName) {
	try {
	    properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
	    Class.forName($("driver"));
	    connection = DriverManager.getConnection($("url"), $("user"), $("password"));
	} catch (SQLException | ClassNotFoundException | IOException e) {
	    logger.error(e);
	} 
    }

    /*
     * Syntax sugar
     */
    protected String $(String key) {
	return properties.getProperty(key);
    }

    @Override
    public synchronized User findOne(Integer id) {
	User user = null;
	try(PreparedStatement pstmt = connection.prepareStatement($("findOne"))) {
	    pstmt.setInt(1, id);
	    try(ResultSet rs = pstmt.executeQuery()) {
		if (rs.next())
		    user = new User(rs.getInt($("id")), rs.getString($("name")), rs.getString($("surname")));
	    }
	} catch (SQLException e) {
	    logger.error(e);
	}
	return user;
    }

    @Override
    public synchronized List<User> findAll() {
	List<User> users = new ArrayList<>();
	try(Statement stmt = connection.createStatement()) {
	    try(ResultSet rs = stmt.executeQuery($("findAll"))) {
		while (rs.next()) 
		    users.add(new User(rs.getInt($("id")), rs.getString($("name")), rs.getString($("surname"))));
	    }
	} catch (SQLException e) {
	    logger.error(e);
	}
	return users;
    }

    @Override
    public synchronized User save(User pojo) {
	try(CallableStatement cstmt = connection.prepareCall($("save"))) {
	    cstmt.setString(2, pojo.getName());
	    cstmt.setString(3, pojo.getSurname());
	    cstmt.registerOutParameter(1, Type.INT);
	    cstmt.executeUpdate();
	    pojo.setId(cstmt.getInt(1));
	    return pojo;
	} catch (SQLException e) {
	    logger.error(e);
	}
	return null;
    }

    @Override
    public synchronized void update(User pojo) {
	try(PreparedStatement pstmt = connection.prepareStatement($("update"))) {
	    connection.setAutoCommit(false);
	    pstmt.setString(1, pojo.getName());
	    pstmt.setString(2, pojo.getSurname());
	    pstmt.setInt(3, pojo.getId());
	    pstmt.executeUpdate();
	    connection.commit();
	} catch (SQLException e) {
	    logger.error(e);
	    try {
		connection.rollback();
	    } catch (SQLException e1) {
		logger.error(e1);
	    }
	}
	try {
	    connection.setAutoCommit(true);
	} catch (SQLException e2) {
	    logger.error(e2);
	}
    }

    @Override
    public synchronized void delete(Integer id) {
	try(PreparedStatement pstmt = connection.prepareStatement($("delete"))) {
	    pstmt.setInt(1, id);
	    pstmt.executeUpdate();
	} catch (SQLException e) {
	    logger.error(e);
	}
    }

    @Override
    public synchronized void close() {
	if (connection != null) {
	    try {
		connection.close();
		logger.info("ALL CONNECTIONS TO DB WERE CLOSED!");
	    } catch (SQLException e) {
		logger.error(e);
	    }
	}
    }

}
