package core.utils;

import org.apache.commons.lang3.StringUtils;

public class NextStringUtils extends StringUtils {
	public static String escapeHtml(String str) {
		if (isEmpty(str)) {
			return str;
		}
		String escaped = str.replaceAll("&", "&amp;");
		escaped = escaped.replaceAll("\\<", "&lt;");
		escaped = escaped.replaceAll("\\>", "&gt;");
		escaped = escaped.replaceAll("\"", "&quot;");
		escaped = escaped.replaceAll("\'", "&#39;"); // not not use "&apos;" -
														// IE does not allow it.
		escaped = escaped.replaceAll("  ", " &nbsp;");
		return escaped;
	}

	/**
	 * 문자열의 새줄 기호를 &lt;br&gt; 태그로 변환한다.
	 *
	 * @param str
	 * @return
	 */
	public static String newLineToBr(String str) {
		if (isEmpty(str)) {
			return str;
		}
		String converted = str.replaceAll("\n", "<br />\n");
		return converted;
	}

	/**
	 * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String escapeHtmlAndNewLineToBr(String str) {
		return newLineToBr(escapeHtml(str));
	}
}
