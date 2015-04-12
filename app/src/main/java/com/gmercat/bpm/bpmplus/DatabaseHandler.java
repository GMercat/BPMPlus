package com.gmercat.bpm.bpmplus;

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
        aDb.execSQL(BPMDAO.TABLE_DROP);
        onCreate(aDb);
    }
}
