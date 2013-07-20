package jp.neap.hanne;

public class Logger {

	public static boolean isDebugEnabled() {
		return true;
	}

	public static void debug(String message) {
		android.util.Log.v("HANNE", message);
	}
}
