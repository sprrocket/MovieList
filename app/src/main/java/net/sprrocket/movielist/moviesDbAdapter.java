package net.sprrocket.movielist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sprrocket on 7/20/2017.
 *
 * IF YOU DON'T KNOW WHAT YOU'RE DOING WITH SQL,
 * PLEASE TAKE EXTREME CAUTION HERE
 *
 */

public class moviesDbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DIRECTOR = "director";
    public static final String KEY_YEAR = "year";
    public static final String KEY_RATING = "rating";

    private static final String TAG = "moviesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Movies";
    private static final String SQLITE_TABLE = "Entry";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
            KEY_ROWID + " integer PRIMARY KEY autoincrement," +
            KEY_TITLE + "," +
            KEY_DIRECTOR + "," +
            KEY_YEAR + "," +
            KEY_RATING + "," +
            " UNIQUE (" + KEY_TITLE +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }

    }

    public moviesDbAdapter(Context ctx){//TODO: Figure out what the heck this does
        this.mCtx = ctx;
    }

    public moviesDbAdapter open() throws SQLException{
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){//TODO: close the database when the app exits?
        if(mDbHelper !=null){
            mDbHelper.close();
        }
    }

    public long createMovie(String title, String director, String year, String rating){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DIRECTOR, director);
        initialValues.put(KEY_YEAR, year);
        initialValues.put(KEY_RATING, rating);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllMovies(){//might get used to 'delete all'
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    //TODO: write a function to delete a specific movie

    public Cursor fetchMoviesByTitle(String inputText) throws SQLException{//TODO: Implement search with this func
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if(inputText==null || inputText.length() ==0){
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_TITLE, KEY_DIRECTOR,
                    KEY_YEAR, KEY_RATING}, null, null, null, null, null);
        }
        else{
            mCursor=mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_DIRECTOR, KEY_YEAR,
            KEY_RATING}, KEY_TITLE + "like '%" + inputText + "%'", null, null, null, null, null);
        }
        if (mCursor!=null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllMovies(){
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
        KEY_TITLE, KEY_DIRECTOR, KEY_YEAR, KEY_RATING},
        null, null, null, null, null);

        if(mCursor !=null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeMovies(){
        createMovie("Example Title", "Example Director", "9999", "F");
    }
}
