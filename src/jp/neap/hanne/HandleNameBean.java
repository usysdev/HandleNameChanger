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

	// ドリランドのリクエストは制御しない。
	public static final int DORILAND_REQUEST_NONE = 0;	

	// ドリランドのリクエストをONにする。
	public static final int DORILAND_REQUEST_ON = 1;

	// ドリランドのリクエストをOFFにする。
	public static final int DORILAND_REQUEST_OFF = 2;

	private final int id;

	private final int appType;

	private final String loginName;

	private final String password;

	private final String handleName;

	private final int doriReq;
	
	public HandleNameBean(
			int id,
			int appType,
			String loginName,
			String password,
			String handleName,
			int doriReq) {

		this.id = id;
		this.appType = appType;
		this.loginName = loginName;
		this.password = password;
		this.handleName = handleName;
		this.doriReq = doriReq;
	}

	public int id() { return id; }

	public int appType() { return appType; }

	public String loginName() { return loginName; }

	public String password() { return password; }

	public String handleName() { return handleName; }

	public int doriReq() { return doriReq; } 

	public String formattedHandleName(Context context) {
		if (isEmpty()) {
			return context.getString(R.string.undefined_handle_name);
		}
		if (appType == APPTYPE_GREE) {
			String result = handleName;
			if (doriReq == DORILAND_REQUEST_ON) {
				result = result + "/" + context.getString(R.string.doriland_request_on);
			} else if (doriReq == DORILAND_REQUEST_OFF) {
				result = result + "/" + context.getString(R.string.doriland_request_off);
			}
			return result;
		}
		return context.getString(R.string.undefined_handle_name);
	}
	
	public boolean isEmpty() {
		final boolean bFilled = (appType == APPTYPE_GREE) &&
			!"".equals(loginName) &&
//			!"".equals(password) &&
			!"".equals(handleName);
		return !bFilled;
	}
}
