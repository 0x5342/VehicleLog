package com.diversedistractions.vehiclelog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single Vehicle detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link VehicleListActivity}.
 */
public class VehicleDetailActivity extends AppCompatActivity {

    private Uri mVehicleUri;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mVehicleUri = intent.getParcelableExtra(VehicleDetailFragment.ARG_ITEM_URI);
        mMode = intent.getIntExtra(VehicleDetailFragment.DETAIL_MODE, 0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (mMode == VehicleDetailFragment.DETAIL_IN_VIEW_MODE) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Replace the VehicleDetailFragment in view mode with it in edit mode.
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(VehicleDetailFragment.ARG_ITEM_URI, mVehicleUri);
                    arguments.putInt(VehicleDetailFragment.DETAIL_MODE,
                            VehicleDetailFragment.DETAIL_IN_EDIT_MODE);
                    VehicleDetailFragment fragment = new VehicleDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.vehicle_detail_container, fragment)
                            .commit();
                }
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            // ARG_ITEM_URI will be null when creating a new vehicle or will contain the uri of a
            // particular vehicle when viewing or editing.
            // DETAIL_MODE will contain either DETAIL_IN_VIEW_MODE, DETAIL_IN_EDIT_MODE, or
            // default to DETAIL_IN_CREATE_MODE to tell the fragment which layout to use and
            // whether the fields start blank or filled in edit/create mode.
            Bundle arguments = new Bundle();
            arguments.putParcelable(VehicleDetailFragment.ARG_ITEM_URI,
                    getIntent().getParcelableExtra(VehicleDetailFragment.ARG_ITEM_URI));
            arguments.putInt(VehicleDetailFragment.DETAIL_MODE,
                    getIntent().getIntExtra(VehicleDetailFragment.DETAIL_MODE,
                            VehicleDetailFragment.DETAIL_IN_CREATE_MODE));
            VehicleDetailFragment fragment = new VehicleDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.vehicle_detail_container, fragment)
                    .commit();
        }
    }

    /**
     * When an option is selected from the menu, act on it accordingly.
     * //TODO: explain what each item that can be selected will do
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, VehicleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
