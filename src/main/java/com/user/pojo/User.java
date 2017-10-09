package com.user.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO.
 * 
 * @author e-polischuk
 *
 */
@XmlRootElement(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String surname;
    
    public User() {}

    public User(int id, String name, String surname) {
	this.id = id;
	this.name = name;
	this.surname = surname;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getSurname() {
	return surname;
    }

    public void setId(int id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setSurname(String surname) {
	this.surname = surname;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }

}
