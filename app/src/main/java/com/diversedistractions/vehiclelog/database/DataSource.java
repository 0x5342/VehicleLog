package com.diversedistractions.vehiclelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diversedistractions.vehiclelog.models.VehicleItem;

public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

//    public VehicleItem createVehicleItem(VehicleItem item) {
//        ContentValues values = item.toValues();
//        mDatabase.insert(VehiclesTable.VEHICLE_DATABASE_TABLE, null, values);
//        return item;
//    }

    public long getVehicleDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, VehiclesTable.VEHICLE_DATABASE_TABLE);
    }

//    public List<DataVehicle> getAllItems(String category) {
//        List<DataVehicle> dataVehicles = new ArrayList<>();
//
//        Cursor cursor = null;
//        if (category == null) {
//            //TODO: fix this cursor
//            cursor = mDatabase.query(VehiclesTable.VEHICLE_DATABASE_TABLE,
//                    VehiclesTable.ALL_VEHICLE_COLUMNS, null, null, null, null,
//                    VehiclesTable.COL_VEHICLE_MAKE);
//        } else {
//            String[] categories = (category);
//            cursor = mDatabase.query(VehiclesTable.VEHICLE_DATABASE_TABLE,
//                    VehiclesTable.ALL_VEHICLE_COLUMNS, VehiclesTable.COL_VEHICLE_MAKE)
//        }
//    }
}
