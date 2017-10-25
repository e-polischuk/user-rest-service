package com.user.dao.api;

import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.spi.resource.Singleton;

/**
 * Alternatively to injection by web container, this own factory
 * is available for instances of {@link PojoDao} implementations.
 * It is configured in web.xml as provider. Factory scope must be
 * a singleton.
 * 
 * @author e-polischuk
 *
 */
@Singleton
public class PojoDaoFactory {
    private static final Logger logger = LogManager.getLogger(PojoDaoFactory.class);
    /**
     * The repository for instances of {@link PojoDao} implementations.
     */
    private static Map<Class<?>, Object> daoCache = new IdentityHashMap<>();

    /**
     * Unless an instance of type {@code pojoDaoClass} exists in
     * {@code daoCache} then creates and returns it else returns from
     * {@code daoCache}.
     * 
     * @param pojoDaoClass type of {@link PojoDao} implementation
     * @return an instance of the pojoDaoClass type
     */
    @SuppressWarnings("unchecked")
    public synchronized static <T, ID> PojoDao<T, ID> get(Class<?> pojoDaoClass) {
	PojoDao<T, ID> pojoDao = (PojoDao<T, ID>) daoCache.get(pojoDaoClass);
	if (pojoDao != null)
	    return pojoDao;
	try {
	    pojoDao = (PojoDao<T, ID>) Class.forName(pojoDaoClass.getName()).newInstance();
	    daoCache.put(pojoDaoClass, pojoDao);
	    logger.info("CREATED & CACHED DAO: " + pojoDaoClass.getName());
	} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
	    logger.error(e);
	}
	return pojoDao;
    }

    /**
     * If an instance of type {@code pojoDaoClass} exists in {@code daoCache}
     * then it is removed from {@code daoCache}, its connections to the
     * database is closed and it is free for the garbage collector.
     * 
     * @param pojoDaoClass type of {@code PojoDao} implementation
     * @return {@code true} if {@code daoCache} contained {@code pojoDaoClass}
     *         else - {@code false}
     */
    @SuppressWarnings("unchecked")
    public synchronized static <T, ID> boolean close(Class<?> pojoDaoClass) {
	PojoDao<T, ID> pojoDao = (PojoDao<T, ID>) daoCache.remove(pojoDaoClass);
	if (pojoDao != null) {
	    pojoDao.close();
	    logger.info("CLOSED DAO: " + pojoDaoClass.getName());
	    return true;
	}
	return false;
    }
    
    /**
     * Closes all connections of all DAOs those there are in {@code daoCache}
     * and clears {@code daoCache}.
     */
    @SuppressWarnings("rawtypes")
    public synchronized static void clearCache() {
	daoCache.values().forEach(obj -> ((PojoDao) obj).close());
	daoCache.clear();
    }
    
}
