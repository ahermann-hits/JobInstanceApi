package de.hits.jobinstance.common.utils;

/**
 * A minimal version of my NumberUtils, only with the methods I need in this
 * project.
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public class NumberUtils {

	public static boolean isEmpty(Number number) {
		boolean result = true;

		if (number != null && isNumber(number)) {
			result = false;
		}

		return result;
	}

	public static boolean isNumber(Object number) {
		boolean result = false;

		if (number instanceof Number) {
			result = true;
		} else if (number instanceof String) {
			try {
				Double.parseDouble((String) number);
				result = true;
			} catch (NumberFormatException nfe) {
			}
		}

		return result;
	}

	public static Integer getInteger(Object number) {
		Integer result = null;

		if (number instanceof Number) {
			result = ((Number) number).intValue();
		} else if (number instanceof String) {
			try {
				result = Integer.parseInt((String) number);
			} catch (Exception e) {
			}
		}

		return result;
	}

	public static Long getLong(Object number) {
		Long result = null;

		if (number instanceof Number) {
			result = ((Number) number).longValue();
		} else if (number instanceof String) {
			try {
				result = Long.parseLong((String) number);
			} catch (Exception e) {
			}
		}

		return result;
	}

	public static long getNullSaveLong(Object number) {
		Long result = 0l;

		if (number instanceof Number) {
			result = ((Number) number).longValue();
		} else if (number instanceof String) {
			try {
				result = Long.parseLong((String) number);
			} catch (Exception e) {
			}
		}

		return result;
	}
}