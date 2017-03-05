package com.diversedistractions.vehiclelog;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diversedistractions.vehiclelog.database.VehicleLogContentProvider;
import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is the main activity.
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VehicleListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private boolean permissionGranted;

    private CursorAdapter mCursorAdapter;
    private String sortByField;
    DateConversionHelper dateConversionHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        dateConversionHelper = new DateConversionHelper();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        // This will retrieve the preferred sort order and set the sort by field accordingly
        String sortBy = settings.getString(getString(R.string.preferences_key_vehicle_sortby), null);
        if (sortBy != null) {
            if (sortBy.equals(getString(R.string.most_recently_used))) {
                sortByField = VehiclesTable.COL_VEHICLE_MODIFIED_ORDER;
            } else if (sortBy.equals(getString(R.string.year))) {
                sortByField = VehiclesTable.COL_VEHICLE_YEAR;
            } else if (sortBy.equals(getString(R.string.make))) {
                sortByField = VehiclesTable.COL_VEHICLE_MAKE;
            } else if (sortBy.equals(getString(R.string.model))) {
                sortByField = VehiclesTable.COL_VEHICLE_MODEL;
            }
        }

        //TODO: I think I want to move this
        checkPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDelete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(VehicleDetailFragment.DETAIL_MODE,
                            VehicleDetailFragment.DETAIL_IN_CREATE_MODE);
                    VehicleDetailFragment fragment = new VehicleDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.vehicle_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VehicleDetailActivity.class);
                    intent.putExtra(VehicleDetailFragment.DETAIL_MODE,
                            VehicleDetailFragment.DETAIL_IN_CREATE_MODE);

                    context.startActivity(intent);
                }
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

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Create the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When an item is selected from the menu, act accordingly.
     * //:TODO describe each action
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PreferencesActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_export:
                //TODO: Correct this code to use the content provider
//                boolean result = JSONHelper.exportToJSON(this,vehicleItemList);
//                if (result) {
//                    Toast.makeText(this, "Data exported", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
//                }
                return true;
            case R.id.action_import:
                //TODO: Correct this code to use the content provider
//                List<VehicleItem> vehicleItems = JSONHelper.importFromJSON(this);
//                if (vehicleItems != null) {
//                    for (VehicleItem vehicleItem: vehicleItems) {
//                        //TODO: code to verify data format and import into the database
//                    }
//                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * //TODO: description
     * @param recyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new VehicleItemAdapter(this, null));
    }

    /**
     * //TODO: description
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, VehicleLogContentProvider.VEHICLE_CONTENT_URI,
                null, null, null, sortByField );
    }

    /**
     * //TODO: description
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    /**
     * //TODO: description
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    /**
     * //TODO: description
     */
    public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>{

        // Because RecyclerView.Adapter in its current form doesn't natively
        // support cursors, wrap a CursorAdapter that will do it.
        private Context mContext;

        /**
         * //TODO: description
         * @param context
         * @param cursor
         */
        public VehicleItemAdapter(Context context, Cursor cursor) {

            mContext = context;

            mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {

                /**
                 * //TODO: description
                 * @param context
                 * @param cursor
                 * @param parent
                 * @return
                 */
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    return inflater.inflate(R.layout.vehicle_list_item, parent, false);
                }

                /**
                 * //TODO: description
                 * @param view
                 * @param context
                 * @param cursor
                 */
                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    // Set a tag in order to get the ID of the item clicked on
                    view.setTag(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ID)));
                    String year = dateConversionHelper.getYearAsString
                            (cursor.getLong(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
                    String make = cursor.getString
                            (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE));
                    String model = cursor.getString
                            (cursor.getColumnIndex
                                    (VehiclesTable.COL_VEHICLE_MODEL));
                    String imageFile = cursor.getString
                            (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE));

                    ((TextView) view.findViewById(R.id.vehicleYear)).setText(year);
                    ((TextView) view.findViewById(R.id.makeText)).setText(make);
                    ((TextView) view.findViewById(R.id.modelText)).setText(model);
                    // If there is a path to a vehicle image, load it; if not load the no image icon
                    if (null != imageFile) {
                        try {
                            InputStream inputStream = context.getAssets().open(imageFile);
                            Drawable drawable = Drawable.createFromStream(inputStream, null);
                            ((ImageView) view.findViewById
                                    (R.id.vehicleImage)).setImageDrawable(drawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            InputStream inputStream = context.getAssets()
                                    .open(VehiclesTable.VEHICLE_NO_ICON_FOLDER +
                                            "vi_no_vehicle_image.png");
                            Drawable drawable = Drawable.createFromStream(inputStream, null);
                            ((ImageView) view.findViewById
                                    (R.id.vehicleImage)).setImageDrawable(drawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        /**
         * //TODO: description
         * @param c
         */
        public void reQuery(Cursor c) {
            if (mCursorAdapter != null) {
                mCursorAdapter.changeCursor(c);
                mCursorAdapter.notifyDataSetChanged();
            }
        }

        /**
         * //TODO: description
         * @return
         */
        @Override
        public int getItemCount() {
            return mCursorAdapter.getCount();
        }

        /**
         * //TODO: description
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Passing the inflater job to the cursor adapter
            View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
        }

        /**
         * //TODO: description
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
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

                    // Get the item id from the view's tag
                    int vehicleSelected = (Integer) v.getTag();
                    // Set the URI to the selected vehicle's id
                    Uri vehicleUri = Uri.parse(VehicleLogContentProvider.
                            VEHICLE_CONTENT_URI + "/" + vehicleSelected);

                    if (mTwoPane) {
                        //TODO: need to rework two pane mode
//                        Bundle arguments = new Bundle();
//                        arguments.putParcelable(VehicleDetailFragment.ARG_ITEM_URI, vehicleUri);
//                        arguments.putInt(VehicleDetailFragment.DETAIL_MODE,
//                                VehicleDetailFragment.DETAIL_IN_VIEW_MODE);
//                        VehicleDetailFragment fragment = new VehicleDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.vehicle_detail_container, fragment)
//                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, VehicleDetailActivity.class);
                        intent.putExtra(VehicleDetailFragment.ARG_ITEM_URI, vehicleUri);
                        intent.putExtra(VehicleDetailFragment.DETAIL_MODE,
                                VehicleDetailFragment.DETAIL_IN_VIEW_MODE);

                        context.startActivity(intent);
                    }
                }
            });

            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    String vYear = "";
                    String vMake = "";
                    String vModel = "";

                    // Get the item id from the view's tag
                    int vehicleSelected = (Integer) v.getTag();
                    // Set the URI to the selected vehicle's id
                    Uri vehicleUri = Uri.parse(VehicleLogContentProvider.
                            VEHICLE_CONTENT_URI + "/" + vehicleSelected);

                    Cursor cursor = mContext.getContentResolver().query
                            (vehicleUri, null, null, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        vYear = dateConversionHelper.getYearAsString(cursor
                                .getLong(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
                        vMake = cursor.getString
                                (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE));
                        vModel = cursor.getString
                                (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL));
                    }
                    //TODO: show a dialog fragment that asks user to verify deletion
                    return false;
                }
            });
        }

        /**
         * //TODO: description
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView.findViewById(R.id.vehicle_list_item);
            }
        }
    }

    /**
     * Checks if external storage is available for read and write
     * @return
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if external storage is available to at least read
     * @return
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    /**
     * Initiate request for permissions.
     * @return
     */
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

   /**
     * Handle permissions result
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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
