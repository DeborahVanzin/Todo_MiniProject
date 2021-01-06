package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents security token that is used for authentication.
 * The token has its lifetime, after which it isn't valid anymore.
 */
public class Token {	
	private String token; // String representation of the token
	private LocalDateTime creationDateTime; // Timestamp of when it was created
	private int expiryTimeMinutes; // Lifetime after which the token expires
	
	public Token(int expiryTimeMinutes)
	{
		token = UUID.randomUUID().toString();
		token = token.toUpperCase();
		token = token.replace("-", "");
		creationDateTime = LocalDateTime.now();
		this.expiryTimeMinutes = expiryTimeMinutes;
	}
	
	/**
	 * Checks, if the token is still valid, that is if it hasn't expired yet.
	 * @return True, if the token is valid or false otherwise (if it has expired).
	 */
	public boolean isValid() {
		long elapsedMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), creationDateTime);
		return elapsedMinutes < expiryTimeMinutes;
	}

	@Override
	public String toString() {
		return token;
	}
}
