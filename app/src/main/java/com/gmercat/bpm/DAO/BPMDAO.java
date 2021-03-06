package com.gmercat.bpm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmercat.bpm.bpmplus.BPM;

public class BPMDAO {
    public static final String TABLE_NAME   = "bpms";
    public static final String KEY          = "id";
    public static final String NAME         = "name";
    public static final String TITLE        = "title";
    public static final String ARTIST       = "artist";
    public static final String VALUE        = "value";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT, " + ARTIST + " TEXT, " + VALUE + " REAL);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static final String TABLE_MIGRATION_1_2_ADD_TITLE = "ALTER TABLE " + TABLE_NAME +
            " ADD " + TITLE + " TEXT;";

    public static final String TABLE_MIGRATION_1_2_COPY_NAME_TITLE = "UPDATE " + TABLE_NAME +
            " SET " + TITLE + "=" + NAME + ";";

    public static final String TABLE_MIGRATION_1_2_ADD_ARTIST = "ALTER TABLE " + TABLE_NAME +
            " ADD " + ARTIST + " TEXT DEFAULT \"\";";

    protected final static int      VERSION     = 2;
    protected final static String   FILENAME    = "database.db";

    protected SQLiteDatabase    mDb         = null;
    protected DatabaseHandler   mHandler    = null;

    public BPMDAO(Context aContext) {
        mHandler = new DatabaseHandler(aContext, FILENAME, null, VERSION);
    }

    public void open() {
        mDb = mHandler.getWritableDatabase();
    }

    public void close() {
        mDb.close();
    }

    public int add (BPM aBpm) {
        ContentValues value = new ContentValues ();
        value.put(TITLE, aBpm.getTitle());
        value.put(ARTIST, aBpm.getArtist());
        value.put(VALUE, aBpm.getBpm());
        return (int)mDb.insert(TABLE_NAME, null, value);
    }

    public void del (long aId) {
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(aId)});
    }

    public void delAll () {
        mDb.delete(TABLE_NAME, null, null);
    }

    public void update (BPM aBpm) {
        ContentValues value = new ContentValues ();
        value.put(TITLE, aBpm.getTitle());
        value.put(ARTIST, aBpm.getArtist());
        value.put(VALUE, aBpm.getBpm ());
        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(aBpm.getId())});
    }

    public Cursor getAllBPMs () {
        return mDb.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
