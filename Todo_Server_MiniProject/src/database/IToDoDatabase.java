package database;

import java.time.LocalDate;
import java.util.List;

import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.User;

/**
 * Provides all functionality of the database, that is used for
 * creating, storing and retrieving the ToDo entries of users.
 */
public interface IToDoDatabase {
	/**
	 * Creates a new user login with given email and password.
	 * @param email Email of the user
	 * @param password Password of the user
	 * @return True, if the new user login has been created successfully or false otherwise (e.g. email is already used).
	 */
	boolean createLogin(String email, String password);
	
	/**
	 * Gets User instance corresponding to the given email.
	 * @param email Email of the user
	 * @return User instance with the given email or null, if user with such e-mail doesn't exist.
	 */
	User getUserByEmail(String email);
	
	/**
	 * Creates a new ToDo entry and stores in the database.
	 * @param token Security token of the logged user
	 * @param title Title of the ToDo entry
	 * @param priority Priority of the ToDo entry
	 * @param description Description of the ToDo entry
	 * @param dueDate Due date of the ToDo entry
	 * @return Assigned ID of the created ToDo entry or -1, if entry could not be created (e.g. invalid token).
	 */
	int createToDo(String token, String title, Priority priority, String description, LocalDate dueDate);
	
	/**
	 * Gets the whole list of ToDos of some user. The user is identified by a token.
	 * @param token Security token of the logged user
	 * @return List of the user's ToDos or null if it could be not retrieved (e.g. invalid token).
	 */
	List<Integer> listToDos(String token);
	
	/**
	 * Gets particular ToDo of some user. The user is identified by a token.
	 * @param token Security token of the logged user
	 * @param index Index of the ToDo of the user's list
	 * @return ToDo entry of the user or null if it could not be retrieved (e.g. invalid index).
	 */
	ToDo getToDo(String token, int index);
	
	/**
	 * Sets the login handler service.
	 * @param loginHandler Login handler service to be set
	 */
	void setLoginHandler(ILoginHandler loginHandler);
}
