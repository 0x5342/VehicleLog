package com.diversedistractions.vehiclelog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.diversedistractions.vehiclelog.dummy.DummyContentProvider;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.JSONHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VehicleListActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private static final String TAG = "vehicleLogImport";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    List<VehicleItem> vehicleItemList = DummyContentProvider.vehicleItemList;
    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

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
                        Log.i(TAG, "onOptionsItemSelected: " + vehicleItem.getVehicleMake());
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new VehicleItemAdapter(this, vehicleItemList));
    }

    public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>{

        private final List<VehicleItem> mItems;
        private Context mContext;

        public VehicleItemAdapter(Context context, List<VehicleItem> items) {
            mItems = items;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View vehicleItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vehicle_list_item, parent, false);
            return new ViewHolder(vehicleItemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final VehicleItem vehicleItem = mItems.get(position);

            try {
                holder.vYear.setText(Integer.toString(vehicleItem.getVehicleYear()));
                holder.vMake.setText(vehicleItem.getVehicleMake());
                holder.vModel.setText(vehicleItem.getVehicleModel());
                String vehicleImageFile = vehicleItem.getVehicleImage();
                InputStream inputStream = mContext.getAssets().open(vehicleImageFile);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                holder.vImage.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             *When an item in the vehicle list is clicked on:
             *If in two pane mode, i.e. tablet in landscape mode, pass the vehicle as a parcelable
             *into a fragment for the second pane. If in single pane mode, i.e. phone in either
             *orientation or tablet in portrait mode, pass the vehicle as a parcelable into an
             * intent to launch the new activity.
             */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(VehicleDetailFragment.ARG_ITEM, vehicleItem);
                        VehicleDetailFragment fragment = new VehicleDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.vehicle_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, VehicleDetailActivity.class);
                        intent.putExtra(VehicleDetailFragment.ARG_ITEM, vehicleItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView vYear;
            public TextView vMake;
            public TextView vModel;
            public ImageView vImage;
            public View mView;
//            public DummyContentProvider.DummyItem mItem;

            public ViewHolder(View vehicleItemView) {
                super(vehicleItemView);
                vYear = (TextView) vehicleItemView.findViewById(R.id.vehicleYear);
                vMake = (TextView) vehicleItemView.findViewById(R.id.makeText);
                vModel = (TextView) vehicleItemView.findViewById(R.id.modelText);
                vImage = (ImageView) vehicleItemView.findViewById(R.id.vehicleImage);
                mView = vehicleItemView;
            }

//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
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
    //TODO: Change this to note that import / export will not work without this permission
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
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
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
