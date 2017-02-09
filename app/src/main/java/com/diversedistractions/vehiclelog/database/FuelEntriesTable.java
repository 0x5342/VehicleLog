package com.diversedistractions.vehiclelog.database;

/**
 * Defines column names and creation strings for the Fuel Entries table.
 */

public class FuelEntriesTable {

    /*
     * Name of the table that contains the list of fuel entries for each vehicle
     */
    public static final String FUEL_ENTRY_DATABASE_TABLE = "fuel_entries";

    /*
     * Fuel entries table column definitions
     */
    // Column name for the primary key
    public static final String COL_FUEL_ENTRY_ID = "fuel_entry_id";
    // Column name for the foreign key referencing the vehicle this entry belongs to
    public static final String COL_VEH_ID = "v_id";
    // Column name for the date of record for this entry
    public static final String COL_FUEL_ENTRY_DATE = "date";
    // Column name for the odometer reading for this entry
    public static final String COL_FUEL_ENTRY_ODOMETER = "odometer";
    // Column name for the number of gallons filled for this entry
    public static final String COL_FUEL_ENTRY_QUANTITY = "fuel_quantity";
    // Column name for the number of gallons filled for this entry
    public static final String COL_FUEL_ENTRY_COST = "fuel_cost";
    // Column name for the flag (1 or 0) denoting that the tank was not filled for this entry
    public static final String COL_FUEL_ENTRY_PARTIAL = "partial_tank";
    // Column name for the miles per gallon of the current full tank entry
    public static final String COL_FUEL_ENTRY_EFF = "entry_efficiency";

    // Convenient string that contains all of the table columns
    public static final String[] ALL_FUEL_ENTRY_COLUMNS = {COL_FUEL_ENTRY_ID, COL_VEH_ID,
            COL_FUEL_ENTRY_DATE, COL_FUEL_ENTRY_ODOMETER, COL_FUEL_ENTRY_QUANTITY,
            COL_FUEL_ENTRY_COST, COL_FUEL_ENTRY_PARTIAL, COL_FUEL_ENTRY_EFF};

    /*
     * Database table creation SQL statement for each vehicle's fuel entries
     */
    public static final String FUEL_ENTRY_TABLE_CREATE =
            "create table " + FUEL_ENTRY_DATABASE_TABLE + "(" +
                    COL_FUEL_ENTRY_ID + " text primary key," +
                    COL_VEH_ID + " text," +
                    COL_FUEL_ENTRY_DATE + " integer," +
                    COL_FUEL_ENTRY_ODOMETER + " real," +
                    COL_FUEL_ENTRY_QUANTITY + " real," +
                    COL_FUEL_ENTRY_COST + " real," +
                    COL_FUEL_ENTRY_PARTIAL + " integer," +
                    COL_FUEL_ENTRY_EFF + " real," +
                    " FOREIGN KEY(" + COL_VEH_ID +")" +
                    " REFERENCES " + VehiclesTable.VEHICLE_DATABASE_TABLE +
                    "(" + VehiclesTable.COL_VEHICLE_ID + "));";

    public static final String FUEL_ENTRY_TABLE_DELETE = "DROP TABLE " + FUEL_ENTRY_DATABASE_TABLE;
}
