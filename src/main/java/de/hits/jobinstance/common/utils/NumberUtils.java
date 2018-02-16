package de.hits.jobinstance.common.utils;

public class NumberUtils {

	public static boolean isEmpty(Number num) {
		if (num == null) {
			return true;
		}
		if (isNumber(num)) {
			return false;
		}
		return true;
	}

	public static Integer getInteger(Object input) {
		Integer result = null;

		if (input instanceof Number) {
			result = ((Number) input).intValue();
		} else if (input instanceof String) {
			try {
				result = Integer.parseInt((String) input);
			} catch (Exception e) {
			}
		}

		return result;
	}

	public static Long getLong(Object input) {
		Long result = null;

		if (input instanceof Number) {
			result = ((Number) input).longValue();
		} else if (input instanceof String) {
			try {
				result = Long.parseLong((String) input);
			} catch (Exception e) {
			}
		}

		return result;
	}

	public static long getNullSaveLong(Object input) {
		if (input instanceof Number) {
			return ((Number) input).longValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Long.parseLong((String) s);
		} else {
			return 0;
		}
	}

	public static boolean isNumber(Object o) {
		if (o instanceof Number) {
			return true;
		} else if (o instanceof String) {
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (NumberFormatException nfe) {
				return false;
			}
		} else {
			return false;
		}
	}
}