package com.user;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.user.dao.H2UserDao;

/**
 * Integration tests for this web application is run in the test container.
 * 
 * @author e-polischuk
 *
 */
public class UserServiceTest extends JerseyTest {
    private static volatile boolean noEmpty;
    private static WebResource service;
    
    public UserServiceTest() throws Exception {
	super("com.user", "com.user.dao", "com.user.pojo");
	service = resource();
	if (!noEmpty) {
	    noEmpty = true;
	    JSONObject in = new JSONObject().put("id", 0).put("name", "Ivan").put("surname", "Petrov");
	    service.path("new").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, in.toString());
	    in = new JSONObject().put("id", 0).put("name", "Vong").put("surname", "Lee");
	    service.path("new").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, in.toString());
	    in = new JSONObject().put("id", 0).put("name", "Syltan").put("surname", "Shakh");
	    service.path("new").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, in.toString());
	}
    }
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @AfterClass
    public static void end() {
	UserService.changeDaoTo(H2UserDao.class);
    }

    @Test
    public void save() throws JSONException {
	JSONObject in = new JSONObject().put("id", 0).put("name", "Tom").put("surname", "Soyer");
	ClientResponse resp = service.path("new").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, in.toString());
	assertTrue(resp.getStatus() == 200);
	JSONObject out = resp.getEntity(JSONObject.class);
	assertTrue(out.getInt("id") >= 3);
    }
    
    @Test
    public void findOne() throws JSONException {
	ClientResponse resp = service.path("1").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	assertTrue(resp.getStatus() == 200);
	JSONObject out = resp.getEntity(JSONObject.class);
	assertTrue(out.getInt("id") == 1);
    }

    @Test
    public void findAll() throws JSONException {
	ClientResponse resp = service.path("all").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	assertTrue(resp.getStatus() == 200);
	JSONObject out = resp.getEntity(JSONObject.class);
	assertTrue(out.getJSONArray("user").length() >= 2);
    }

    @Test
    public void update() throws JSONException {
	JSONObject in = new JSONObject().put("id", 2).put("name", "Deepak").put("surname", "Chopra");
	ClientResponse resp = service.path("update").type(MediaType.APPLICATION_JSON).put(ClientResponse.class, in.toString());
	assertTrue(resp.getStatus() == 204);
	resp = service.path("2").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	assertTrue(resp.getStatus() == 200);
	JSONObject out = resp.getEntity(JSONObject.class);
	assertTrue(out.getString("name").equals("Deepak"));
	assertTrue(out.getString("surname").equals("Chopra"));
    }
    
    @Test
    public void delete() throws JSONException, UniformInterfaceException {
	JSONObject in = new JSONObject().put("id", 3).put("name", "Syltan").put("surname", "Shakh");
	ClientResponse resp = service.path("delete").type(MediaType.APPLICATION_JSON).delete(ClientResponse.class, in.toString());
	assertTrue(resp.getStatus() == 204);
	resp = service.path("3").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	assertTrue(resp.getStatus() == 204);
	thrown.expect(UniformInterfaceException.class);
	resp.getEntity(JSONObject.class);
    }

}
