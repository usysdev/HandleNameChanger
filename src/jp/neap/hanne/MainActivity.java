package jp.neap.hanne;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
			Logger.debug("�n���h���l�[�����R�[�h��=" + handleNameBeanList.size());
		}
		
		for (int beanIndex = 0 ; beanIndex < handleNameBeanList.size() ; beanIndex++) {
			// �n���h�����ύX���s�{�^��
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
								Logger.debug("�n���h�����ύX(����)=" + handleNameBean.id());
							}
						}
					}
				});
			}

			// ���O�C���A�J�E���g�E�n���h�����ݒ�{�^��
			{
				final HandleNameBean handleNameBean = handleNameBeanList.get(beanIndex);
				final Button btn = (Button)findViewById(beanIdToSettingButtonIdMap.get(handleNameBean.id()));
				btn.setOnClickListener(new View.OnClickListener() {
					/* (non-Javadoc)
					 * @see android.view.View.OnClickListener#onClick(android.view.View)
					 */
					@Override
					public void onClick(View buttonView) {
						// �|�b�v�A�b�v�_�C�A���O�p�ɐV�����r���[���쐬����B
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

						// �A�v���P�[�V������I������
				        {
				        	final RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.appTypeGroup);
				        	switch (curHandleNameBean.appType()) {
				        	case HandleNameBean.APPTYPE_GREE:
					        	radioGroup.check(R.id.radioGree);
					        	break;
				        	}
				        }
				        // ���O�C�������o��
				        {
				        	final TextView textView = (TextView)dialogView.findViewById(R.id.textLoginName);
				        	if (curHandleNameBean.appType() == HandleNameBean.APPTYPE_GREE) {
				        		textView.setText(R.string.gree_login_name);
				        	}
				        }
				        // ���O�C����
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLoginName);
				       		editBox.setText(curHandleNameBean.loginName());
				        }
				        // �p�X���[�h
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editPassword);
				       		editBox.setText(curHandleNameBean.password());
				        }
				        // �n���h����
				        {
				        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editHandleName);
				       		editBox.setText(curHandleNameBean.handleName());
				        }
				        // �h�������h���N�G�X�g
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
				        // ���̐ݒ�l���R�s�[����
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
							        // �h�������h���N�G�X�g
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

									// �{�^�����x���ɐV�����n���h������\������B
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

		// �Q�[���^�C�}�[�̋N���A�g
		{
			final ImageView btnGameTimer = (ImageView)findViewById(R.id.title_gametimer);
			btnGameTimer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					boolean bGameTimerInstalled = false;
					// �Q�[���^�C�}�[���C���X�g�[������Ă��邩���ׂ�
					{
						final PackageManager packageManager = view.getContext().getPackageManager();
						try {
							packageManager.getApplicationInfo("jp.neap.gametimer", 0);
							bGameTimerInstalled = true;
						} catch (NameNotFoundException __ignore__) {}
					}
					if (bGameTimerInstalled) {
						// �Q�[���^�C�}�[���N������
						Intent intent = new Intent();
						intent.setClassName("jp.neap.gametimer", "jp.neap.gametimer.GameTimerListActivity");						
						startActivity(intent);
					}
					else {
						// �Q�[���^�C�}�[�̃_�E�����[�h�y�[�W(Google Play)�ɑJ�ڂ���
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
