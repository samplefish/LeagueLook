package edu.csulb.LeagueLook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by William on 6/24/2017.
 */

public class GameDatabaseHelper extends SQLiteOpenHelper {
    public static final String ID_COLUMN = "_id";
    public static final String NAME_COLUMN = "gameID";
    public static final String DATABASE_TABLE = "GameIDs";
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    " %s integer primary key autoincrement, " +
                    " %s text)",
            DATABASE_TABLE, ID_COLUMN, NAME_COLUMN);

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}