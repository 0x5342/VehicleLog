package com.diversedistractions.vehiclelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.util.ArrayList;
import java.util.List;

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

    public VehicleItem createVehicleItem(VehicleItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(VehiclesTable.VEHICLE_DATABASE_TABLE, null, values);
        return item;
    }

    public long getVehicleDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, VehiclesTable.VEHICLE_DATABASE_TABLE);
    }

    public void seedDatabase(List<VehicleItem> vehicleItemList) {
        long numItems = getVehicleDataItemsCount();
        if (numItems == 0) {
            for (VehicleItem item : vehicleItemList) {
                try {
                    createVehicleItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<VehicleItem> getAllItems() {
        List<VehicleItem> vehicleItems = new ArrayList<>();
        Cursor cursor = mDatabase.query(VehiclesTable.VEHICLE_DATABASE_TABLE,
                VehiclesTable.ALL_VEHICLE_COLUMNS, null, null, null, null, null);

        while (cursor.moveToNext()){
            VehicleItem item = new VehicleItem();
            item.setVehicleId(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ROW_ID)));
            item.setVehicleType(cursor.getString(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TYPE)));
            item.setVehicleMake(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE)));
            item.setVehicleModel(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL)));
            item.setVehicleYear(cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
            item.setVehicleVin(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_VIN)));
            item.setVehicleLp(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_LP)));
            item.setVehicleLpRenewalDate(cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_REN_DATE)));
            item.setVehicleImage(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE)));
            item.setVehicleTdMilage(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TD_MPG)));
            item.setVehicleNotes(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_NOTE)));
        }
        cursor.close();
        return vehicleItems;
    }
}
