package utility;

import java.util.logging.Logger;

/**
 * Utility class that helps to validate passwords.
 */
public class PasswordValidator {
	private static Logger logger = Logger.getLogger(PasswordValidator.class.getName());

	private final static int MINIMAL_LENGTH = 6;

	/**
	 * Checks if the given password is valid and meets the password requirements.
	 * @param password Password to be validated
	 * @return True, if the password is valid and meets all the password requirements or false otherwise.
	 */
	public static boolean validate(String password) {
		if (password.length() < MINIMAL_LENGTH) {
			logger.warning("Password is too short! It must have at least " + MINIMAL_LENGTH + " characters!");
			return false;
		} else if (!containsDigit(password)) {
			logger.warning("Password must contain digit!");
			return false;
		} else if (!containsUpperCase(password)) {
			logger.warning("Password must contain upper case character!");
			return false;
		} else if (!containsLowerCase(password)) {
			logger.warning("Password must contain lower case character!");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the given password contains at least one upper-case character.
	 */
	private static boolean containsUpperCase(String password) {
		return password.matches(".*[A-Z].*");
	}

	/**
	 * Checks if the given password contains at least one lower-case character.
	 */
	private static boolean containsLowerCase(String password) {
		if (password.matches(".*[a-z].*"))
			return true;
		return false;
	}

	/**
	 * Checks if the given password contains at least one digit character.
	 */
	private static boolean containsDigit(String password) {
		if (password.matches(".*\\d.*"))
			return true;
		return false;
	}
}