package com.diversedistractions.vehiclelog;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.CustomDatePickerDialogFragment;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;
import com.diversedistractions.vehiclelog.utilities.IconFromAssetsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An activity representing a single Vehicle detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link VehicleListActivity}.
 */
public class VehicleDetailActivity extends AppCompatActivity
        implements CustomDatePickerDialogFragment.CustomDatePickerListener,
        VehicleImageChoiceFragment.OnVehicleImageChoiceFragmentInteractionListener,
        IconFromAssetsFragment.OnListFragmentInteractionListener,
        ConfirmDeleteDialogFragment.ConfirmationListener,
        SaveDismissReturnDialogFragment.OnSaveDismissReturnInteractionListener{

    public static final int CHOOSE_APP_ICON = 2001;
    public static final int CHOOSE_IMAGE_ON_DEVICE = 2002;
    public static final int TAKE_PHOTO = 2003;
    public static final int SAVE_EDIT = 3001;
    public static final int CANCEL_EDIT = 3002;
    public static final int RETURN_TO_EDIT = 3003;
    private static final String IMAGE_PATH = "/photos/";
    DateConversionHelper dateConversionHelper;
    private Uri mVehicleUri;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

        dateConversionHelper = new DateConversionHelper();

        Intent intent = getIntent();
        mVehicleUri = intent.getParcelableExtra(VehicleDetailFragment.ARG_ITEM_URI);
        mMode = intent.getIntExtra(VehicleDetailFragment.DETAIL_MODE, 0);
        FloatingActionButton fabFuelEntry = (FloatingActionButton) findViewById(R.id.fabFuelEntry);
        FloatingActionButton fabServiceEntry = (FloatingActionButton) findViewById(R.id.fabServiceEntry);
        // When not in view mode, hide both the fuel and service entry FABs.
        if (mMode != VehicleDetailFragment.DETAIL_IN_VIEW_MODE){
            fabServiceEntry.setVisibility(View.INVISIBLE);
            fabFuelEntry.setVisibility(View.INVISIBLE);
        } else {
            fabFuelEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: complete code to go to fuel entries
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FuelEntryListActivity.class);

                    startActivity(intent);

                }
            });
            fabServiceEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: change code to go to service entries
//                    // Replace the VehicleDetailFragment in view mode with it in edit mode.
//                    mMode = VehicleDetailFragment.DETAIL_IN_EDIT_MODE;
//                    Bundle arguments = new Bundle();
//                    arguments.putParcelable(VehicleDetailFragment.ARG_ITEM_URI, mVehicleUri);
//                    arguments.putInt(VehicleDetailFragment.DETAIL_MODE,
//                            VehicleDetailFragment.DETAIL_IN_EDIT_MODE);
//                    VehicleDetailFragment fragment = new VehicleDetailFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.vehicle_detail_container, fragment)
//                            .commit();
                }
            });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vehicle_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (mMode != VehicleDetailFragment.DETAIL_IN_VIEW_MODE) {
                    askToSave();
                    return true;
                } else {
                    NavUtils.navigateUpTo(this, new Intent(this, VehicleListActivity.class));
                    return true;
                }
            case R.id.action_veh_delete:
                VehicleDetailFragment vdf_obj=(VehicleDetailFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.vehicle_detail_container);
                vdf_obj.showConfirmDeleteDialog();
                return true;
            case R.id.action_veh_edit:
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called after the custom date picker dialog to update the appropriate date field in the
     * vehicle that is being created or edited.
     *
     * @param vehicleItem: the temporary vehicle that the DatePicker assigned the date to
     * @param dateField: a string to denote whether it was the vehicle year or license renewal date
     */
    @Override
    public void onDatePickComplete(VehicleItem vehicleItem, String dateField) {

        // Get a reference to the vehicle detail fragment in order to update the values
        VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.vehicle_detail_container);

        // Only update the appropriate date field
        if (dateField.equalsIgnoreCase(VehiclesTable.COL_VEHICLE_YEAR)) {
            long vYear = vehicleItem.getVehicleYear();
            String year = dateConversionHelper.getYearAsString(vYear);
            vdf_obj.updateVehicleYear(vYear, year);
        } else if (dateField.equalsIgnoreCase(VehiclesTable.COL_VEHICLE_REN_DATE)) {
            long vMonthYear = vehicleItem.getVehicleLpRenewalDate();
            String monthYear = dateConversionHelper.getYearMonthAsString(vMonthYear);
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

    // interface from ConfirmDeleteDialogFragment
    public void onConfirmVehicleDeleteInteraction(boolean choice) {
        if (choice) {
            VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.vehicle_detail_container);
            vdf_obj.deleteVehicle();
            finish();
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

            BitmapFactory.Options bfOptions = new BitmapFactory.Options();
            bfOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, bfOptions);
            int width = bfOptions.outWidth;
            int height = bfOptions.outHeight;
            if (width > height) {
                height = (height*(1600000/width))/10000;
                width = 160;
            } else {
                width = (width*(1220000/height))/10000;
                height = 122;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            int count = getFilesDir().listFiles().length;
            String FILE_NAME = count+1+".png";
            try {
                FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Get a reference to the vehicle detail fragment in order to update the values
            VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.vehicle_detail_container);
            // Update the displayed image in the editor
            vdf_obj.updateVehicleImage((FILE_NAME));

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
            askToSave();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public void askToSave(){
        SaveDismissReturnDialogFragment saveDismissReturnDialogFragment =
                SaveDismissReturnDialogFragment.newInstance();
        saveDismissReturnDialogFragment.show(getSupportFragmentManager(), "SaveDismissReturn");
    }

    @Override
    public void onSaveDismissReturnInteraction(int choice) {
        switch (choice){
            case SAVE_EDIT:
                // Get a reference to the vehicle detail fragment in order to update the values
                VehicleDetailFragment vdf_obj = (VehicleDetailFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.vehicle_detail_container);
                vdf_obj.finishEditing();
                NavUtils.navigateUpTo(this, new Intent(this, VehicleListActivity.class));
                break;
            case RETURN_TO_EDIT:
                break;
            case CANCEL_EDIT:
                NavUtils.navigateUpTo(this, new Intent(this, VehicleListActivity.class));
                break;
        }
    }
}
