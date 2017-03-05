package com.diversedistractions.vehiclelog.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.diversedistractions.vehiclelog.database.VehicleLogContentProvider;
import com.diversedistractions.vehiclelog.database.VehiclesTable;


/**
 * A class that will reorder the integers stored under the ModOrder so that the most recently
 * modified vehicle is 1 and the remaining vehicles descend from there in their original order.
 */

public class VehicleModOrderTool {

    private int mNumOfVehs = 0;
    private int mLatestId = 0;
    private int mLatestModNum = 0;
    private Context mContext;

    public VehicleModOrderTool(Context context) {
        this.mContext = context;
    }

    public void RenumberVehicleModOrder(Uri uri) {
        int mNewModNum = 0;
        // if a valid URI was passed in (after a vehicle delete, this will be null)
        if (uri != null) {
            // get the vehicle ID and Modified Order number from the latest modified vehicle
            Cursor latestCursor = mContext.getContentResolver().query
                    (uri, null, null, null, null, null);
            if (latestCursor != null) {
                latestCursor.moveToFirst();
                mLatestId = latestCursor.getInt
                        (latestCursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ID));
                mLatestModNum = latestCursor.getInt
                        (latestCursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER));
                latestCursor.close();
            }
            mNewModNum = 2;
        } else {
            mNewModNum = 1;
        }

        Cursor cursor = mContext.getContentResolver().query
                (VehicleLogContentProvider.VEHICLE_CONTENT_URI, null, null, null,
                        VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, null);
        if (cursor != null) {
            mNumOfVehs = cursor.getCount();
        }
        // if newly created or after delete, thus modNum is 0, set the modNum to the end of the list
        if (mLatestModNum == 0) {
            mLatestModNum = mNumOfVehs;
        }

        // as long as this isn't the only vehicle, nor is it already marked as latest modified
        if (cursor != null) {
            if (mNumOfVehs > 1 && mLatestModNum != 1) {
                cursor.moveToFirst();
                ContentValues values = new ContentValues();
                while (!cursor.isAfterLast()) {
                    values.clear();
                    // if the mod number is less than the latest modified, modify the current cursor
                    // if Uri is null due to a vehicle deletion, all need to be modified anyway
                    if (cursor.getInt(cursor.getColumnIndex
                            (VehiclesTable.COL_VEHICLE_MODIFIED_ORDER)) <= mLatestModNum ||
                            uri == null) {
                        // if latest to be modified, set mod order field to 1
                        if (cursor.getInt(cursor.getColumnIndex
                                (VehiclesTable.COL_VEHICLE_ID)) == mLatestId) {
                            values.put(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, 1);
                        } else { // put in a mNewModNum and increment mNewModNum by 1
                            values.put(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, mNewModNum);
                            mNewModNum++;
                        }
                        updateVehicle(values, cursor.getInt
                                (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ID)));
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }
    private void updateVehicle(ContentValues values, int vehId) {
        mContext.getContentResolver().update(VehicleLogContentProvider.VEHICLE_CONTENT_URI,
                values, VehiclesTable.COL_VEHICLE_ID+"="+vehId,null);
    }
}
