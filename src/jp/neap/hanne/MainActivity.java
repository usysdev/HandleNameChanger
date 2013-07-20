package jp.neap.hanne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		// http://s7643.socode.us/question/5081300c4f1eba38a4263fd0
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);
		
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
						final ArrayList<Integer> requestTargetIdList;
						final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
						final SQLiteDatabase db = dbHelper.getReadableDatabase();
						try {
							kickBean = dbHelper.getRecordById(db, handleNameBean.id());
							requestTargetIdList = dbHelper.listRequestTargetGames(db);
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
							browserIntent.putExtra("request", kickBean.request());
							browserIntent.putExtra("requestTargetIdList", requestTargetIdList);
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
				        final View mainDialogView = inflater.inflate(R.layout.handlename_setting, null);

				        final HandleNameBean curHandleNameBean;
				        final int requestTargetIdCount;
				        {
							final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
							final SQLiteDatabase db = dbHelper.getReadableDatabase();
							try {
								curHandleNameBean = dbHelper.getRecordById(db, settingButtonIdToBeanIdMap.get(buttonView.getId()));
								requestTargetIdCount = dbHelper.getRequestTargetGamesCount(db);
							}
							finally {
								db.close();
							}
						}

				        // ログイン名見出し
				        {
				        	final TextView textView = (TextView)mainDialogView.findViewById(R.id.textLoginName);
				        	if (curHandleNameBean.appType() == HandleNameBean.APPTYPE_GREE) {
				        		textView.setText(R.string.gree_login_name);
				        	}
				        }
				        // ログイン名
				        {
				        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editLoginName);
				       		editBox.setText(curHandleNameBean.loginName());
				        }
				        // パスワード
				        {
				        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editPassword);
				       		editBox.setText(curHandleNameBean.password());
				        }
				        // ハンドル名
				        {
				        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editHandleName);
				       		editBox.setText(curHandleNameBean.handleName());
				        }
				        // リクエスト
				        {
				        	final Spinner spinner = (Spinner)mainDialogView.findViewById(R.id.spinnerRequest);
				        	switch (curHandleNameBean.request()) {
				        	case HandleNameBean.REQUEST_ON:
				        		spinner.setSelection(1);
				        		break;
				        	case HandleNameBean.REQUEST_OFF:	
				        		spinner.setSelection(2);
				        		break;
				        	default:
				        		spinner.setSelection(0);
				        	}
				        	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

								@Override
								public void onItemSelected(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
						        	final TextView txtNoGameSelected = (TextView)mainDialogView.findViewById(R.id.txtNoGameSelected);
						        	if (position == 0) {
						        		txtNoGameSelected.setVisibility(View.GONE);
						        	}
						        	else {
						        		final int requestTargetIdCount;
						        		{
						        			final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
						        			final SQLiteDatabase db = dbHelper.getReadableDatabase();
						        			try {
						        				requestTargetIdCount = dbHelper.getRequestTargetGamesCount(db);
						        			}
						        			finally {
						        				db.close();
						        			}
						        		}
						        		txtNoGameSelected.setVisibility((requestTargetIdCount > 0) ? View.GONE : View.VISIBLE);
						        	}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									
								}
							});
				        }
				        // ゲーム選択メッセージ
				        {
				        	final TextView txtNoGameSelected = (TextView)mainDialogView.findViewById(R.id.txtNoGameSelected);
				        	txtNoGameSelected.setVisibility((requestTargetIdCount > 0) ? View.VISIBLE : View.GONE);
				        }
				        // ゲーム選択
				        {
				        	final Button btnSelectGames = (Button)mainDialogView.findViewById(R.id.btn_select_app);
				        	btnSelectGames.setOnClickListener(new View.OnClickListener(){

								@Override
								public void onClick(View buttonView) {
									// TODO Auto-generated method stub
									final LayoutInflater inflater = LayoutInflater.from(buttonView.getContext());
							        final View gameDialogView = inflater.inflate(R.layout.request_games, null);
							        final Set<Integer> requestTargetGameIdSet;
							        {
							        	final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
							        	final SQLiteDatabase db = dbHelper.getReadableDatabase();
							        	try {
							        		requestTargetGameIdSet = dbHelper.listRequestTargetGamesSet(db);
							        	}
							        	finally {
							        		db.close();
							        	}
							        }
							        {
							        	final CheckBox chkGame0 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_0);
							        	chkGame0.setChecked(requestTargetGameIdSet.contains(0));
							        }
							        {
							        	final CheckBox chkGame1 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_1);
							        	chkGame1.setChecked(requestTargetGameIdSet.contains(1));
							        }
							        {
							        	final CheckBox chkGame2 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_2);
							        	chkGame2.setChecked(requestTargetGameIdSet.contains(2));
							        }
							        {
							        	final CheckBox chkGame3 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_3);
							        	chkGame3.setChecked(requestTargetGameIdSet.contains(3));
							        }
							        {
							        	final CheckBox chkGame4 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_4);
							        	chkGame4.setChecked(requestTargetGameIdSet.contains(4));
							        }
							        {
							        	final CheckBox chkGame5 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_5);
							        	chkGame5.setChecked(requestTargetGameIdSet.contains(5));
							        }
							        {
							        	final CheckBox chkGame6 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_6);
							        	chkGame6.setChecked(requestTargetGameIdSet.contains(6));
							        }
							        new AlertDialog.Builder(buttonView.getContext())
							        .setTitle(R.string.select_games)
							        .setView(gameDialogView)
						        	.setPositiveButton(
						        			gameDialogView.getContext().getString(R.string.ok), 
						        		new DialogInterface.OnClickListener() {          
						        			@Override
						        			public void onClick(DialogInterface dialog, int which) {
									        	final DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_FILENAME, null, DBHelper.DB_VERSION);
									        	final SQLiteDatabase db = dbHelper.getWritableDatabase();
									        	final int requestTargetGameCount;
									        	try {
											        {
											        	final CheckBox chkGame0 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_0);
											        	dbHelper.updateRecordRequestGame(db, 0, chkGame0.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame1 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_1);
											        	dbHelper.updateRecordRequestGame(db, 1, chkGame1.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame2 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_2);
											        	dbHelper.updateRecordRequestGame(db, 2, chkGame2.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame3 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_3);
											        	dbHelper.updateRecordRequestGame(db, 3, chkGame3.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame4 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_4);
											        	dbHelper.updateRecordRequestGame(db, 4, chkGame4.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame5 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_5);
											        	dbHelper.updateRecordRequestGame(db, 5, chkGame5.isChecked() ? 1 : 0);
											        }
											        {
											        	final CheckBox chkGame6 = (CheckBox)gameDialogView.findViewById(R.id.chk_game_6);
											        	dbHelper.updateRecordRequestGame(db, 6, chkGame6.isChecked() ? 1 : 0);
											        }
											        requestTargetGameCount = dbHelper.getRequestTargetGamesCount(db);
									        	}
									        	finally {
									        		db.close();
									        	}
									        	final Spinner spinner = (Spinner)mainDialogView.findViewById(R.id.spinnerRequest);
									        	final TextView txtNoGameSelected = (TextView)mainDialogView.findViewById(R.id.txtNoGameSelected);
									        	if (spinner.getSelectedItemPosition() == 0) {
									        		txtNoGameSelected.setVisibility(View.GONE);
									        	}
									        	else {
									        		txtNoGameSelected.setVisibility((requestTargetGameCount > 0) ? View.GONE : View.VISIBLE);
									        	}
						        			}
						        		})
						        	.setNegativeButton(
						        			gameDialogView.getContext().getString(R.string.cancel), 
						        		new DialogInterface.OnClickListener() {          
						        			@Override
						        			public void onClick(DialogInterface dialog, int which) {
						        			}
						        		})
						        	.show();
								}
							});
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
					    		adapterItemList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
					    				if (bean.request() == HandleNameBean.REQUEST_ON) {
					    					buffer.append(sep).append(getApplicationContext().getString(R.string.request_on));
					    					sep = "/";
					    				} else if (bean.request() == HandleNameBean.REQUEST_OFF) {
					    					buffer.append(sep).append(getApplicationContext().getString(R.string.request_off));
					    					sep = "/";
					    				}
					    				adapterItemList.add(new SpinnerHandleNameBean(
					    						bean.id(),
					    						bean.appType(),
					    						bean.loginName(),
					    						bean.password(),
					    						bean.handleName(),
					    						bean.request(),
					    						buffer.toString()));

					    			}
					    		}
					    		final Spinner spinnerCopySettings = (Spinner)mainDialogView.findViewById(R.id.spinnerCopySettings);
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
									        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editLoginName);
									       		editBox.setText(bean.loginName());
											}
											{
									        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editPassword);
									       		editBox.setText(bean.password());
											}
											{
									        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editHandleName);
									       		editBox.setText(bean.handleName());
											}
									        {
									        	final Spinner spinner = (Spinner)mainDialogView.findViewById(R.id.spinnerRequest);
									        	switch (bean.request()) {
									        	case HandleNameBean.REQUEST_ON:
									        		spinner.setSelection(1);
									        		break;
									        	case HandleNameBean.REQUEST_OFF:	
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
			        	.setView(mainDialogView)
			        	.setTitle(R.string.dlgtitle_gree)
			        	.setPositiveButton(
			        			mainDialogView.getContext().getString(R.string.ok), 
			        		new DialogInterface.OnClickListener() {          
			        			@Override
			        			public void onClick(DialogInterface dialog, int which) {
			        				int appType = HandleNameBean.APPTYPE_GREE;
			        				String loginName = "";
									String password = "";
									String handleName = "";
									int request = HandleNameBean.REQUEST_NONE;
									
									{
							        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editLoginName);
							        	loginName = editBox.getText().toString();
									}
									{
							        	final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editPassword);
							        	password = editBox.getText().toString();
									}
									{
										final EditText editBox = (EditText)mainDialogView.findViewById(R.id.editHandleName);
										handleName = editBox.getText().toString();
									}
							        // ドリランドリクエスト
							        {
							        	final Spinner spinner = (Spinner)mainDialogView.findViewById(R.id.spinnerRequest);
							        	switch (spinner.getSelectedItemPosition()) {
							        	case 1:
							        		request = HandleNameBean.REQUEST_ON;
							        		break;
							        	case 2:	
							        		request = HandleNameBean.REQUEST_OFF;
							        		break;
							        	}
							        }

									final HandleNameBean newHandleNameBean = new HandleNameBean(
											curHandleNameBean.id(),
											appType,
											loginName,
											password,
											handleName,
											request); 

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
			        			mainDialogView.getContext().getString(R.string.cancel), 
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

		// ゲームタイマーの起動連携
		{
			final ImageView btnGameTimer = (ImageView)findViewById(R.id.title_gametimer);
			btnGameTimer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					boolean bGameTimerInstalled = false;
					// ゲームタイマーがインストールされているか調べる
					{
						final PackageManager packageManager = view.getContext().getPackageManager();
						try {
							packageManager.getApplicationInfo("jp.neap.gametimer", 0);
							bGameTimerInstalled = true;
						} catch (NameNotFoundException __ignore__) {}
					}
					if (bGameTimerInstalled) {
						// ゲームタイマーを起動する
						Intent intent = new Intent();
						intent.setClassName("jp.neap.gametimer", "jp.neap.gametimer.GameTimerListActivity");						
						startActivity(intent);
					}
					else {
						// ゲームタイマーのダウンロードページ(Google Play)に遷移する
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=jp.neap.gametimer"));
						startActivity(intent);
					}
				}
			});
			btnGameTimer.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						{
							final ImageView ivGameTimer = (ImageView)view.findViewById(R.id.title_gametimer);
							ivGameTimer.setImageResource(R.drawable.icon_gametimer_mo);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					default:
					{
						final ImageView ivGameTimer = (ImageView)view.findViewById(R.id.title_gametimer);
						ivGameTimer.setImageResource(R.drawable.icon_gametimer);
					}
					}
					return false;
				}
			});
		}
	}
}
