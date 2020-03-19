package com.diversedistractions.vehiclelog.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.Nullable;

public class VehicleLogContentProvider extends ContentProvider{

    public static final String AUTHORITY =
            "com.diversedistractions.vehiclelog.vehiclelogcontentprovider";
    // Path part for the Vehicles URI
    public static final String BASE_PATH_VEHICLES = "vehicles";
    // The content:// style URL for this table
    public static final Uri VEHICLE_CONTENT_URI =
        Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_VEHICLES);

    // Constants to identify the requested operation
    public static final int VEHICLES = 1;
    public static final int VEHICLES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH_VEHICLES, VEHICLES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_VEHICLES + "/#", VEHICLES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        database = dbOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if (uriMatcher.match(uri) == VEHICLES_ID) {
            selection = VehiclesTable.COL_VEHICLE_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(VehiclesTable.VEHICLE_DATABASE_TABLE,
                VehiclesTable.ALL_VEHICLE_COLUMNS, selection, null, null, null,
                sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(VehiclesTable.VEHICLE_DATABASE_TABLE, null, values);
        return Uri.parse(VEHICLE_CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(VehiclesTable.VEHICLE_DATABASE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(VehiclesTable.VEHICLE_DATABASE_TABLE, values,
                selection, selectionArgs);
    }
}
