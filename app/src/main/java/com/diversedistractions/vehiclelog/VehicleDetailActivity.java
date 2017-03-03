package com.diversedistractions.vehiclelog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.CustomDatePickerDialogFragment;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;
import com.diversedistractions.vehiclelog.utilities.IconFromAssetsFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * An activity representing a single Vehicle detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link VehicleListActivity}.
 */
public class VehicleDetailActivity extends AppCompatActivity
        implements CustomDatePickerDialogFragment.CustomDatePickerListener
        ,VehicleImageChoiceFragment.OnVehicleImageChoiceFragmentInteractionListener
,IconFromAssetsFragment.OnListFragmentInteractionListener{

    public static final int CHOOSE_APP_ICON = 1;
    public static final int CHOOSE_IMAGE_ON_DEVICE = 2;
    public static final int TAKE_PHOTO = 3;
    private Uri mVehicleUri;
    private int mMode;
    DateConversionHelper dateConversionHelper;

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
                    mMode = VehicleDetailFragment.DETAIL_IN_EDIT_MODE;
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

    /**
     *
     * @param vehicleItem
     * @param dateField
     */
    @Override
    public void onDatePickComplete(VehicleItem vehicleItem, String dateField) {

        // Get a reference to the vehicle detail fragment in order to update the values
        VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.vehicle_detail_container);

        // Only update the appropriate date field
        if (dateField.equalsIgnoreCase(VehiclesTable.COL_VEHICLE_YEAR)) {
            long vYear = vehicleItem.getVehicleYear();
            SimpleDateFormat ySimpleDateFormat = new SimpleDateFormat("yyyy", Locale.US);
            String year = ySimpleDateFormat.format(vYear);
            vdf_obj.updateVehicleYear(vYear, year);
        } else if (dateField.equalsIgnoreCase(VehiclesTable.COL_VEHICLE_REN_DATE)) {
            long vMonthYear = vehicleItem.getVehicleLpRenewalDate();
            SimpleDateFormat ymSimpleDateFormat = new SimpleDateFormat("MMM-yyyy", Locale.US);
            String monthYear = ymSimpleDateFormat.format(vMonthYear);
            vdf_obj.updateVehicleLpRenewalDate(vMonthYear, monthYear);
        }
    }


    /**
     *
     * @param choice
     */
    @Override
    public void onVehicleImageChoiceFragmentInteraction(int choice) {
        switch (choice){
            case CHOOSE_APP_ICON:
                ChooseAppIcon();
                break;
            case CHOOSE_IMAGE_ON_DEVICE:
                ChooseImageOnDevice();
                break;
            case TAKE_PHOTO:
                break;
        }
    }

    public void ChooseAppIcon() {
        showVehicleImageFromIcon();
    }

    public void ChooseImageOnDevice() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOOSE_IMAGE_ON_DEVICE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE_ON_DEVICE && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

//            bitmap = BitmapFactory.decodeFile(picturePath);
//            image.setImageBitmap(bitmap);
//
//            if (bitmap != null) {
//                ImageView rotate = (ImageView) findViewById(R.id.rotate);
//
//            }

        } else {

//            Log.i("SonaSys", "resultCode: " + resultCode);
//            switch (resultCode) {
//                case 0:
//                    Log.i("SonaSys", "User cancelled");
//                    break;
//                case -1:
//                    onPhotoTaken();
//                    break;
//
//            }
        }
    }

    private void showVehicleImageFromIcon() {
        IconFromAssetsFragment iconFromAssetsFragment = IconFromAssetsFragment.newInstance();
        iconFromAssetsFragment.show(getFragmentManager(), "Vehicle Image Icon From Asset");
    }

    @Override
    public void onListFragmentInteraction(String imageString) {
        // Get a reference to the vehicle detail fragment in order to update the values
        VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.vehicle_detail_container);
        // Update the displayed image in the editor
        vdf_obj.updateVehicleImage((VehiclesTable.VEHICLE_ICONS_FOLDER + imageString));
    }

    @Override
    public void onBackPressed(){
        if (mMode != VehicleDetailFragment.DETAIL_IN_VIEW_MODE) {
            // Get a reference to the vehicle detail fragment in order to update the values
            VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.vehicle_detail_container);
            vdf_obj.finishEditing();
        }
        finish();
    }
}
