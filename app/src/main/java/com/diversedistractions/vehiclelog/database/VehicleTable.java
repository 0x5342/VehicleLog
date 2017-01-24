package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Vehicles table.
 */

public class VehicleTable {

    /*
     * Name of the table that contains the list of vehicles
     */
    public static final String VEHICLE_DATABASE_TABLE = "vehicles";

    /*
     * Vehicle table column definitions
     */
    // Column name for the primary key
    public static final String COL_VEHICLE_ROW_ID = "_id";
    // Column name for the vehicle's make
    public static final String COL_VEHICLE_MAKE = "make";
    // Column name for the vehicle's model
    public static final String COL_VEHICLE_MODEL = "model";
    // Column name for the vehicle's model year
    public static final String COL_VEHICLE_YEAR = "year";
    // Column name for the vehicle's identification number
    public static final String COL_VEHICLE_VIN = "vin";
    // Column name for the vehicle's license plate number
    public static final String COL_VEHICLE_LP = "plate";
    // Column name for the vehicle's license plate renewal date
    public static final String COL_VEHICLE_REN_DATE = "ren_date";
    // Column name for the vehicle's icon or photo
    public static final String COL_VEHICLE_IMAGE = "veh_image";
    // Column name for the vehicle's year-to-date miles per gallon
    public static final String COL_VEHICLE_YTD_MPG = "ytd_mpg";
    // Column name for the vehicle's note
    public static final String COL_VEHICLE_NOTE = "note";

    // Convenient string that contains all of the table columns
    public static final String[] ALL_VEHICLE_COLUMNS = {COL_VEHICLE_ROW_ID, COL_VEHICLE_MAKE,
            COL_VEHICLE_MODEL, COL_VEHICLE_YEAR, COL_VEHICLE_VIN, COL_VEHICLE_LP,
            COL_VEHICLE_REN_DATE, COL_VEHICLE_IMAGE, COL_VEHICLE_YTD_MPG, COL_VEHICLE_NOTE};

    /*
     * Database table creation SQL statement for the vehicles list
     */
    private static final String VEHICLES_TABLE_CREATE =
            "create table " + VEHICLE_DATABASE_TABLE + "(" +
                    COL_VEHICLE_ROW_ID + " integer primary key autoincrement," +
                    COL_VEHICLE_MAKE + " text not null," +
                    COL_VEHICLE_MODEL + " text not null," +
                    COL_VEHICLE_YEAR + " integer not null," +
                    COL_VEHICLE_VIN + " text," +
                    COL_VEHICLE_LP + " text," +
                    COL_VEHICLE_REN_DATE + " integer," +
                    COL_VEHICLE_IMAGE + " text," +
                    COL_VEHICLE_YTD_MPG + " text," +
                    COL_VEHICLE_NOTE + " text" + ");";

    public static final String VEHICLES_SQL_DELETE = "DROP TABLE " + VEHICLE_DATABASE_TABLE;
}
