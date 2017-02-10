package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Service Entries table.
 */

public class ServiceEntriesTable {

    /*
     * Name of the table that contains the list of service entries for each vehicle
     */
    public static final String SERVICE_ENTRY_DATABASE_TABLE = "service_entries";

    /*
     * Service entries table column definitions
     */
    // Column name for the primary key
    public static final String COL_SERVICE_ENTRY_ID = "_id";
    // Column name for the foreign key referencing the vehicle this entry belongs to
    public static final String COL_VEH_ID = "vId";
    // Column name for the date of record for this entry
    public static final String COL_SERVICE_ENTRY_DATE = "date";
    // Column name for the odometer or hour meter reading for this entry
    public static final String COL_SERVICE_ENTRY_METER = "meter";
    // Column name for the flag (1 or 0) denoting that the oil was changed for this entry
    public static final String COL_SERVICE_ENTRY_OIL = "oil";
    // Column name for the flag (1 or 0) denoting that the tank was not filled for this entry
    public static final String COL_SERVICE_ENTRY_OIL_FILTER = "oilFilter";
    // Column name for the flag (1 or 0) denoting that the oil filter was changed for this entry
    public static final String COL_SERVICE_ENTRY_AIR_FILTER = "airFilter";
    // Column name for a service note for this entry
    public static final String COL_SERVICE_ENTRY_NOTE = "serviceNote";

    // Convenient string that contains all of the table columns
    public static final String[] ALL_SERVICE_ENTRY_COLUMNS = {COL_SERVICE_ENTRY_ID, COL_VEH_ID,
            COL_SERVICE_ENTRY_DATE, COL_SERVICE_ENTRY_METER, COL_SERVICE_ENTRY_OIL,
            COL_SERVICE_ENTRY_OIL_FILTER, COL_SERVICE_ENTRY_AIR_FILTER,
            COL_SERVICE_ENTRY_NOTE};

    /*
     * Database table creation SQL statement for each vehicle's service entries
     */
    public static final String SERVICE_ENTRY_TABLE_CREATE =
            "CREATE TABLE " + SERVICE_ENTRY_DATABASE_TABLE + "(" +
                    COL_SERVICE_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_VEH_ID + " INTEGER," +
                    COL_SERVICE_ENTRY_DATE + " INTEGER," +
                    COL_SERVICE_ENTRY_METER + " REAL," +
                    COL_SERVICE_ENTRY_OIL + " INTEGER," +
                    COL_SERVICE_ENTRY_OIL_FILTER + " INTEGER," +
                    COL_SERVICE_ENTRY_AIR_FILTER + " INTEGER," +
                    COL_SERVICE_ENTRY_NOTE + " TEXT," +
                    " FOREIGN KEY(" + COL_VEH_ID +")" +
                    " REFERENCES " + VehiclesTable.VEHICLE_DATABASE_TABLE +
                    "(" + VehiclesTable.COL_VEHICLE_ID + "));";

    public static final String SERVICE_ENTRY_TABLE_DELETE =
            "DROP TABLE " + SERVICE_ENTRY_DATABASE_TABLE;
}
