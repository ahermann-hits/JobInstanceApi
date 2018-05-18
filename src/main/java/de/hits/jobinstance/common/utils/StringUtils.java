package de.hits.jobinstance.common.utils;

/**
 * A minimal version of my StringUtils, only with the methods I need in this
 * project.
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public class StringUtils {

	public static boolean isEmpty(String s) {
		boolean result = false;

		if (s == null) {
			result = true;
		} else if (s.trim().isEmpty()) {
			result = true;
		} else if (s.equals("null")) {
			result = true;
		}

		return result;
	}

	public static boolean getNullSaveBoolean(String value) {
		boolean result = false;

		if (value != null) {
			value = value.toLowerCase();

			if ("true".equals(value)) {
				result = Boolean.TRUE;
			} else if ("1".equals(value)) {
				result = Boolean.TRUE;
			} else if ("yes".equals(value)) {
				result = Boolean.TRUE;
			} else if ("y".equals(value)) {
				result = Boolean.TRUE;
			} else if ("sí".equals(value)) {
				result = Boolean.TRUE;
			} else if ("да".equals(value)) {
				result = Boolean.TRUE;
			} else if ("ja".equals(value)) {
				result = Boolean.TRUE;
			} else if ("j".equals(value)) {
				result = Boolean.TRUE;
			} else if ("oui".equals(value)) {
				result = Boolean.TRUE;
			} else if ("ok".equals(value)) {
				result = Boolean.TRUE;
			} else if ("x".equals(value)) {
				result = Boolean.TRUE;
			}
		}

		return result;
	}

	public static boolean equals(String str, String compare) {
		boolean result = false;

		if (str == null) {
			result = compare == null;
		} else if (compare == null) {
			result = false;
		} else {
			result = str.trim().equalsIgnoreCase(compare.trim());
		}

		return result;
	}
}