package utility;

public class PasswordValidator {
	private final static int MINIMAL_LENGTH = 6;

	public static boolean validate(String password) {
		if (password.length() < MINIMAL_LENGTH) {
			System.out.println("Password is too short! It must have at least " + MINIMAL_LENGTH + " characters!");
			return false;
		} else if (!containsDigit(password)) {
			System.out.println("Password must contain digit!");
			return false;
		} else if (!containsUpperCase(password)) {
			System.out.println("Password must contain upper case character!");
			return false;
		} else if (!containsLowerCase(password)) {
			System.out.println("Password must contain lower case character!");
			return false;
		}
		return true;
	}
	
	private static boolean containsUpperCase(String password) {
		return password.matches(".*[A-Z].*");
	}

	private static boolean containsLowerCase(String password) {
		if (password.matches(".*[a-z].*"))
			return true;
		return false;
	}

	private static boolean containsDigit(String password) {
		if (password.matches(".*\\d.*"))
			return true;
		return false;
	}
}