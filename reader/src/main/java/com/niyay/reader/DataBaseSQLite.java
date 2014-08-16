package com.niyay.reader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DataBaseSQLite
{
	private static final String DATABASE_NAME = "Niyay.db";
	private static final int DATABASE_VERSION = 2;

	private SQLiteDatabase db;

    private static DataBaseSQLite instance = null;

    public DataBaseSQLite(Context context)
	{
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
        Config cfg = Config.getInstance();
        String sql = "SELECT * FROM Config WHERE 1";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst())
        {
            cfg.user = cursor.getString(0);
            cfg.password = cursor.getString(1);
            cfg.category = cursor.getInt(2);
        }
        else{
            cfg.user = "";
            cfg.password = "";
            cfg.category = 0;
            sql = "INSERT INTO Config(user, password, category)VALUES('','', 0)";
            db.execSQL(sql);
        }
	}

    public static DataBaseSQLite getInstance(Context context) {
        if (instance == null)
            instance = new DataBaseSQLite(context);
        return instance;
    }

    public static DataBaseSQLite getInstance() {
        return instance;
    }

    public void save_config(Config config)
	{
        String sql = "UPDATE Config SET user = '" + config.user + "', password = '" + config.password + "', category = '" + config.category + "' WHERE 1";
		db.execSQL(sql);
	}

    public void add_favorite(String story_id)
    {
        String sql = "INSERT INTO Favorite(story_id)VALUES(" + story_id + ")";
        db.execSQL(sql);
    }

    public void remove_favorite(String story_id)
    {
        db.delete("Favorite", "story_id = " + story_id, null);
    }

    public Boolean isFavorite(String story_id)
    {
        String sql = "SELECT * FROM Favorite WHERE story_id = " + story_id;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst()?true:false;
    }

    public List<NameValuePair> getFavorite(Integer page, Integer limit)
    {
        String sql = "SELECT * FROM Favorite WHERE 1 LIMIT " + Integer.toString(page * limit) + "," + Integer.toString(limit);
        Cursor cursor = db.rawQuery(sql, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        if (cursor.moveToFirst())
        {
            for(int i=0; i< cursor.getCount(); i++)
            {
                nameValuePairs.add(new BasicNameValuePair("story_" + i, Integer.toString(cursor.getInt(0))));
                cursor.moveToNext();
            }
        }
        return nameValuePairs;
    }

	private static class OpenHelper extends SQLiteOpenHelper
	{

		OpenHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
            db.execSQL("CREATE TABLE Config(user TEXT, password TEXT, category INTEGER);");
            db.execSQL("CREATE TABLE Favorite(story_id INTEGER);");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
            db.execSQL("DROP TABLE IF EXISTS Config");
            db.execSQL("DROP TABLE IF EXISTS Favorite");
			onCreate(db);
		}
	}
}
