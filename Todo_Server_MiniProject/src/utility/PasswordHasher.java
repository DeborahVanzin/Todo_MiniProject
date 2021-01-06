package utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class that helps to hash passwords.
 */
public class PasswordHasher {
	
	/**
	 * Calculates SHA-256 hash of a given password.
	 * @param password Password to be hashed
	 * @return SHA-256 hash of the given password.
	 */
	public static String calculatePasswordHash(String password) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encodedHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Converts byte hash representation to a hexadecimal string.
	 * @param hash Raw byte hash
	 * @return String representation of the byte hash.
	 */
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
