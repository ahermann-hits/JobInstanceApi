package de.hits.jobinstance.common;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

/**
 * Class to generate random passwords.
 * 
 * @implNote Implements a simple algorithm to generate a password based on a
 *           dictionary of characters, numbers and specials characters.
 * 
 * @author André Hermann
 * @since 04.04.2018
 * @version 1.0
 */
@Component
public class PasswordGenerator {

	/**
	 * The small letters for the dictionary: abcdefghijklmnopqrstuvwxyz
	 */
	public static final String alpha = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * The capital letters for the dictionary: ABCDEFGHIJKLMNOPQRSTUVWXYZ
	 */
	public static final String alphaCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * The numeric characters for the dictionary: 0123456789
	 */
	public static final String numeric = "0123456789";
	/**
	 * The special characters for the dictionary:
	 * .&#58;,&#59;&#33;&#63;&#64;&#35;&#36;&#37;&#94;&amp;&#42;&#95;&#61;&#43;&#45;&frasl;&#124;
	 */
	public static final String special = ".:,;!?@#$%^&*_=+-/|";

	private SecureRandom random = new SecureRandom();

	/**
	 * Method to generate a random password with a default length and default
	 * dictionary.
	 * 
	 * @return the generated password.
	 */
	public String generatePassword() {
		return generatePassword(64, PasswordGenerator.alpha, PasswordGenerator.alphaCaps, PasswordGenerator.numeric,
				PasswordGenerator.special);
	}

	/**
	 * Method to generate a random password with a default dictionary and
	 * parameterisable length.
	 * 
	 * @param length
	 *            the length of the generated password
	 * @return the generated password.
	 */
	public String generatePassword(int length) {
		return generatePassword(length, PasswordGenerator.alpha, PasswordGenerator.alphaCaps, PasswordGenerator.numeric,
				PasswordGenerator.special);
	}

	/**
	 * Method to generate a random password with a parameterisable length and
	 * parameterisable dictionary.
	 * 
	 * @param length
	 *            the length of the generated password
	 * @param alpha
	 *            the alpha characters to use for the password.
	 * @param alphaCaps
	 *            the capital alpha characters to use for the password.
	 * @param numeric
	 *            the numeric characters to use for the password.
	 * @param special
	 *            the special characters to use for the password. (e.g.:
	 *            !@#$%^&amp;*_=+-/)
	 * @return the generated password.
	 */
	public String generatePassword(int length, String alpha, String alphaCaps, String numeric, String special) {
		String result = "";

		StringBuilder temp = new StringBuilder();
		if (alpha != null) {
			temp.append(alpha.trim());
		}
		if (alphaCaps != null) {
			temp.append(alphaCaps.trim());
		}
		if (numeric != null) {
			temp.append(numeric.trim());
		}
		if (special != null) {
			temp.append(special.trim());
		}
		String dictionary = temp.toString();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(dictionary.length());
			result += dictionary.charAt(index);
		}

		return result;
	}
}