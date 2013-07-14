/**
 * 
 */
package jp.neap.hanne;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.JsResult;

/**
 *
 */
public class BrowserActivity extends Activity {

	private static final int START = 0;

	private static final int GREE_LOGIN = 1;

	private static final int GREE_TOP_SP = 2;

	private static final int GREE_TOP_PC = 3;

	private static final int GREE_CHANGE_PROFILE_BEFORE = 4;

	private static final int GREE_CHANGE_PROFILE_AFTER = 5;

	private static final int GREE_DORILAND_APP_SETTING = 6;

	private static final int GREE_LOGOUT = 7;

	private String escapeForJavaScript(String value) {
		final StringBuffer buffer = new StringBuffer();
		for (int index = 0 ; index < value.length() ; index++) {
			final char c = value.charAt(index);
			switch (c) {
			case '"':
			case '\\':
				buffer.append('\\');
				break;
			}
			buffer.append(c);
		}
		return buffer.toString();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);

		final Intent browserIntent = getIntent();
		final int appType = browserIntent.getIntExtra("appType", 0);
		final String loginName = browserIntent.getStringExtra("loginName");
		final String password = browserIntent.getStringExtra("password");
		final String handleName = browserIntent.getStringExtra("handleName");
		final int doriReq = browserIntent.getIntExtra("doriReq", 0);

		if (Logger.isDebugEnabled()) {
			Logger.debug("appType=" + appType + ",loginName=" + loginName + ",password=" + password + ",handleName=" + handleName + ",doriReq=" + doriReq);
		}

		final WebView webView = (WebView)findViewById(R.id.webview); 

		webView.clearCache(true);
		webView.clearFormData();
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(false);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setDatabaseEnabled(false);
		webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.4; ja-jp; SH-02E Build/SB140) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
		
		if (appType == HandleNameBean.APPTYPE_GREE) {
			String cookie = CookieManager.getInstance().getCookie("http://gree.jp/");
			if (Logger.isDebugEnabled()) {
				Logger.debug("cookie=" + cookie);
			}
		}
		CookieManager.getInstance().removeAllCookie();

		// WebView内で遷移させる。
		webView.setWebViewClient(new WebViewClient() {

			int actionState = START;

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

				if (appType == HandleNameBean.APPTYPE_GREE) {
					greeSmartPhone(view, url, true);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);

				if (appType == HandleNameBean.APPTYPE_GREE) {
					greeSmartPhone(view, url, false);
				}
			}

			private void greeSmartPhone(WebView view, String url, boolean bOnStart) {
				if ("http://t.gree.jp/?action=login&ignore_sso=1&backto=".equals(url)) {
					if (actionState == START) {
						if (bOnStart) {
							setTitle(getString(R.string.tying_login));
						}
						else {
							if (Logger.isDebugEnabled()) {
								Logger.debug("ログイン画面");
							}
							setTitle(getString(R.string.tying_login));
							actionState = GREE_LOGIN;
							final String script;
							if ("".equals(password)) {
								script =
								"javascript:{" +
									"document.getElementById('user_mail').value = '" + loginName + "';" +
//									"document.getElementById('login_status').checked = false;" +
//									"document.getElementById('login_status').disabled = 'disabled';" +
//									"document.getElementById('login_registration').parentNode.style.display = 'none';" +
//									"document.getElementById('login_get_password').parentNode.style.display = 'none';" +
//									"document.getElementById('login_migration').parentNode.style.display = 'none';" +
//									"document.getElementById('login_app_login').parentNode.style.display = 'none';" +
//									"document.getElementById('footer-links').style.display = 'none';" +
									"var divElement = document.createElement('div');" +
									"divElement.innerHTML = '<font color=red>" + getApplicationContext().getString(R.string.login_usage) + "</font>';" +
									"document.getElementById('login-form').appendChild(divElement);" +
								"};";
							}
							else {
								script =
								"javascript:{" +
									"document.getElementById('user_mail').value = '" + loginName + "';" +
									"document.getElementById('user_password_login').value = '" + password + "';" +
									"document.getElementById('login_login').click();" +
								"};";
							}
							view.loadUrl(script);
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("http://games.gree.net/".equals(url)) {
					if (actionState == GREE_LOGIN) {
						if (bOnStart) {
							setTitle(getString(R.string.logined));
						}
						else {
							actionState = GREE_TOP_SP;
							if (Logger.isDebugEnabled()) {
								Logger.debug("トップ画面(SP)");
							}
							setTitle(getString(R.string.logined));
							view.loadUrl("http://gree.jp/?mode=preference&act=set&fepref=pc");
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("http://gree.jp/".equals(url)) {
					if (actionState == GREE_TOP_SP) {
						if (bOnStart) {
							setTitle(getString(R.string.logined));
						}
						else {
							actionState = GREE_TOP_PC;
							if (Logger.isDebugEnabled()) {
								Logger.debug("トップ画面(PC)");
							}
							setTitle(getString(R.string.logined));
							view.loadUrl("http://gree.jp/?mode=home&act=config_profile_form");
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("http://gree.jp/?mode=home&act=config_profile_form".equals(url)) {
					if (actionState == GREE_TOP_PC) {
						if (bOnStart) {
							setTitle(getString(R.string.changing));
						}
						else {
							actionState = GREE_CHANGE_PROFILE_BEFORE;
							if (Logger.isDebugEnabled()) {
								Logger.debug("プロフィール画面(変更前)");
							}
							setTitle(getString(R.string.changing));
//							String script =
//								"javascript:{" +
//									"document.getElementById('nick_name').value = '" + handleName + "';" +
//									"preview();" +
//									"document.forms.config.onsubmit();" +
//								"};";
							String script =
							"javascript:{" +
								"document.getElementById('nick_name').value = '" + escapeForJavaScript(handleName) + "';" +
								"document.forms.profile.submit();" +
							"};";
							view.loadUrl(script);
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("https://secure.gree.jp/?mode=home&act=config_profile_form".equals(url)) {
					if (actionState == GREE_CHANGE_PROFILE_BEFORE) {
						if (bOnStart) {
							setTitle(getString(R.string.changed));
						}
						else {
							actionState = GREE_CHANGE_PROFILE_AFTER;
							if (Logger.isDebugEnabled()) {
								Logger.debug("プロフィール画面(変更後)");
							}
							if (doriReq == HandleNameBean.DORILAND_REQUEST_NONE) {
								// ドリランドのリクエストは制御しないのでログアウトする。
								String script =
									"javascript:{" +
										"location.href='http://t.gree.jp/?mode=id&act=logout';" +
									"};";
								view.loadUrl(script);
							}
							else {
								String script =
										"javascript:{" +
											"location.href='http://apps.gree.net/gd/app/info/setting/view/98';" +
										"};";
									view.loadUrl(script);
							}
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("http://apps.gree.net/gd/app/info/setting/view/98".equals(url)) {
					// ドリランドのアプリ設定画面
					if (actionState == GREE_CHANGE_PROFILE_AFTER) {
						if (bOnStart) {
							setTitle(getString(R.string.dori_request_changing));
						}
						else {
							actionState = GREE_DORILAND_APP_SETTING;
							if (Logger.isDebugEnabled()) {
								Logger.debug("ドリランドのアプリ設定画面");
							}
							setTitle(getString(R.string.dori_request_changing));
							view.setTag(R.string.webview_tag, "WAITING");
							if (doriReq == HandleNameBean.DORILAND_REQUEST_ON) {
								String script =
										"javascript:{" +
											"document.getElementById('accept_request_message_email').checked = true;" +
											"document.getElementById('accept_request_message_push').checked = true;" +
											"document.getElementById('accept_request_message').checked = true;" +
											"sendRequest(this);" +
										"};";
								view.loadUrl(script);
							} else if (doriReq == HandleNameBean.DORILAND_REQUEST_OFF) {
								String script =
									"javascript:{" +
										"document.getElementById('accept_request_message_email').checked = false;" +
										"document.getElementById('accept_request_message_push').checked = false;" +
										"document.getElementById('accept_request_message').checked = false;" +
										"sendRequest(this);" +
									"};";
								view.loadUrl(script);
							}

							waitUntilRequestAccepted(view);
							
							{
								String script =
									"javascript:{" +
										"location.href='http://t.gree.jp/?mode=id&act=logout';" +
									"};";
								view.loadUrl(script);
							}
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				} else if ("http://t.gree.jp/?action=top".equals(url)) {
					if ((actionState == GREE_CHANGE_PROFILE_AFTER) || (actionState == GREE_DORILAND_APP_SETTING)) {
						if (bOnStart) {
							setTitle(getString(R.string.logouted));
						}
						else {
							actionState = GREE_LOGOUT;
							if (Logger.isDebugEnabled()) {
								Logger.debug("ログアウトしました");
							}
							setTitle(getString(R.string.logouted));
//							Toast.makeText(getApplicationContext(), R.string.change_handle_name_success, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
					else {
						if (!bOnStart) {
							Toast.makeText(getApplicationContext(), R.string.change_handle_name_maybe_failed, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				}
			}

			private void waitUntilRequestAccepted(WebView view) {
				final long startTime = System.currentTimeMillis();
				for (;;) {
					if ("ACCEPTED".equals(view.getTag(R.string.webview_tag))) {
						return;
					}
					if ((System.currentTimeMillis() - startTime) >= 10000L) {
						return;
					}
					try {
						Thread.sleep(100L);
					} catch (Exception __ignore__) {}
				}
			}

			private void greePC(WebView view, String url) {
				if ("http://gree.jp/".equals(url)) {
					// <input type="submit">タグの名前が name="submit" なので、
					// document.forms['名前'].submit() が動かない。
					String script =
						"javascript:{" +
						"document.getElementById('user_mail').value = '" + loginName + "';" +
						"document.getElementById('user_password').value = '" + password + "';" +
						"var elements = document.getElementsByName('submit');" +
						"elements[0].click();" +
						"};";
					view.loadUrl(script);
				} else if ("http://gree.jp/?action=home".equals(url)) {
					view.loadUrl("http://gree.jp/?mode=home&act=config_profile_form");
				} else if ("http://gree.jp/?mode=home&act=config_profile_form".equals(url)) {
					String script =
						"javascript:{" +
						"document.getElementById('nick_name').value = '" + handleName + "';" +
						"document.forms['profile'].submit();" +
						"};";
					view.loadUrl(script);
				} else if ("https://secure.gree.jp/?mode=home&act=config_profile_form".equals(url)) {
					finish();
				}
			}
		});

		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				setTitle(message);
				result.confirm();
				view.setTag(R.string.webview_tag, "ACCEPTED");
				return true;
			}
		});
		
		// 最初に開くURL
		if (appType == HandleNameBean.APPTYPE_GREE) {
			webView.loadUrl("http://t.gree.jp/?action=login&from_signuptop=login");
		}
	}
}
