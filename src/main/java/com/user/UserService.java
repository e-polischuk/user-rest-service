package com.user;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.dao.H2UserDao;
import com.user.dao.api.PojoDao;
import com.user.dao.api.PojoDaoFactory;
import com.user.pojo.User;

/**
 * Handles User CRUD requests of client's side.
 * 
 * @author e-polischuk
 *
 */
@Path("")
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    /**
     * DAO for User POJO. The PojoDaoFactory provides PojoDao api
     * with a single connection to the database.
     */
    private static PojoDao<User, Integer> dao = PojoDaoFactory.open(H2UserDao.class);

    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User save(User user) {
	user = dao.save(user);
	if (user != null && user.getId() > 0)
	    logger.info("SAVED: " + user.toString());
	return user;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findOne(@PathParam("id") int id) {
	return dao.findOne(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findAll() {
	return dao.findAll();
    }

    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(User user) {
	User before = dao.findOne(user.getId());
	dao.update(user);
	if (before != null)
	    logger.info(before.toString() + " WAS UPDATED TO " + user.toString());
    }

    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(User user) {
	if (user != null && user.getId() > 0) {
	    dao.delete(user.getId());
	    if (dao.findOne(user.getId()) == null)
		logger.info(user.toString() + " WAS SUCCESSFULLY DELETED!");
	}
    }

    /**
     * Existing dao will be removed from factory cache, its connection will be
     * closed and it's free for GC. Instead that new dao with new database will
     * be set.
     * 
     * @param pojoDaoClass is class type of new dao
     */
    public static void changeDaoTo(Class<?> pojoDaoClass) {
	logger.info(dao.getClass() + " was replaced with the new - " + pojoDaoClass);
	PojoDaoFactory.remove(dao.getClass());
	dao = PojoDaoFactory.open(pojoDaoClass);
    }

}
