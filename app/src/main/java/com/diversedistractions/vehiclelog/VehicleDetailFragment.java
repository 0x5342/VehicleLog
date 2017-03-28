package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.diversedistractions.vehiclelog.database.VehicleLogContentProvider;
import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.CustomDatePickerDialogFragment;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;
import com.diversedistractions.vehiclelog.utilities.VehicleModOrderTool;

import java.io.File;
import java.io.FileInputStream;
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
    public static final int DETAIL_IN_CREATE_MODE = 4001;
    public static final int DETAIL_IN_VIEW_MODE = 4002;
    public static final int DETAIL_IN_EDIT_MODE = 4003;
    DateConversionHelper dateConversionHelper;
    View rootView = null;
    VehicleItem vehicleItem;
    VehicleItem vehicleItemOriginal;
    private ImageButton mVehImageButton;
    private Spinner mVehTypeSpinner;
    private Button mVehYearButton;
    private EditText mMakeText;
    private EditText mModelText;
    private EditText mVinText;
    private LinearLayout mLpContainer;
    private EditText mLpText;
    private LinearLayout mLpRenewDateContainer;
    private Button mLpRenewDateButton;
    private EditText mNotesText;
    private Context mContext;
    private int mMode;

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
        vehicleItem = new VehicleItem();


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
            // If in edit mode, create another vehicle item to store the original data to compare
            // to when done editing to verify if any fields changed.
            if (mMode == DETAIL_IN_EDIT_MODE) {
                vehicleItemOriginal = new VehicleItem();
            }
        } else {
            mMode = DETAIL_IN_CREATE_MODE;
        }

        // If there is a vehicle URI then we are in edit or view mode, so retrieve existing data.
        // If not, create a new vehicleItem that can start containing the created data.
        if (mVehicleUri != null) {
            getVehicleData(mVehicleUri);
        } else {
            vehicleItem = new VehicleItem();
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.
                findViewById(R.id.toolbar_layout);

        // When the fragment is not launched by an intent, and therefore VehicleDetailActivity won't
        // hide this button in all but view mode, this will allow the button to be hidden.
        FloatingActionButton fabDelete =(FloatingActionButton)activity.findViewById(R.id.fabFuelEntry);
        FloatingActionButton fabEdit = (FloatingActionButton) activity.findViewById(R.id.fabServiceEntry);

        // Set the title in the appBar (if active) to match whether creating, viewing, or editing.
        if (appBarLayout != null) {
            switch (mMode){
                case VehicleDetailFragment.DETAIL_IN_CREATE_MODE:
                    appBarLayout.setTitle(getString(R.string.new_vehicle));
                    fabEdit.setVisibility(View.INVISIBLE);
                    fabDelete.setVisibility(View.INVISIBLE);
                    break;
                case VehicleDetailFragment.DETAIL_IN_VIEW_MODE:
                    appBarLayout.setTitle(getString(R.string.view_vehicle));
                    break;
                case VehicleDetailFragment.DETAIL_IN_EDIT_MODE:
                    appBarLayout.setTitle(getString(R.string.edit_vehicle));
                    fabEdit.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

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

                mVehImageButton = (ImageButton) rootView.findViewById(R.id.vehicleImageButton);
                mVehTypeSpinner = (Spinner) rootView.findViewById(R.id.vehicleTypeSpinner);
                mVehYearButton = (Button) rootView.findViewById(R.id.vehicleYearButton);
                mMakeText = (EditText) rootView.findViewById(R.id.makeEditText);
                mModelText = (EditText) rootView.findViewById(R.id.modelEditText);
                mVinText = (EditText) rootView.findViewById(R.id.vehicleVinEditText);
                mLpContainer = (LinearLayout) rootView.findViewById(R.id.lpEditContainer);
                mLpText = (EditText) rootView.findViewById(R.id.vehicleLicensePlateEditText);
                mLpRenewDateContainer = (LinearLayout) rootView.
                        findViewById(R.id.lpRenewalDateEditContainer);
                mLpRenewDateButton = (Button)rootView.findViewById(R.id.vehicleLpRenewalDateButton);
                mNotesText = (EditText) rootView.findViewById(R.id.vehNotesEditText);

                mVehImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showVehicleImageChoice();
                    }
                });
                try {
                    InputStream inputStream = getContext().getAssets()
                            .open(VehiclesTable.VEHICLE_NO_ICON);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    mVehImageButton.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mVehYearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_YEAR);
                    }
                });

                // If a utility type vehicle is chosen, hide the license plate and license plate
                // renewal date. If a car, truck, motorcycle vehicle type is chosen, make the
                // license plate and license plate renewal date visible.
                mVehTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        vehicleItem.setVehicleType(spinner.getSelectedItemPosition());
                        if (spinner.getSelectedItemPosition() == 0) {
                            mLpRenewDateContainer.setVisibility(View.VISIBLE);
                            mLpContainer.setVisibility(View.VISIBLE);
                        } else {
                            mLpRenewDateContainer.setVisibility(View.GONE);
                            mLpContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                mLpRenewDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_REN_DATE);
                    }
                });

                break;

            case DETAIL_IN_EDIT_MODE:

                // Show the vehicle's content in edit mode
                rootView = inflater.inflate(R.layout.vehicle_detail_edit, container, false);

                mVehImageButton = (ImageButton) rootView.findViewById(R.id.vehicleImageButton);
                mVehTypeSpinner = (Spinner) rootView.findViewById(R.id.vehicleTypeSpinner);
                mVehYearButton = (Button) rootView.findViewById(R.id.vehicleYearButton);
                mMakeText = (EditText) rootView.findViewById(R.id.makeEditText);
                mModelText = (EditText) rootView.findViewById(R.id.modelEditText);
                mVinText = (EditText) rootView.findViewById(R.id.vehicleVinEditText);
                mLpContainer = (LinearLayout) rootView.findViewById(R.id.lpEditContainer);
                mLpText = (EditText) rootView.findViewById(R.id.vehicleLicensePlateEditText);
                mLpRenewDateContainer = (LinearLayout) rootView.
                        findViewById(R.id.lpRenewalDateEditContainer);
                mLpRenewDateButton = (Button)rootView.findViewById(R.id.vehicleLpRenewalDateButton);
                mNotesText = (EditText) rootView.findViewById(R.id.vehNotesEditText);

                mVehImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showVehicleImageChoice();
                    }
                });
                //TODO: simplify this
                // If there is a path to a vehicle image, load it; if not load the no image icon
                if (vehicleItem.getVehicleImage() != null) {
                    if (vehicleItem.getVehicleImage().length() >= 5) {
                        if (vehicleItem.getVehicleImage().substring(0,5)
                                .equals(VehiclesTable.VEHICLE_ICONS_FOLDER.substring(0,5))) {
                            try {
                                InputStream inputStream = getContext().getAssets()
                                        .open(vehicleItem.getVehicleImage());
                                Drawable drawable = Drawable.createFromStream(inputStream, null);
                                mVehImageButton.setImageDrawable(drawable);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                                File directory = cw.getFilesDir();
                                File file = new File(directory, vehicleItem.getVehicleImage());
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                                mVehImageButton.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                            File directory = cw.getFilesDir();
                            File file = new File(directory, vehicleItem.getVehicleImage());
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            mVehImageButton.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        InputStream inputStream = getContext().getAssets()
                                .open(VehiclesTable.VEHICLE_NO_ICON);
                        Drawable drawable = Drawable.createFromStream(inputStream, null);
                        mVehImageButton.setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mVehTypeSpinner.setSelection(vehicleItem.getVehicleType());
                // If a utility type vehicle is chosen, hide the license plate and license plate
                // renewal date. If a car, truck, motorcycle vehicle type is chosen, make the
                // license plate and license plate renewal date visible.
                mVehTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        vehicleItem.setVehicleType(spinner.getSelectedItemPosition());
                        if (spinner.getSelectedItemPosition() == 0) {
                            mLpRenewDateContainer.setVisibility(View.VISIBLE);
                            mLpContainer.setVisibility(View.VISIBLE);
                        } else {
                            mLpRenewDateContainer.setVisibility(View.GONE);
                            mLpContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                mVehYearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomDatePicker(VehiclesTable.COL_VEHICLE_YEAR);
                    }
                });
                mVehYearButton.setText(dateConversionHelper
                        .getYearAsString(vehicleItem.getVehicleYear()));

                mMakeText.setText(vehicleItem.getVehicleMake());
                mModelText.setText(vehicleItem.getVehicleModel());
                mVinText.setText(vehicleItem.getVehicleVin());

                /*
                 * If the vehicle is a car, truck, motorcycle, etc., there will be a license plate
                 * and a license plate renewal date to put data into. If the vehicle is a utility
                 * vehicle, we need to hide the license plate and license plate renewal date fields.
                 */
                if (vehicleItem.getVehicleType()==0) {
                    mLpText.setText(vehicleItem.getVehicleLp());
                    mLpRenewDateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCustomDatePicker(VehiclesTable.COL_VEHICLE_REN_DATE);
                        }
                    });
                    mLpRenewDateButton.setText(dateConversionHelper
                            .getYearMonthAsString(vehicleItem.getVehicleLpRenewalDate()));
                } else {
                    mLpRenewDateContainer.setVisibility(View.GONE);
                    mLpContainer.setVisibility(View.GONE);
                }

                // Don't set the text if there isn't any saved text
                if (vehicleItem.getVehicleNotes() != null &&
                        vehicleItem.getVehicleNotes().length() > 0) {
                    mNotesText.setText(vehicleItem.getVehicleNotes());
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

                //TODO: simplify this
                // If there is a path to a vehicle image, load it; if not load the no image icon
                if (vehicleItem.getVehicleImage() != null) {
                    if (vehicleItem.getVehicleImage().length() >= 5) {
                        if (vehicleItem.getVehicleImage().substring(0,5)
                                .equals(VehiclesTable.VEHICLE_ICONS_FOLDER.substring(0,5))) {
                            try {
                                InputStream inputStream = getContext().getAssets()
                                        .open(vehicleItem.getVehicleImage());
                                Drawable drawable = Drawable.createFromStream(inputStream, null);
                                ((ImageView) rootView.findViewById(R.id.vehicleImage))
                                        .setImageDrawable(drawable);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                                File directory = cw.getFilesDir();
                                File file = new File(directory, vehicleItem.getVehicleImage());
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                                ((ImageView) rootView.findViewById(R.id.vehicleImage))
                                        .setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                            File directory = cw.getFilesDir();
                            File file = new File(directory, vehicleItem.getVehicleImage());
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            ((ImageView) rootView.findViewById(R.id.vehicleImage))
                                    .setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        InputStream inputStream = getContext().getAssets()
                                .open(VehiclesTable.VEHICLE_NO_ICON);
                        Drawable drawable = Drawable.createFromStream(inputStream, null);
                        ((ImageView) rootView.findViewById(R.id.vehicleImage))
                                .setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    (rootView.findViewById(R.id.lpRenewalDateViewContainer))
                            .setVisibility(View.GONE);
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
            vehicleItem.setVehicleYear(cursor.getLong
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
            vehicleItem.setVehicleMake(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE)));
            vehicleItem.setVehicleModel(cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL)));
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

            // make an original to compare to if in edit mode
            if (mMode == VehicleDetailFragment.DETAIL_IN_EDIT_MODE) {
                vehicleItemOriginal.setVehicleId(cursor.getInt
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_ID)));
                vehicleItemOriginal.setVehicleType(cursor.getInt
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TYPE)));
                vehicleItemOriginal.setVehicleYear(cursor.getLong
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
                vehicleItemOriginal.setVehicleMake(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE)));
                vehicleItemOriginal.setVehicleModel(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL)));
                vehicleItemOriginal.setVehicleVin(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_VIN)));
                vehicleItemOriginal.setVehicleLp(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_LP)));
                vehicleItemOriginal.setVehicleLpRenewalDate(cursor.getLong
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_REN_DATE)));
                vehicleItemOriginal.setVehicleImage(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE)));
                vehicleItemOriginal.setVehicleTdEfficiency(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TD_EFF)));
                vehicleItemOriginal.setVehicleNotes(cursor.getString
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_NOTE)));
                vehicleItemOriginal.setVehicleModOrder(cursor.getInt
                        (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER)));
            }


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
     * @param epochYear: the year in epoch form
     * @param year: the year in string form
     */
    public void updateVehicleYear(long epochYear, String year){
        mVehYearButton.setText(year);
        vehicleItem.setVehicleYear(epochYear);
    }

    /**
     * This is used to set the text on the license plate renewal date button from the activity when
     * the DatePicker ENTER button is pressed.
     * @param epochMonthYear: the date in epoch form
     * @param monthYear: the date in string form
     */
    public void updateVehicleLpRenewalDate(long epochMonthYear, String monthYear){
        mLpRenewDateButton.setText(monthYear);
        vehicleItem.setVehicleLpRenewalDate(epochMonthYear);
    }

    /**
     * Starts the fragment that allows one to choose WHERE the image will come from.
     */
    private void showVehicleImageChoice() {
        VehicleImageChoiceFragment vehicleImageChoiceFragment =
                VehicleImageChoiceFragment.newInstance();
        vehicleImageChoiceFragment.show(getFragmentManager(), "Vehicle Image Choice");
    }

    /**
     * Updates the vehicle image after selecting a new image and sets the vehicleItem image to the
     * selected image as well.
     * @param image: a string representing the path and image
     */
    public void updateVehicleImage(String image){
        //TODO: simplify this
        if (image != null) {
            if (image.length() >= 5) {
                if (image.substring(0,5)
                        .equals(VehiclesTable.VEHICLE_ICONS_FOLDER.substring(0,5))) {
                    try {
                        InputStream inputStream = getContext().getAssets().open(image);
                        Drawable drawable = Drawable.createFromStream(inputStream, null);
                        mVehImageButton.setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                        File directory = cw.getFilesDir();
                        File file = new File(directory, image);
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                        mVehImageButton.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
                    File directory = cw.getFilesDir();
                    File file = new File(directory, image);
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    mVehImageButton.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            vehicleItem.setVehicleImage(image);
        } else {
            try {
                InputStream inputStream = getContext().getAssets()
                        .open(VehiclesTable.VEHICLE_NO_ICON);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                mVehImageButton.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
            vehicleItem.setVehicleImage(VehiclesTable.VEHICLE_NO_ICON);
        }
    }

    public void finishEditing() {
        //TODO: handle blank entries
        vehicleItem.setVehicleMake(mMakeText.getText().toString().trim());
        vehicleItem.setVehicleModel(mModelText.getText().toString().trim());
        vehicleItem.setVehicleVin(mVinText.getText().toString().trim());
        vehicleItem.setVehicleLp(mLpText.getText().toString().trim());
        vehicleItem.setVehicleNotes(mNotesText.getText().toString().trim());

        ContentValues values = new ContentValues();
        values.put(VehiclesTable.COL_VEHICLE_TYPE, vehicleItem.getVehicleType());
        values.put(VehiclesTable.COL_VEHICLE_MAKE, vehicleItem.getVehicleMake());
        values.put(VehiclesTable.COL_VEHICLE_MODEL, vehicleItem.getVehicleModel());
        values.put(VehiclesTable.COL_VEHICLE_YEAR, vehicleItem.getVehicleYear());
        values.put(VehiclesTable.COL_VEHICLE_VIN, vehicleItem.getVehicleVin());
        values.put(VehiclesTable.COL_VEHICLE_LP, vehicleItem.getVehicleLp());
        values.put(VehiclesTable.COL_VEHICLE_REN_DATE, vehicleItem.getVehicleLpRenewalDate());
        if (vehicleItem.getVehicleImage()!=null) {
            values.put(VehiclesTable.COL_VEHICLE_IMAGE, vehicleItem.getVehicleImage());
        } else {
            values.put(VehiclesTable.COL_VEHICLE_IMAGE, VehiclesTable.VEHICLE_NO_ICON);
        }
        values.put(VehiclesTable.COL_VEHICLE_TD_EFF, vehicleItem.getVehicleTdEfficiency());
        values.put(VehiclesTable.COL_VEHICLE_NOTE, vehicleItem.getVehicleNotes());
        values.put(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, vehicleItem.getVehicleModOrder());

        switch (mMode){
            case DETAIL_IN_EDIT_MODE:
                if (vehicleItem.getVehicleType()==vehicleItemOriginal.getVehicleType() &&
                        (vehicleItem.getVehicleMake()).equals
                                (vehicleItemOriginal.getVehicleMake()) &&
                        (vehicleItem.getVehicleModel()).equals
                                (vehicleItemOriginal.getVehicleModel()) &&
                        vehicleItem.getVehicleYear()==vehicleItemOriginal.getVehicleYear() &&
                        (vehicleItem.getVehicleVin()).equals
                                (vehicleItemOriginal.getVehicleVin()) &&
                        (vehicleItem.getVehicleLp()).equals(vehicleItemOriginal.getVehicleLp()) &&
                        vehicleItem.getVehicleLpRenewalDate()==
                                vehicleItemOriginal.getVehicleLpRenewalDate() &&
                        (vehicleItem.getVehicleImage()).equals
                                (vehicleItemOriginal.getVehicleImage()) &&
                        (vehicleItem.getVehicleNotes()).equals
                                (vehicleItemOriginal.getVehicleNotes())) {
                    break;
                } else {
                    updateVehicle(values);
                    break;
                }
            case DETAIL_IN_CREATE_MODE:
                insertVehicle(values);
                break;
        }
    }

    /**
     * Class that will update an existing vehicle in the database through the content provider
     * @param values the vehicle item content values
     */
    private void updateVehicle(ContentValues values) {
        getContext().getContentResolver().update(VehicleLogContentProvider.VEHICLE_CONTENT_URI,
                values, VehiclesTable.COL_VEHICLE_ID+"="+vehicleItem.getVehicleId(),null);
        Uri uri = Uri.parse(VehicleLogContentProvider.VEHICLE_CONTENT_URI+
                "/"+vehicleItem.getVehicleId());
        VehicleModOrderTool vehicleModOrderTool = new VehicleModOrderTool(getContext());
        vehicleModOrderTool.RenumberVehicleModOrder(uri);
    }

    /**
     * Class that will add a vehicle to the database through the content provider
     * @param values the vehicle item content values
     */
    private void insertVehicle(ContentValues values) {
        Uri uri = getContext().getContentResolver()
                .insert(VehicleLogContentProvider.VEHICLE_CONTENT_URI, values);
        VehicleModOrderTool vehicleModOrderTool = new VehicleModOrderTool(getContext());
        vehicleModOrderTool.RenumberVehicleModOrder(uri);
    }

    public void deleteVehicle() {
        getContext().getContentResolver()
                .delete(VehicleLogContentProvider.VEHICLE_CONTENT_URI,
                        VehiclesTable.COL_VEHICLE_ID+"="+vehicleItem.getVehicleId(),null);
        VehicleModOrderTool vehicleModOrderTool = new VehicleModOrderTool(getContext());
        vehicleModOrderTool.RenumberVehicleModOrder(null);
    }

    public void showConfirmDeleteDialog() {
        ConfirmDeleteDialogFragment confirmDeleteDialogFragment =
                ConfirmDeleteDialogFragment.newInstance(vehicleItem);
        confirmDeleteDialogFragment.setCancelable(false);
        confirmDeleteDialogFragment.show(getFragmentManager(), "CONFIRM_DELETE");
    }
}
