/**
 * 
 */
package jp.neap.hanne;

import java.util.ArrayList;

import android.content.Context;

/**
 *
 */
public class RequestSetting {

	private final String name;

	private final String url;

	private final String scriptON;

	private final String scriptOFF;	
	
	public RequestSetting(
			String name,
			String url,
			String scriptON,
			String scriptOFF) {
		this.name = name;
		this.url = url;
		this.scriptON = scriptON;
		this.scriptOFF = scriptOFF;
	}

	public String name() { return name; }

	public String url() { return url; }

	public String scriptON() { return scriptON; }

	public String scriptOFF() { return scriptOFF; }

	public static RequestSetting getRequestSetting(Context context, ArrayList<Integer> idList) {
		Integer id = idList.get(0);
		switch (id.intValue()) {
		case 0:
			// ドリランド
			return new RequestSetting(
					context.getString(R.string.game_0),
					"http://apps.gree.net/gd/app/info/setting/view/98",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = true;" +
						"document.getElementById('accept_request_message_push').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = false;" +
						"document.getElementById('accept_request_message_push').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 1:
			// 聖戦ケルベロス
			return new RequestSetting(
					context.getString(R.string.game_1),
					"http://apps.gree.net/gd/app/info/setting/view/1242",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = true;" +
						"document.getElementById('accept_request_message_push').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = false;" +
						"document.getElementById('accept_request_message_push').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 2:
			// モンプラ
			return new RequestSetting(
					context.getString(R.string.game_2),
					"http://apps.gree.net/gd/app/info/setting/view/99",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = true;" +
						"document.getElementById('accept_request_message_push').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = false;" +
						"document.getElementById('accept_request_message_push').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 3:
			// 釣り★スタ
			return new RequestSetting(
					context.getString(R.string.game_3),
					"http://apps.gree.net/gd/app/info/setting/view/96",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = true;" +
						"document.getElementById('accept_request_message_push').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = false;" +
						"document.getElementById('accept_request_message_push').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 4:
			// プロ野球ドリームナイン
			return new RequestSetting(
					context.getString(R.string.game_4),
					"http://apps.gree.net/gd/app/info/setting/view/1236",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_email').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 5:
			// ガンダムマスターズ
			return new RequestSetting(
					context.getString(R.string.game_5),
					"http://apps.gree.net/gd/app/info/setting/view/2676",
					"javascript:{" +
						"document.getElementById('accept_request_message_push').checked = true;" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message_push').checked = false;" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		case 6:
			// 大相撲カード決戦
			return new RequestSetting(
					context.getString(R.string.game_6),
					"http://apps.gree.net/gd/app/info/setting/view/56641",
					"javascript:{" +
						"document.getElementById('accept_request_message').checked = true;" +
						"sendRequest(this);" +
					"};",
					"javascript:{" +
						"document.getElementById('accept_request_message').checked = false;" +
						"sendRequest(this);" +
					"};");
		}
		return null;
	}
}
