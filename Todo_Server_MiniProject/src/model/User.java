package model;

import java.io.Serializable;

import utility.PasswordHasher;

/**
 * Represents a user of the system. User has an email and password.
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String passwordHash;
	
	public User(String email, String password) {
		this.email = email;
		this.passwordHash = PasswordHasher.calculatePasswordHash(password);
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
}
