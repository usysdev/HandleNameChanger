package jp.neap.hanne;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {

	public static final Map<Integer,Integer> beanIdToChangeButtonIdMap = new HashMap<Integer,Integer>(); 

	public static final Map<Integer,Integer> beanIdToSettingButtonIdMap = new HashMap<Integer,Integer>(); 

	public static final Map<Integer,Integer> settingButtonIdToBeanIdMap = new HashMap<Integer,Integer>(); 

	static {
		beanIdToChangeButtonIdMap.put(Integer.valueOf(0), Integer.valueOf(R.id.handlename_change_button_00));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(1), Integer.valueOf(R.id.handlename_change_button_01));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(2), Integer.valueOf(R.id.handlename_change_button_02));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(3), Integer.valueOf(R.id.handlename_change_button_03));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(4), Integer.valueOf(R.id.handlename_change_button_04));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(5), Integer.valueOf(R.id.handlename_change_button_05));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(6), Integer.valueOf(R.id.handlename_change_button_06));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(7), Integer.valueOf(R.id.handlename_change_button_07));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(8), Integer.valueOf(R.id.handlename_change_button_08));
		beanIdToChangeButtonIdMap.put(Integer.valueOf(9), Integer.valueOf(R.id.handlename_change_button_09));

		beanIdToSettingButtonIdMap.put(Integer.valueOf(0), Integer.valueOf(R.id.handlename_settings_dialog_button_00));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(1), Integer.valueOf(R.id.handlename_settings_dialog_button_01));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(2), Integer.valueOf(R.id.handlename_settings_dialog_button_02));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(3), Integer.valueOf(R.id.handlename_settings_dialog_button_03));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(4), Integer.valueOf(R.id.handlename_settings_dialog_button_04));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(5), Integer.valueOf(R.id.handlename_settings_dialog_button_05));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(6), Integer.valueOf(R.id.handlename_settings_dialog_button_06));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(7), Integer.valueOf(R.id.handlename_settings_dialog_button_07));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(8), Integer.valueOf(R.id.handlename_settings_dialog_button_08));
		beanIdToSettingButtonIdMap.put(Integer.valueOf(9), Integer.valueOf(R.id.handlename_settings_dialog_button_09));
		
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_00), Integer.valueOf(0));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_01), Integer.valueOf(1));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_02), Integer.valueOf(2));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_03), Integer.valueOf(3));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_04), Integer.valueOf(4));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_05), Integer.valueOf(5));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_06), Integer.valueOf(6));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_07), Integer.valueOf(7));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_08), Integer.valueOf(8));
		settingButtonIdToBeanIdMap.put(Integer.valueOf(R.id.handlename_settings_dialog_button_09), Integer.valueOf(9));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final List<HandleNameBean> handleNameBeanList;
		{
			final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
			final SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				handleNameBeanList = dbHelper.listAllSorted(db);
			}
			finally {
				db.close();
			}
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("ハンドルネームレコード数=" + handleNameBeanList.size());
		}
		
		for (int beanIndex = 0 ; beanIndex < handleNameBeanList.size() ; beanIndex++) {
			// ハンドル名変更実行ボタン
			{
				final HandleNameBean handleNameBean = handleNameBeanList.get(beanIndex);
				final Button btn = (Button)findViewById(beanIdToChangeButtonIdMap.get(handleNameBean.id()));
				btn.setText(Html.fromHtml(handleNameBean.formattedHandleName(getApplicationContext())));

				btn.setOnClickListener(new View.OnClickListener() {
					/* (non-Javadoc)
					 * @see android.view.View.OnClickListener#onClick(android.view.View)
					 */
					@Override
					public void onClick(View buttonView) {
						final HandleNameBean kickBean;
						final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
						final SQLiteDatabase db = dbHelper.getReadableDatabase();
						try {
							kickBean = dbHelper.getRecordById(db, handleNameBean.id());
						}
						finally {
							db.close();
						}

						if (!kickBean.isEmpty()) {
							final Intent browserIntent = new Intent(getApplicationContext(), BrowserActivity.class);
							browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							browserIntent.putExtra("appType", kickBean.appType());
							browserIntent.putExtra("loginName", kickBean.loginName());
							browserIntent.putExtra("password", kickBean.password());
							browserIntent.putExtra("handleName", kickBean.handleName());
							browserIntent.putExtra("doriReq", kickBean.doriReq());
							startActivity(browserIntent);
						}
						else {
							if (Logger.isDebugEnabled()) {
								Logger.debug("ハンドル名変更(無効)=" + handleNameBean.id());
							}
						}
					}
				});
			}

			// ログインアカウント・ハンドル名設定ボタン
			{
				final HandleNameBean handleNameBean = handleNameBeanList.get(beanIndex);
				final Button btn = (Button)findViewById(beanIdToSettingButtonIdMap.get(handleNameBean.id()));
				btn.setOnClickListener(new View.OnClickListener() {
					/* (non-Javadoc)
					 * @see android.view.View.OnClickListener#onClick(android.view.View)
					 */
					@Override
					public void onClick(View buttonView) {
						// ポップアップダイアログ用に新しいビューを作成する。
						LayoutInflater inflater = LayoutInflater.from(buttonView.getContext());
				        final View dialogView = inflater.inflate(R.layout.handlename_setting, null);

				        final HandleNameBean curHandleNameBean;
				        {
							final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
							final SQLiteDatabase db = dbHelper.getReadableDatabase();
							try {
								curHandleNameBean = dbHelper.getRecordById(db, settingButtonIdToBeanIdMap.get(buttonView.getId()));
							}
							finally {
								db.close();
							}
						}

						// アプリケーションを選択する
				        {
				        	final RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.appTypeGroup);
				        	switch (curHandleNameBean.appType()) {
				        	case HandleNameBean.APPTYPE_GREE:
					        	radioGroup.check(R.id.radioGree);
					        	break;
				        	}
				        }
				        // ログイン名見出し
				        {
				        	final TextView textView = (TextView)dialogView.findViewById(R.id.textLoginName);
				        	if (curHandleNameBean.appType() == HandleNameBean.APPTYPE_GREE) {
				        		textView.setText(R.string.gree_login_name);
				        	}
				        }
				        // ログイン名
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLoginName);
				       		editBox.setText(curHandleNameBean.loginName());
				        }
				        // パスワード
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editPassword);
				       		editBox.setText(curHandleNameBean.password());
				        }
				        // ハンドル名
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editHandleName);
				       		editBox.setText(curHandleNameBean.handleName());
				        }
				        // ドリランドリクエスト
				        {
				        	final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerDoriReq);
				        	switch (curHandleNameBean.doriReq()) {
				        	case HandleNameBean.DORILAND_REQUEST_ON:
				        		spinner.setSelection(1);
				        		break;
				        	case HandleNameBean.DORILAND_REQUEST_OFF:	
				        		spinner.setSelection(2);
				        		break;
				        	default:
				        		spinner.setSelection(0);
				        	}
				        }
				        // 他の設定値をコピーする
				        {
				    		final List<HandleNameBean> handleNameBeanList;
				    		{
				    			final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
				    			final SQLiteDatabase db = dbHelper.getReadableDatabase();
				    			try {
				    				handleNameBeanList = dbHelper.listAllSorted(db);
				    			}
				    			finally {
				    				db.close();
				    			}
					    		final ArrayAdapter<SpinnerHandleNameBean> adapterItemList = new ArrayAdapter<SpinnerHandleNameBean>(buttonView.getContext(), android.R.layout.simple_spinner_item);
					    		adapterItemList.add(new SpinnerHandleNameBean(-1, 0, "", "", "", 0, getApplicationContext().getString(R.string.do_not_copy)));
					    		for (int index = 0 ; index < handleNameBeanList.size() ; index++) {
					    			final HandleNameBean bean = handleNameBeanList.get(index);
					    			if (bean.id() != curHandleNameBean.id()) {
					    				final StringBuffer buffer = new StringBuffer();
					    				String sep = "";
					    				if (!"".equals(bean.handleName())) {
					    					buffer.append(sep).append(bean.handleName());
					    					sep = "/";
					    				}
					    				if (bean.doriReq() == HandleNameBean.DORILAND_REQUEST_ON) {
					    					buffer.append(sep).append(getApplicationContext().getString(R.string.doriland_request_on));
					    					sep = "/";
					    				} else if (bean.doriReq() == HandleNameBean.DORILAND_REQUEST_OFF) {
					    					buffer.append(sep).append(getApplicationContext().getString(R.string.doriland_request_off));
					    					sep = "/";
					    				}
					    				adapterItemList.add(new SpinnerHandleNameBean(
					    						bean.id(),
					    						bean.appType(),
					    						bean.loginName(),
					    						bean.password(),
					    						bean.handleName(),
					    						bean.doriReq(),
					    						buffer.toString()));

					    			}
					    		}
					    		final Spinner spinnerCopySettings = (Spinner)dialogView.findViewById(R.id.spinnerCopySettings);
					    		spinnerCopySettings.setAdapter(adapterItemList);
					    		spinnerCopySettings.setOnItemSelectedListener(new OnItemSelectedListener(){

									/* (non-Javadoc)
									 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
									 */
									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										final Spinner spinnerTarget = (Spinner)arg0;
										final SpinnerHandleNameBean bean = (SpinnerHandleNameBean)spinnerTarget.getSelectedItem();
										if (bean.id() > -1) {
											{
									        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLoginName);
									       		editBox.setText(bean.loginName());
											}
											{
									        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editPassword);
									       		editBox.setText(bean.password());
											}
											{
									        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editHandleName);
									       		editBox.setText(bean.handleName());
											}
									        {
									        	final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerDoriReq);
									        	switch (bean.doriReq()) {
									        	case HandleNameBean.DORILAND_REQUEST_ON:
									        		spinner.setSelection(1);
									        		break;
									        	case HandleNameBean.DORILAND_REQUEST_OFF:	
									        		spinner.setSelection(2);
									        		break;
									        	default:
									        		spinner.setSelection(0);
									        	}
									        }
										}
									}

									/* (non-Javadoc)
									 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
									 */
									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub
										
									}
					    			
					    		});
				    		}
				        }

				        new AlertDialog.Builder(buttonView.getContext())
			        	.setView(dialogView)
			        	.setPositiveButton(
			        			dialogView.getContext().getString(R.string.ok), 
			        		new DialogInterface.OnClickListener() {          
			        			@Override
			        			public void onClick(DialogInterface dialog, int which) {
			        				int appType = HandleNameBean.APPTYPE_GREE;
			        				String loginName = "";
									String password = "";
									String handleName = "";
									int doriReq = HandleNameBean.DORILAND_REQUEST_NONE;
									
									{
							        	final RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.appTypeGroup);
							        	switch (radioGroup.getCheckedRadioButtonId()) {
							        	case R.id.radioGree:
							        		appType = HandleNameBean.APPTYPE_GREE;
							        		break;
							        	}
									}
									{
							        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLoginName);
							        	loginName = editBox.getText().toString();
									}
									{
							        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editPassword);
							        	password = editBox.getText().toString();
									}
									{
										final EditText editBox = (EditText)dialogView.findViewById(R.id.editHandleName);
										handleName = editBox.getText().toString();
									}
							        // ドリランドリクエスト
							        {
							        	final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerDoriReq);
							        	switch (spinner.getSelectedItemPosition()) {
							        	case 1:
							        		doriReq = HandleNameBean.DORILAND_REQUEST_ON;
							        		break;
							        	case 2:	
							        		doriReq = HandleNameBean.DORILAND_REQUEST_OFF;
							        		break;
							        	}
							        }

									final HandleNameBean newHandleNameBean = new HandleNameBean(
											curHandleNameBean.id(),
											appType,
											loginName,
											password,
											handleName,
											doriReq); 

									{
										final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
										final SQLiteDatabase db = dbHelper.getWritableDatabase();
										try {
											dbHelper.updateRecord(db, newHandleNameBean);
										}
										finally {
											db.close();
										}
									}

									// ボタンラベルに新しいハンドル名を表示する。
									{
										final Button btn = (Button)findViewById(beanIdToChangeButtonIdMap.get(handleNameBean.id()));
										btn.setText(Html.fromHtml(newHandleNameBean.formattedHandleName(getApplicationContext())));
									}
			        			}
			        		})
			        	.setNegativeButton(
			        			dialogView.getContext().getString(R.string.cancel), 
			        		new DialogInterface.OnClickListener() {          
			        			@Override
			        			public void onClick(DialogInterface dialog, int which) {
			        			}
			        		})
			        	.show();
					}
				});
			}
		}
	}
}
