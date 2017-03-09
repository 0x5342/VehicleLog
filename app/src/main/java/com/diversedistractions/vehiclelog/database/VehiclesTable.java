package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Vehicles table.
 */

public class VehiclesTable {

    // Name of the table that contains the list of vehicles
    public static final String VEHICLE_DATABASE_TABLE = "vehicles";

    // Folder in assets where icons reside
    public static final String VEHICLE_ICONS_FOLDER = "icons/";
    // Folder in assets where no vehicle image icon resides
    public static final String VEHICLE_NO_ICON = "icons/vi_no_vehicle_image.png";

    /*
     * Vehicle table column definitions
     */
    // Column name for the primary key
    public static final String COL_VEHICLE_ID = "_id";
    // Column name for the type of vehicle
    public static final String COL_VEHICLE_TYPE = "vehicleType";
    // Column name for the vehicle's make
    public static final String COL_VEHICLE_MAKE = "vehicleMake";
    // Column name for the vehicle's model
    public static final String COL_VEHICLE_MODEL = "vehicleModel";
    // Column name for the vehicle's model year
    public static final String COL_VEHICLE_YEAR = "vehicleYear";
    // Column name for the vehicle's identification number
    public static final String COL_VEHICLE_VIN = "vin";
    // Column name for the vehicle's license plate number
    public static final String COL_VEHICLE_LP = "plate";
    // Column name for the vehicle's license plate renewal date
    public static final String COL_VEHICLE_REN_DATE = "renDate";
    // Column name for the vehicle's icon or photo
    public static final String COL_VEHICLE_IMAGE = "vehImage";
    // Column name for the vehicle's note
    public static final String COL_VEHICLE_NOTE = "vehicleNote";
    // Column name for the vehicle's year-to-date fuel efficiency
    public static final String COL_VEHICLE_TD_EFF = "tdEfficiency";
    // Column name for tracking order of last modified vehicles
    public static final String COL_VEHICLE_MODIFIED_ORDER = "vehicleModOrder";

    // String that contains all of the table columns
    public static final String[] ALL_VEHICLE_COLUMNS = {COL_VEHICLE_ID, COL_VEHICLE_TYPE,
            COL_VEHICLE_MAKE, COL_VEHICLE_MODEL, COL_VEHICLE_YEAR, COL_VEHICLE_VIN, COL_VEHICLE_LP,
            COL_VEHICLE_REN_DATE, COL_VEHICLE_IMAGE, COL_VEHICLE_NOTE, COL_VEHICLE_TD_EFF,
            COL_VEHICLE_MODIFIED_ORDER};

    /*
     * Database table creation SQL statement for the vehicles list
     */
    public static final String VEHICLES_TABLE_CREATE =
            "CREATE TABLE " + VEHICLE_DATABASE_TABLE + "(" +
                    COL_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_VEHICLE_TYPE + " INTEGER," +
                    COL_VEHICLE_MAKE + " TEXT," +
                    COL_VEHICLE_MODEL + " TEXT," +
                    COL_VEHICLE_YEAR + " INTEGER," +
                    COL_VEHICLE_VIN + " TEXT," +
                    COL_VEHICLE_LP + " TEXT," +
                    COL_VEHICLE_REN_DATE + " INTEGER," +
                    COL_VEHICLE_IMAGE + " TEXT," +
                    COL_VEHICLE_NOTE + " TEXT," +
                    COL_VEHICLE_TD_EFF + " TEXT," +
                    COL_VEHICLE_MODIFIED_ORDER + " INTEGER" + ");";

    public static final String VEHICLES_TABLE_DELETE = "DROP TABLE " + VEHICLE_DATABASE_TABLE;
}
