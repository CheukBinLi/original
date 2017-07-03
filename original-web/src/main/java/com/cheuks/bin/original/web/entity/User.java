package com.cheuks.bin.original.web.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cheuks.bin.original.db.DefaultBaseEntity;

@Entity(name = "original_web_user")
public class User extends DefaultBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	private String userNmae;
	private String password;
	private String role;

	public int getId() {
		return id;
	}

	public User setId(int id) {
		this.id = id;
		return this;
	}

	public String getUserNmae() {
		return userNmae;
	}

	public User setUserNmae(String userNmae) {
		this.userNmae = userNmae;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getRole() {
		return role;
	}

	public User setRole(String role) {
		this.role = role;
		return this;
	}

}
