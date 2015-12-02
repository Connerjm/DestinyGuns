package edu.uw.connerjm.destinyguns.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Conner on 02/12/2015.
 */
public class Database
{

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Destiny.db";

    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public Database(Context context)
    {
        mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
    }

    public boolean insertBookmark(String gunname, String whichlist)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("gunname", gunname);
        contentValues.put("list", whichlist);

        long rowId = mSQLiteDatabase.insert("bookmarks", null, contentValues);
        return rowId != -1;
    }

    public boolean isGunInList(String gunname, String whichlist)
    {
        String[] columns = {"gunname", "list"};
        Cursor c = mSQLiteDatabase.query("bookmarks", columns, null, null, null, null, null, null);
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++)
        {
            String name = c.getString(0);
            String list = c.getString(1);
            if(gunname.equals(name) && whichlist.equals(list))
            {
                return true;
            }
        }
        return false;
    }

    public void deleteBookmark(String gunname, String whichlist)
    {
        mSQLiteDatabase.delete("bookmarks",
                gunname + "='gunname' AND " + whichlist + "='list'", null);
    }

    public void closeDB()
    {
        mSQLiteDatabase.close();
    }
}

class DBHelper extends SQLiteOpenHelper
{

    private static final String CREATE_USER_SQL =
        "CREATE TABLE IF NOT EXISTS bookmarks " +
                "(gunname TEXT, list TEXT, PRIMARY KEY(gunname, list))";
    private static final String DROP_USER_SQL = "DROP TABLE IF EXISTS bookmarks";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_USER_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i , int i1)
    {
        sqLiteDatabase.execSQL(DROP_USER_SQL);
        onCreate(sqLiteDatabase);
    }
}