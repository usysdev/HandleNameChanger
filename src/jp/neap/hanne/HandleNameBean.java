/**
 * 
 */
package jp.neap.hanne;

import android.content.Context;

/**
 *
 */
public class HandleNameBean {

	public static final int APPTYPE_GREE = 1;

	// リクエストは制御しない。
	public static final int REQUEST_NONE = 0;	

	// リクエストをONにする。
	public static final int REQUEST_ON = 1;

	// リクエストをOFFにする。
	public static final int REQUEST_OFF = 2;

	private final int id;

	private final int appType;

	private final String loginName;

	private final String password;

	private final String handleName;

	private final int request;
	
	public HandleNameBean(
			int id,
			int appType,
			String loginName,
			String password,
			String handleName,
			int request) {

		this.id = id;
		this.appType = appType;
		this.loginName = loginName;
		this.password = password;
		this.handleName = handleName;
		this.request = request;
	}

	public int id() { return id; }

	public int appType() { return appType; }

	public String loginName() { return loginName; }

	public String password() { return password; }

	public String handleName() { return handleName; }

	public int request() { return request; } 

	public String formattedHandleName(Context context) {
		if (isEmpty()) {
			return "<font color=#888888>" + context.getString(R.string.undefined_handle_name) + "</font>";
		}
		if (appType == APPTYPE_GREE) {
			String result = handleName;
			if (request == REQUEST_ON) {
				result = result + "/" + context.getString(R.string.request_on);
			} else if (request == REQUEST_OFF) {
				result = result + "/" + context.getString(R.string.request_off);
			}
			return result;
		}
		return "<font color=#888888>" + context.getString(R.string.undefined_handle_name) + "</font>";
	}
	
	public boolean isEmpty() {
		final boolean bFilled = (appType == APPTYPE_GREE) &&
			!"".equals(loginName) &&
//			!"".equals(password) &&
			!"".equals(handleName);
		return !bFilled;
	}
}
