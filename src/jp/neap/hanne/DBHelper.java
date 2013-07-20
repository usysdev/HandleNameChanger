/**
 * 
 */
package jp.neap.hanne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_FILENAME = "hannechanger.db";

	public static final int DB_VERSION = 3;

	public static final String TABLE_NAME_HANDLE_NAME = "handlename";

	public static final String TABLE_NAME_REQUEST_GAME = "request_game";

	private Context context;

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context; 
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE " + TABLE_NAME_HANDLE_NAME + " " +
				"(id INTEGER NOT NULL" +
				",app_type INTEGER NOT NULL" +
				",login_name TEXT NOT NULL" +
				",password TEXT NOT NULL" +
				",handle_name TEXT NOT NULL" +
				",request INTEGER DEFAULT 0" +
				")"
		);

		db.execSQL(
				"CREATE TABLE " + TABLE_NAME_REQUEST_GAME + " " +
				"(id INTEGER NOT NULL" +
				",request_target INTEGER NOT NULL" +
				",game_name TEXT NOT NULL" +
				",display_order INTEGER NOT NULL" +
				")"
		);

		// èâä˙íl
		{
			final String[][] initialValues = {
				{"0", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", context.getString(R.string.sample_handle_name_0)},
				{"1", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", context.getString(R.string.sample_handle_name_1)},
				{"2", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", context.getString(R.string.sample_handle_name_2)},
				{"3", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"4", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"5", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"6", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"7", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"8", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
				{"9", String.valueOf(HandleNameBean.APPTYPE_GREE), "", "", ""},
			};

			for (int paramIndex = 0 ; paramIndex < initialValues.length ; paramIndex++) {
				db.execSQL("INSERT INTO " + TABLE_NAME_HANDLE_NAME + " " +
					"(id" +
					",app_type" +
					",login_name" +
					",password" +
					",handle_name" +
					") VALUES (" +
						 "'" + initialValues[paramIndex][0] + "'" +
						",'" + initialValues[paramIndex][1] + "'" +
						",'" + initialValues[paramIndex][2] + "'" +
						",'" + CryptUtil.encrypt(initialValues[paramIndex][3]) + "'" +
						",'" + initialValues[paramIndex][4] + "'" +
					")");
			}
		}
		{
			final String[][] initialValues = {
					{"0", "0", context.getString(R.string.game_0), "1"},
					{"1", "0", context.getString(R.string.game_1), "2"},
					{"2", "0", context.getString(R.string.game_2), "3"},
					{"3", "0", context.getString(R.string.game_3), "4"},
					{"4", "0", context.getString(R.string.game_4), "5"},
					{"5", "0", context.getString(R.string.game_5), "6"},
					{"6", "0", context.getString(R.string.game_6), "7"},
			};
			for (int paramIndex = 0 ; paramIndex < initialValues.length ; paramIndex++) {
				db.execSQL("INSERT INTO " + TABLE_NAME_REQUEST_GAME + " " +
					"(id" +
					",request_target" +
					",game_name" +
					",display_order" +
					") VALUES (" +
						 "'" + initialValues[paramIndex][0] + "'" +
						",'" + initialValues[paramIndex][1] + "'" +
						",'" + initialValues[paramIndex][2] + "'" +
						",'" + initialValues[paramIndex][3] + "'" +
					")");
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			if (newVersion == 2) {
				// 1 Å® 2
				db.execSQL(
						"ALTER TABLE " + TABLE_NAME_HANDLE_NAME + " " +
						"ADD COLUMN dori_req INTEGER DEFAULT 0"
				);
			} else if (newVersion == 3) {
				// 1 Å® 3
				db.execSQL(
						"CREATE TABLE " + TABLE_NAME_REQUEST_GAME + " " +
						"(id INTEGER NOT NULL" +
						",request_target INTEGER NOT NULL" +
						",game_name TEXT NOT NULL" +
						",display_order INTEGER NOT NULL" +
						")"
				);
				{
					final String[][] initialValues = {
							{"0", "0", context.getString(R.string.game_0), "1"},
							{"1", "0", context.getString(R.string.game_1), "2"},
							{"2", "0", context.getString(R.string.game_2), "3"},
							{"3", "0", context.getString(R.string.game_3), "4"},
							{"4", "0", context.getString(R.string.game_4), "5"},
							{"5", "0", context.getString(R.string.game_5), "6"},
							{"6", "0", context.getString(R.string.game_6), "7"},
					};
					for (int paramIndex = 0 ; paramIndex < initialValues.length ; paramIndex++) {
						db.execSQL("INSERT INTO " + TABLE_NAME_REQUEST_GAME + " " +
							"(id" +
							",request_target" +
							",game_name" +
							",display_order" +
							") VALUES (" +
								 "'" + initialValues[paramIndex][0] + "'" +
								",'" + initialValues[paramIndex][1] + "'" +
								",'" + initialValues[paramIndex][2] + "'" +
								",'" + initialValues[paramIndex][3] + "'" +
							")");
					}
				}
			}
		} else if (oldVersion == 2) {
			if (newVersion == 3) {
				// 2 Å® 3
				db.execSQL("ALTER TABLE " + TABLE_NAME_HANDLE_NAME + " RENAME TO temp_table");
				db.execSQL(
						"CREATE TABLE " + TABLE_NAME_HANDLE_NAME + " " +
						"(id INTEGER NOT NULL" +
						",app_type INTEGER NOT NULL" +
						",login_name TEXT NOT NULL" +
						",password TEXT NOT NULL" +
						",handle_name TEXT NOT NULL" +
						",request INTEGER DEFAULT 0" +
						")"
				);
				db.execSQL("INSERT INTO " + TABLE_NAME_HANDLE_NAME + " (id,app_type,login_name,password,handle_name,request)" +
						   " SELECT id,app_type,login_name,password,handle_name,dori_req FROM temp_table");
				db.execSQL("DROP TABLE temp_table");

				db.execSQL(
						"CREATE TABLE " + TABLE_NAME_REQUEST_GAME + " " +
						"(id INTEGER NOT NULL" +
						",request_target INTEGER NOT NULL" +
						",game_name TEXT NOT NULL" +
						",display_order INTEGER NOT NULL" +
						")"
				);
				String doriland = "0";
				final List<HandleNameBean> beanList = listAllSorted(db);
				for (int i = 0 ; i < beanList.size() ; i++) {
					final HandleNameBean bean = beanList.get(i);
					if ((bean.request() == HandleNameBean.REQUEST_ON) || (bean.request() == HandleNameBean.REQUEST_OFF)) {
						doriland = "1";
					}
				}
				{
					final String[][] initialValues = {
							{"0", doriland, context.getString(R.string.game_0), "1"},
							{"1", "0", context.getString(R.string.game_1), "2"},
							{"2", "0", context.getString(R.string.game_2), "3"},
							{"3", "0", context.getString(R.string.game_3), "4"},
							{"4", "0", context.getString(R.string.game_4), "5"},
							{"5", "0", context.getString(R.string.game_5), "6"},
							{"6", "0", context.getString(R.string.game_6), "7"},
					};
					for (int paramIndex = 0 ; paramIndex < initialValues.length ; paramIndex++) {
						db.execSQL("INSERT INTO " + TABLE_NAME_REQUEST_GAME + " " +
								"(id" +
								",request_target" +
								",game_name" +
								",display_order" +
								") VALUES (" +
									 "'" + initialValues[paramIndex][0] + "'" +
									",'" + initialValues[paramIndex][1] + "'" +
									",'" + initialValues[paramIndex][2] + "'" +
									",'" + initialValues[paramIndex][3] + "'" +
								")");
					}
				}
			}
		}
	}

	public List<HandleNameBean> listAllSorted(SQLiteDatabase db) {
		final List<HandleNameBean> beanList = new LinkedList<HandleNameBean>();
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						",app_type" +
						",login_name" +
						",password" +
						",handle_name" +
						",request" +
						" FROM " + TABLE_NAME_HANDLE_NAME +
						" ORDER BY id ASC",
						new String[0]);
		try {
			if (c.moveToFirst()) {
				do {
					beanList.add(new HandleNameBean(
							c.getInt(0),
							c.getInt(1),
							c.getString(2),
							CryptUtil.decrypt(c.getString(3)),
							c.getString(4),
							c.getInt(5)));
				} while (c.moveToNext());
			}
			return beanList;
		}
		finally {
			c.close();
		}
	}

	public HandleNameBean getRecordById(SQLiteDatabase db, int id) {
		final String[] params = new String[1];
		params[0] = String.valueOf(id);
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						",app_type" +
						",login_name" +
						",password" +
						",handle_name" +
						",request" +
						" FROM " + TABLE_NAME_HANDLE_NAME +
						" WHERE id=?",
						params);
		try {
			if (c.moveToFirst()) {
				HandleNameBean bean = new HandleNameBean(
							c.getInt(0),
							c.getInt(1),
							c.getString(2),
							CryptUtil.decrypt(c.getString(3)),
							c.getString(4),
							c.getInt(5));
				return bean;
			}
			return null;
		}
		finally {
			c.close();
		}
	}

	public void updateRecord(SQLiteDatabase db, HandleNameBean bean) {
		final ContentValues assoc = new ContentValues();

		assoc.put("app_type", bean.appType());
		assoc.put("login_name", bean.loginName());
		assoc.put("password", CryptUtil.encrypt(bean.password()));
		assoc.put("handle_name", bean.handleName());
		assoc.put("request", bean.request());

		db.update(TABLE_NAME_HANDLE_NAME, assoc, "id=?", new String[]{String.valueOf(bean.id())});
	}

	public ArrayList<Integer> listRequestTargetGames(SQLiteDatabase db) {
		final ArrayList<Integer> idList = new ArrayList<Integer>();
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						" FROM " + TABLE_NAME_REQUEST_GAME +
						" WHERE request_target=1" +
						" ORDER BY display_order ASC",
						new String[0]);
		try {
			if (c.moveToFirst()) {
				do {
					idList.add(c.getInt(0));
				} while (c.moveToNext());
			}
			return idList;
		}
		finally {
			c.close();
		}
	}

	public Set<Integer> listRequestTargetGamesSet(SQLiteDatabase db) {
		final Set<Integer> idList = new HashSet<Integer>();
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						" FROM " + TABLE_NAME_REQUEST_GAME +
						" WHERE request_target=1",
						new String[0]);
		try {
			if (c.moveToFirst()) {
				do {
					idList.add(c.getInt(0));
				} while (c.moveToNext());
			}
			return idList;
		}
		finally {
			c.close();
		}
	}

	public int getRequestTargetGamesCount(SQLiteDatabase db) {
		Cursor c = db.rawQuery(
						"SELECT" +
						" COUNT(*)" +
						" FROM " + TABLE_NAME_REQUEST_GAME +
						" WHERE request_target=1",
						new String[0]);
		try {
			if (c.moveToFirst()) {
				do {
					return c.getInt(0);
				} while (c.moveToNext());
			}
			return 0;
		}
		finally {
			c.close();
		}
	}

	public void updateRecordRequestGame(SQLiteDatabase db, int id, int requestTarget) {
		final ContentValues assoc = new ContentValues();

		assoc.put("request_target", requestTarget);

		db.update(TABLE_NAME_REQUEST_GAME, assoc, "id=?", new String[]{String.valueOf(id)});
	}
}
