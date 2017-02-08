package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Vehicles table.
 */

public class VehiclesTable {

    /*
     * Name of the table that contains the list of vehicles
     */
    public static final String VEHICLE_DATABASE_TABLE = "vehicles";

    /*
     * Vehicle table column definitions
     */
    // Column name for the primary key
    public static final String COL_VEHICLE_ID = "vehicle_id";
    // Column name for the type of vehicle
    public static final String COL_VEHICLE_TYPE = "vehicle_type";
    // Column name for the vehicle's make
    public static final String COL_VEHICLE_MAKE = "vehicle_make";
    // Column name for the vehicle's model
    public static final String COL_VEHICLE_MODEL = "vehicle_model";
    // Column name for the vehicle's model year
    public static final String COL_VEHICLE_YEAR = "vehicle_year";
    // Column name for the vehicle's identification number
    public static final String COL_VEHICLE_VIN = "vin";
    // Column name for the vehicle's license plate number
    public static final String COL_VEHICLE_LP = "plate";
    // Column name for the vehicle's license plate renewal date
    public static final String COL_VEHICLE_REN_DATE = "ren_date";
    // Column name for the vehicle's icon or photo
    public static final String COL_VEHICLE_IMAGE = "veh_image";
    // Column name for the vehicle's note
    public static final String COL_VEHICLE_NOTE = "vehicle_note";
    // Column name for the vehicle's year-to-date fuel efficiency
    public static final String COL_VEHICLE_TD_EFF = "td_efficiency";
    // Column name for tracking order of last modified vehicles
    public static final String COL_VEHICLE_MODIFIED_ORDER = "vehicle_mod_order";

    // Convenient string that contains all of the table columns
    public static final String[] ALL_VEHICLE_COLUMNS = {COL_VEHICLE_ID, COL_VEHICLE_TYPE,
            COL_VEHICLE_MAKE, COL_VEHICLE_MODEL, COL_VEHICLE_YEAR, COL_VEHICLE_VIN, COL_VEHICLE_LP,
            COL_VEHICLE_REN_DATE, COL_VEHICLE_IMAGE, COL_VEHICLE_NOTE, COL_VEHICLE_TD_EFF,
            COL_VEHICLE_MODIFIED_ORDER};

    /*
     * Constants for vehicle types
     */
    public static final String VEHICLE_TYPE_CAR = "car";
    public static final String VEHICLE_TYPE_MOTORCYCLE = "motorcycle";
    public static final String VEHICLE_TYPE_TRUCK = "truck";
    public static final String VEHICLE_TYPE_UTILITY = "utility";

    /*
     * Database table creation SQL statement for the vehicles list
     */
    //TODO: Decide between integer and string for ID
    public static final String VEHICLES_TABLE_CREATE =
            "create table " + VEHICLE_DATABASE_TABLE + "(" +
                    COL_VEHICLE_ID + " text primary key," +
                    COL_VEHICLE_TYPE + " text," +
                    COL_VEHICLE_MAKE + " text," +
                    COL_VEHICLE_MODEL + " text," +
                    COL_VEHICLE_YEAR + " integer," +
                    COL_VEHICLE_VIN + " text," +
                    COL_VEHICLE_LP + " text," +
                    COL_VEHICLE_REN_DATE + " integer," +
                    COL_VEHICLE_IMAGE + " text," +
                    COL_VEHICLE_NOTE + " text," +
                    COL_VEHICLE_TD_EFF + " text," +
                    COL_VEHICLE_MODIFIED_ORDER + " integer" + ");";

    public static final String VEHICLES_TABLE_DELETE = "DROP TABLE " + VEHICLE_DATABASE_TABLE;
}
