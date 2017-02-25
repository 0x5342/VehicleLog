package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.CustomDatePickerDialogFragment;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;
import com.diversedistractions.vehiclelog.utilities.IconFromAssetsFragment;

import java.io.IOException;
import java.io.InputStream;

/**
 * A fragment representing a single Vehicle detail screen.
 * This fragment is either contained in a {@link VehicleListActivity}
 * in two-pane mode (on tablets) or a {@link VehicleDetailActivity}
 * on handsets.
 */
public class VehicleDetailFragment extends DialogFragment {

    public static final String ARG_ITEM_URI = "item_uri";
    public static final String DETAIL_MODE = "detail_mode";
    public static final int DETAIL_IN_CREATE_MODE = 0;
    public static final int DETAIL_IN_VIEW_MODE = 1;
    public static final int DETAIL_IN_EDIT_MODE = 2;

    private Context mContext;
    private int mMode;
    DateConversionHelper dateConversionHelper;
    View rootView = null;

    VehicleItem vehicleItem = new VehicleItem();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getActivity();
        dateConversionHelper = new DateConversionHelper();

        // If arguments contains a URI, retrieve it & assign to mVehicleUri.
        // Else set mVehicleUri to null.
        Uri mVehicleUri;
        if (getArguments().containsKey(ARG_ITEM_URI)) {
            mVehicleUri = getArguments().getParcelable(ARG_ITEM_URI);
        } else {
            mVehicleUri = null;
        }

        // If arguments contains what the mode should be, assign it to mMode.
        // Else assume should be in create mode.
        if (getArguments().containsKey(DETAIL_MODE)) {
            mMode = getArguments().getInt(DETAIL_MODE);
        } else {
            mMode = DETAIL_IN_CREATE_MODE;
        }

        // If there is a vehicle URI then we are in edit or view mode, so retrieve existing data.
        if (mVehicleUri != null) {
            getVehicleData(mVehicleUri);
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.
                findViewById(R.id.toolbar_layout);

        // When the fragment is not launched by an intent, and therefore VehicleDetailActivity won't
        // hide this button in all but view mode, this will allow the button to be hidden.
        FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        if (mMode != VehicleDetailFragment.DETAIL_IN_VIEW_MODE) {
            fab.setVisibility(View.INVISIBLE);
        }

        // Set the title in the appBar (if active) to match whether creating, viewing, or editing.
        if (appBarLayout != null) {
            switch (mMode){
                case 0:
                    appBarLayout.setTitle(getString(R.string.new_vehicle));
                    break;
                case 1:
                    appBarLayout.setTitle(getString(R.string.view_vehicle));
                    break;
                case 2:
                    appBarLayout.setTitle(getString(R.string.edit_vehicle));
            }
        }
    }

    /**
     * //TODO: description of class
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
         * Alter the layout and population of fields depending on whether a vehicle is just being
         * viewed, is in edit mode, or is being created from scratch.
         */
        switch (mMode) {

            case DETAIL_IN_CREATE_MODE:

                rootView = inflater.inflate(R.layout.vehicle_detail_edit, container, false);

                Button btnSetVehicleYearCreate = (Button) rootView
                        .findViewById(R.id.vehicleYearButton);
                btnSetVehicleYearCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_YEAR);
                    }
                });

                // If a utility type vehicle is chosen, hide the license plate and license plate
                // renewal date. If a car, truck, motorcycle vehicle type is chosen, make the
                // license plate and license plate renewal date visible.
                ((Spinner) rootView.findViewById(R.id.vehicleTypeSpinner))
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        if (spinner.getSelectedItemPosition() == 0) {
                            (rootView.findViewById(R.id.lpRenewalDateEditContainer))
                                    .setVisibility(View.VISIBLE);
                            (rootView.findViewById(R.id.lpEditContainer))
                                    .setVisibility(View.VISIBLE);
                        } else {
                            (rootView.findViewById(R.id.lpRenewalDateEditContainer))
                                    .setVisibility(View.GONE);
                            (rootView.findViewById(R.id.lpEditContainer))
                                    .setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button btnSetLpRenewalDateCreate = (Button) rootView
                        .findViewById(R.id.vehicleLpRenewalDateButton);
                btnSetLpRenewalDateCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_REN_DATE);
                    }
                });

                break;

            case DETAIL_IN_EDIT_MODE:

                // Show the vehicle's content in edit mode
                rootView = inflater.inflate(R.layout.vehicle_detail_edit, container, false);

                ((Spinner) rootView.findViewById(R.id.vehicleTypeSpinner))
                        .setSelection(vehicleItem.getVehicleType());
                // If a utility type vehicle is chosen, hide the license plate and license plate
                // renewal date. If a car, truck, motorcycle vehicle type is chosen, make the
                // license plate and license plate renewal date visible.
                ((Spinner) rootView.findViewById(R.id.vehicleTypeSpinner))
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        if (spinner.getSelectedItemPosition() == 0) {
                            (rootView.findViewById(R.id.lpRenewalDateEditContainer))
                                    .setVisibility(View.VISIBLE);
                            (rootView.findViewById(R.id.lpEditContainer))
                                    .setVisibility(View.VISIBLE);
                        } else {
                            (rootView.findViewById(R.id.lpRenewalDateEditContainer))
                                    .setVisibility(View.GONE);
                            (rootView.findViewById(R.id.lpEditContainer))
                                    .setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button btnSetVehicleYearEdit = (Button) rootView
                        .findViewById(R.id.vehicleYearButton);
                btnSetVehicleYearEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_YEAR);
                    }
                });
                btnSetVehicleYearEdit.setText(dateConversionHelper
                        .getYearAsString(vehicleItem.getVehicleYear()));

                ((EditText) rootView.findViewById(R.id.makeEditText))
                        .setText(vehicleItem.getVehicleMake());

                ((EditText) rootView.findViewById(R.id.modelEditText))
                        .setText(vehicleItem.getVehicleModel());

                ImageButton btnSetVehicleImageEdit = (ImageButton) rootView
                        .findViewById(R.id.vehicleImageButton);
                btnSetVehicleImageEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showVehicleImageChoice();
                    }
                });
                try {
                    InputStream inputStream = getContext().getAssets()
                            .open(vehicleItem.getVehicleImage());
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    btnSetVehicleImageEdit.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((EditText) rootView.findViewById(R.id.vehicleVinEditText))
                        .setText(vehicleItem.getVehicleVin());

                /*
                 * If the vehicle is a car, truck, motorcycle, etc., there will be a license plate
                 * and a license plate renewal date to put data into. If the vehicle is a utility
                 * vehicle, we need to hide the license plate and license plate renewal date fields.
                 */
                if (vehicleItem.getVehicleType()==0) {
                    ((EditText) rootView.findViewById(R.id.vehicleLicensePlateEditText))
                            .setText(vehicleItem.getVehicleLp());
                    Button btnSetLpRenewalDateEdit = (Button) rootView
                            .findViewById(R.id.vehicleLpRenewalDateButton);
                    btnSetLpRenewalDateEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCustomDatePicker(VehiclesTable.COL_VEHICLE_REN_DATE);
                        }
                    });
                    btnSetLpRenewalDateEdit.setText(dateConversionHelper
                            .getYearMonthAsString(vehicleItem.getVehicleLpRenewalDate()));
                } else {
                    (rootView.findViewById(R.id.lpRenewalDateEditContainer)).setVisibility(View.GONE);
                    (rootView.findViewById(R.id.lpEditContainer)).setVisibility(View.GONE);
                }

                // Don't set the text if there isn't any saved text
                if (vehicleItem.getVehicleNotes() != null &&
                        vehicleItem.getVehicleNotes().length() > 0) {
                    ((EditText) rootView.findViewById(R.id.vehNotesEditText))
                            .setText(vehicleItem.getVehicleNotes());
                }

                break;

            case DETAIL_IN_VIEW_MODE:

                // Show the vehicle's content
                rootView = inflater.inflate(R.layout.vehicle_detail_view, container, false);

                ((TextView) rootView.findViewById(R.id.vehicleYearText))
                        .setText(dateConversionHelper.getYearAsString
                                (vehicleItem.getVehicleYear()));

                ((TextView) rootView.findViewById(R.id.makeText))
                        .setText(vehicleItem.getVehicleMake());

                ((TextView) rootView.findViewById(R.id.modelText))
                        .setText(vehicleItem.getVehicleModel());

                try {
                    InputStream inputStream = getContext().getAssets().
                            open(vehicleItem.getVehicleImage());
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    ((ImageView) rootView.findViewById(R.id.vehicleImage))
                            .setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((TextView) rootView.findViewById(R.id.vehicleVinText))
                        .setText(vehicleItem.getVehicleVin());

                /*
                 * If the vehicle is a car, truck, motorcycle, etc., there will be a license plate
                 * and a license plate renewal date to put data into. If the vehicle is a utility
                 * vehicle, we need to hide the license plate and license plate renewal date fields.
                 */
                if (vehicleItem.getVehicleType()==0) {
                    ((TextView) rootView.findViewById(R.id.vehicleLicensePlateText))
                            .setText(vehicleItem.getVehicleLp());
                    ((TextView) rootView.findViewById(R.id.vehicleLpRenewalDateText))
                            .setText(dateConversionHelper.getYearMonthAsString
                                    (vehicleItem.getVehicleLpRenewalDate()));
                } else {
                    (rootView.findViewById(R.id.lpRenewalDateViewContainer)).setVisibility(View.GONE);
                    (rootView.findViewById(R.id.lpViewContainer)).setVisibility(View.GONE);
                }

                // Don't set the text if there isn't any saved text
                if (vehicleItem.getVehicleTdEfficiency() != null) {
                    ((TextView) rootView.findViewById(R.id.vehToDateText))
                            .setText(vehicleItem.getVehicleTdEfficiency());
                }

                // Don't set the text if there isn't any saved text
                if (vehicleItem.getVehicleNotes() != null &&
                        vehicleItem.getVehicleNotes().length() > 0) {
                    ((TextView) rootView.findViewById(R.id.vehNotesText))
                            .setText(vehicleItem.getVehicleNotes());
                }

                break;
        }
        return rootView;
    }

    /**
     * Access the content provider, retrieve the data for the vehicle represented by the URI as a
     * cursor, and assign the data in the fields to aptly named variables that will be used to
     * populate layout fields.
     * @param uri is the URI for the individual vehicle that will provide data for the layout
     */
    private void getVehicleData (Uri uri) {

        Cursor cursor = mContext.getContentResolver().query
                (uri, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            vehicleItem.setVehicleId(cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ID)));
            vehicleItem.setVehicleType(cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TYPE)));
            vehicleItem.setVehicleMake(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE)));
            vehicleItem.setVehicleModel(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL)));
            vehicleItem.setVehicleYear(cursor.getLong
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
            vehicleItem.setVehicleVin(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_VIN)));
            vehicleItem.setVehicleLp(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_LP)));
            vehicleItem.setVehicleLpRenewalDate(cursor.getLong
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_REN_DATE)));
            vehicleItem.setVehicleImage(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE)));
            vehicleItem.setVehicleTdEfficiency(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TD_EFF)));
            vehicleItem.setVehicleNotes(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_NOTE)));
            vehicleItem.setVehicleModOrder(cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER)));

            cursor.close();
        }
    }

    /**
     * Launches the custom DatePickerDialog and passes in the vehicle data as a parselable
     * vehicleItem. The dateField is used to tell the DatePicker whether to display just the year
     * or both the month and year.
     * @param dateField: DatePicker was call for the vehicle year or the LP renewal date
     */
    private void showCustomDatePicker(String dateField){

        CustomDatePickerDialogFragment customDatePickerDialogFragment =
                CustomDatePickerDialogFragment.newInstance(vehicleItem, dateField);

        customDatePickerDialogFragment.show(getFragmentManager(), "Custom Date Picker");
    }

    /**
     * This is used to set the text on the vehicle year button from the activity when the DatePicker
     * ENTER button is pressed.
     * @param year: a four character string representing the vehicle year
     */
    public void updateVehicleYear(String year){
        ((Button) rootView.findViewById(R.id.vehicleYearButton)).setText(year);
    }

    /**
     * This is used to set the text on the license plate renewal date button from the activity when
     * the DatePicker ENTER button is pressed.
     * @param monthYear
     */
    public void updateVehicleLpRenewalDate(String monthYear){
        ((Button) rootView.findViewById(R.id.vehicleLpRenewalDateButton)).setText(monthYear);
    }

    private void showVehicleImageChoice() {

        VehicleImageChoiceFragment vehicleImageChoiceFragment =
                VehicleImageChoiceFragment.newInstance();

        vehicleImageChoiceFragment.show(getFragmentManager(), "Vehicle Image Choice");
    }
}
