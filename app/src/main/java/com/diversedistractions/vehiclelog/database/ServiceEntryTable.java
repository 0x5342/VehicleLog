package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Service Entries table.
 */

public class ServiceEntryTable {

    /*
     * Name of the table that contains the list of service entries for each vehicle
     */
    public static final String SERVICE_ENTRY_DATABASE_TABLE = "service_entries";

    /*
     * Service entries table column definitions
     */
    // Column name for the primary key
    public static final String COL_SERVICE_ENTRY_ROW_ID = "_id";
    // Column name for the foreign key referencing the vehicle this entry belongs to
    public static final String COL_VEH_ROW_ID = "v_id";
    // Column name for the date of record for this entry
    public static final String COL_SERVICE_ENTRY_DATE = "date";
    // Column name for the odometer reading for this entry
    public static final String COL_SERVICE_ENTRY_ODOMETER = "odometer";
    // Column name for the flag (1 or 0) denoting that the oil was changed for this entry
    public static final String COL_SERVICE_ENTRY_OIL = "oil";
    // Column name for the flag (1 or 0) denoting that the tank was not filled for this entry
    public static final String COL_SERVICE_ENTRY_OIL_FILTER = "oilFilter";
    // Column name for the flag (1 or 0) denoting that the oil filter was changed for this entry
    public static final String COL_SERVICE_ENTRY_AIR_FILTER = "airFilter";
    // Column name for the flag (1 or 0) denoting the cabin air filter was changed for this entry
    public static final String COL_SERVICE_ENTRY_CABIN_AIR_FILTER = "cabinAirFilter";
    // Column name for the flag (1 or 0) denoting that the tires were rotated for this entry
    public static final String COL_SERVICE_ENTRY_ROTATED_TIRES = "rotatedTires";
    // Column name for a service note for this entry
    public static final String COL_SERVICE_ENTRY_NOTE = "note";

    /*
     * Database table creation SQL statement for each vehicle's service entries
     */
    private static final String SERVICE_ENTRY_TABLE_CREATE =
            "create table " + SERVICE_ENTRY_DATABASE_TABLE + "(" +
                    COL_SERVICE_ENTRY_ROW_ID + " integer primary key autoincrement," +
                    COL_VEH_ROW_ID + " integer not null," +
                    COL_SERVICE_ENTRY_DATE + " integer not null," +
                    COL_SERVICE_ENTRY_ODOMETER + " real not null," +
                    COL_SERVICE_ENTRY_OIL + " integer," +
                    COL_SERVICE_ENTRY_OIL_FILTER + " integer," +
                    COL_SERVICE_ENTRY_AIR_FILTER + " integer," +
                    COL_SERVICE_ENTRY_CABIN_AIR_FILTER + " integer," +
                    COL_SERVICE_ENTRY_ROTATED_TIRES + " integer," +
                    COL_SERVICE_ENTRY_NOTE + " text," +
                    " FOREIGN KEY(" + COL_VEH_ROW_ID +")" +
                    " REFERENCES " + VehicleTable.VEHICLE_DATABASE_TABLE + "(_id));";

    public static final String SERVICE_ENTRY_SQL_DELETE =
            "DROP TABLE " + SERVICE_ENTRY_DATABASE_TABLE;
}
