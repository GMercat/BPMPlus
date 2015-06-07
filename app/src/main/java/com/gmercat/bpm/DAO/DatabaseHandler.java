package com.gmercat.bpm.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context aContext, String aName, SQLiteDatabase.CursorFactory aFactory, int aVersion) {
        super(aContext, aName, aFactory, aVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase aDb) {
        aDb.execSQL(BPMDAO.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aDb, int aOldVersion, int aNewVersion) {
        if (aOldVersion != aNewVersion) {
            if ((aOldVersion == 1) && (aNewVersion == 2)) {
                aDb.execSQL(BPMDAO.TABLE_MIGRATION_1_2_ADD_TITLE);
                aDb.execSQL(BPMDAO.TABLE_MIGRATION_1_2_COPY_NAME_TITLE);
                aDb.execSQL(BPMDAO.TABLE_MIGRATION_1_2_ADD_ARTIST);
            }
        } else {
            aDb.execSQL(BPMDAO.TABLE_DROP);
            onCreate(aDb);
        }
    }
}
