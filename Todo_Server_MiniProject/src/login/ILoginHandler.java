package login;

import model.Token;

/**
 * Provides all functionality of the database, that is used for
 * creating, storing and retrieving the ToDo entries of users.
 */
public interface ILoginHandler {
	/**
	 * Performs login of a user with given email and password.
	 * Upon succeeding, returns a security token, that can be further
	 * used to access functions only for logged users.
	 * @email Email of the user
	 * @password Password of the user
	 * @return Token that can be used further or null, if email/password combination doesn't exist.
	 */
	public Token login(String email, String password);
	
	/**
	 * Validates a given token.
	 * @param token Token to be validated
	 * @return Email of the user to which the token belongs or null otherwise.
	 */
	public String validateToken(String token);
	
	/**
	 * Performs logout of a user.
	 * @token Security token that has been granted to a user upon login
	 * @return True, if the user has been logged out successfully or false otherwise.
	 */
	public boolean logout(String token);
}
