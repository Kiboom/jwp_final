package core.web.tags;

import core.utils.NextStringUtils;

/**
 * JSP EL Functions
 */
public class NextFunctions {

	/**
	 * 문자열의 HTML을 Escape한다.
	 *
	 * @param str
	 * @return
	 */
	public static String h(String str) {
		return NextStringUtils.escapeHtml(str);
	}

	/**
	 * 문자열의 새줄기호를 &lt;br&gt; 로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String br(String str) {
		return NextStringUtils.newLineToBr(str);
	}

	/**
	 * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String hbr(String str) {
		return NextStringUtils.escapeHtmlAndNewLineToBr(str);
	}
}