package com.diversedistractions.vehiclelog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "vehiclelog.db";
    public static final int DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VehiclesTable.VEHICLES_TABLE_CREATE);
        db.execSQL(FuelEntriesTable.FUEL_ENTRY_TABLE_CREATE);
        db.execSQL(ServiceEntriesTable.SERVICE_ENTRY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Code to back up the data before upgrading
        // Delete the old database
        db.execSQL(FuelEntriesTable.FUEL_ENTRY_TABLE_DELETE);
        db.execSQL(ServiceEntriesTable.SERVICE_ENTRY_TABLE_DELETE);
        db.execSQL(VehiclesTable.VEHICLES_TABLE_DELETE);
        // Create the database with the new structure
        onCreate(db);
        //TODO: Code to restore and alter data to match new db structure
    }
}
