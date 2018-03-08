import java.util.Collection;
import java.util.Map;

/**
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写 Ognl访问静态方法的表达式为:
 * 
 * @class@method(args) 示例使用:
 * 
 *                     <pre>
 * 	&lt;if test=&quot;@Ognl@isNotEmpty(userId)&quot;&gt;
 * 	and user_id = #{userId}
 * &lt;/if&gt;
 * </pre>
 * 
 * @author badqiu
 */
public class Ognl {
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			if (((String) o).trim().length() == 0 || isBlank((String) o)) {
				return true;
			}
		} else if (o instanceof Collection) {
			if (((Collection) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (((Object[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).isEmpty()) {
				return true;
			}
		} else if (o instanceof Long) {
			if (((Long) o) == null) {
				return true;
			}
		} else if (o instanceof Short) {
			if (((Short) o) == null) {
				return true;
			}
		} else {
			return false;
		}
		return false;

	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	public static boolean isNotEmpty(Long o) {
		return !isEmpty(o);
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o) {
		if (o == null)
			return false;
		if (o instanceof Number) {
			return true;
		}
		if (o instanceof String) {
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	/**
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 */
	public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	/**
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 */
	public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

}