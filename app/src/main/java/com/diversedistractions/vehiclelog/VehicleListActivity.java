package com.diversedistractions.vehiclelog;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diversedistractions.vehiclelog.database.DataSource;
import com.diversedistractions.vehiclelog.database.VehicleLogContentProvider;
import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.dummy.DummyContentProvider;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.JSONHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This is the main activity.
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VehicleListActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private boolean permissionGranted;

    //TODO: Remove for final product
    List<VehicleItem> vehicleItemList = DummyContentProvider.vehicleItemList;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        // Call to insert a new vehicle into the database
//        insertVehicle(vehicleItem);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = settings.getString(getString(R.string.ms_key_vehicle_sortby), null);

        cursor = getContentResolver().query(VehicleLogContentProvider.VEHICLE_CONTENT_URI,
                VehiclesTable.ALL_VEHICLE_COLUMNS, null, null, null, null);

        //TODO: I think I want to move this
        checkPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.vehicle_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.vehicle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void insertVehicle(VehicleItem vehicleItem) {
        ContentValues values = new ContentValues();
        values.put(VehiclesTable.COL_VEHICLE_TYPE, vehicleItem.getVehicleType());
        values.put(VehiclesTable.COL_VEHICLE_MAKE, vehicleItem.getVehicleMake());
        values.put(VehiclesTable.COL_VEHICLE_MODEL, vehicleItem.getVehicleModel());
        values.put(VehiclesTable.COL_VEHICLE_YEAR, vehicleItem.getVehicleYear());
        values.put(VehiclesTable.COL_VEHICLE_VIN, vehicleItem.getVehicleVin());
        values.put(VehiclesTable.COL_VEHICLE_LP, vehicleItem.getVehicleLp());
        values.put(VehiclesTable.COL_VEHICLE_REN_DATE, vehicleItem.getVehicleLpRenewalDate());
        values.put(VehiclesTable.COL_VEHICLE_IMAGE, vehicleItem.getVehicleImage());
        values.put(VehiclesTable.COL_VEHICLE_TD_EFF, vehicleItem.getVehicleTdEfficiency());
        values.put(VehiclesTable.COL_VEHICLE_NOTE, vehicleItem.getVehicleNotes());
        values.put(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, vehicleItem.getVehicleModOrder());
        Uri vehicleUri = getContentResolver()
                .insert(VehicleLogContentProvider.VEHICLE_CONTENT_URI, values);
        Log.d("VehicleListActivity", "Inserted vehicle " + vehicleUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, MainSettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_export:
                boolean result = JSONHelper.exportToJSON(this,vehicleItemList);
                if (result) {
                    Toast.makeText(this, "Data exported", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_import:
                List<VehicleItem> vehicleItems = JSONHelper.importFromJSON(this);
                if (vehicleItems != null) {
                    for (VehicleItem vehicleItem: vehicleItems) {
                        //TODO: code to verify data format and import into the database
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new VehicleItemAdapter(this, cursor));
    }

    public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>{

        // Because RecyclerView.Adapter in its current form doesn't natively
        // support cursors, we wrap a CursorAdapter that will do all the job
        // for us.
        CursorAdapter mCursorAdapter;

        private Context mContext;

        public VehicleItemAdapter(Context context, Cursor cursor) {

            mContext = context;

            mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    return inflater.inflate(R.layout.vehicle_list_item, parent, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    String year = Integer.toString(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
                    String make = cursor.getString(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE));
                    String model = cursor.getString(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL));
                    String imageFile = cursor.getString(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE));

                    ((TextView) view.findViewById(R.id.vehicleYear)).setText(year);
                    ((TextView) view.findViewById(R.id.makeText)).setText(make);
                    ((TextView) view.findViewById(R.id.modelText)).setText(model);
                    try {
                        InputStream inputStream = context.getAssets().open(imageFile);
                        Drawable drawable = Drawable.createFromStream(inputStream, null);
                        ((ImageView) view.findViewById(R.id.vehicleImage)).setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        public void reQuery(Cursor c) {
            if (mCursorAdapter != null) {
                mCursorAdapter.changeCursor(c);
                mCursorAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public int getItemCount() {
            return mCursorAdapter.getCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Passing the inflater job to the cursor adapter
            View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Passing the binding operation to cursor loader
            mCursorAdapter.getCursor().moveToPosition(position);
            mCursorAdapter.bindView(holder.view, mContext, mCursorAdapter.getCursor());

            /*
             *When an item in the vehicle list is clicked on:
             *If in two pane mode, i.e. tablet in landscape mode, pass the vehicle as a parcelable
             *into a fragment for the second pane. If in single pane mode, i.e. phone in either
             *orientation or tablet in portrait mode, pass the vehicle as a parcelable into an
             * intent to launch the new activity.
             */
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Toast.makeText(mContext, "You clicked on a vehicle", Toast.LENGTH_SHORT).show();
//                        Bundle arguments = new Bundle();
//                        arguments.putParcelable(VehicleDetailFragment.ARG_ITEM, vehicleItem);
//                        VehicleDetailFragment fragment = new VehicleDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.vehicle_detail_container, fragment)
//                                .commit();
                    } else {
                        Toast.makeText(mContext, "You clicked on a vehicle", Toast.LENGTH_SHORT).show();
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, VehicleDetailActivity.class);
//                        intent.putExtra(VehicleDetailFragment.ARG_ITEM, vehicleItem);
//
//                        context.startActivity(intent);
                    }
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView.findViewById(R.id.vehicle_list_item);
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "Import/Export only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission to import or export data!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
