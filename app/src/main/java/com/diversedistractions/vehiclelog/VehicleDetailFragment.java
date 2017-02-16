package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * A fragment representing a single Vehicle detail screen.
 * This fragment is either contained in a {@link VehicleListActivity}
 * in two-pane mode (on tablets) or a {@link VehicleDetailActivity}
 * on handsets.
 */
public class VehicleDetailFragment extends Fragment {

    public static final String ARG_ITEM_URI = "item_uri";
    public static final String DETAIL_MODE = "detail_mode";
    public static final int DETAIL_IN_CREATE_MODE = 0;
    public static final int DETAIL_IN_VIEW_MODE = 1;
    public static final int DETAIL_IN_EDIT_MODE = 2;

    private Uri mVehicleUri;
    private Context mContext;
    private int mVehicleTypePosition = 0;
    private String mVehicleMake;
    private String mVehicleModel;
    private String mVehicleYear;
    private String mVehicleVin;
    private String mVehicleLp;
    private String mVehicleLpRenewalDate;
    private String mVehicleImage;
    private String mVehicleNotes;
    private String mVehicleTdEfficiency;
    private int mVehicleModOrder;
    private int mMode;
    DateConversionHelper dateConversionHelper;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;

        switch (mMode) {
            case DETAIL_IN_CREATE_MODE:
                rootView = inflater.inflate(R.layout.vehicle_detail_edit, container, false);
                break;
            case DETAIL_IN_EDIT_MODE:
                rootView = inflater.inflate(R.layout.vehicle_detail_edit, container, false);
                // Show the vehicle's content in edit mode
                ((Spinner) rootView.findViewById(R.id.vehicleTypeSpinner))
                        .setSelection(mVehicleTypePosition);
                ((EditText) rootView.findViewById(R.id.vehicleYearEditText))
                        .setText(mVehicleYear);
                ((EditText) rootView.findViewById(R.id.makeEditText)).setText(mVehicleMake);
                ((EditText) rootView.findViewById(R.id.modelEditText)).setText(mVehicleModel);
                try {
                    InputStream inputStream = getContext().getAssets().open(mVehicleImage);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    ((ImageButton) rootView.findViewById(R.id.vehicleImageButton))
                            .setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((EditText) rootView.findViewById(R.id.vehicleVinEditText)).setText(mVehicleVin);
                if (mVehicleTypePosition==0) {
                    ((EditText) rootView.findViewById(R.id.vehicleLicensePlateEditText))
                            .setText(mVehicleLp);
                    ((EditText) rootView.findViewById(R.id.vehicleLpRenewalDateEditText))
                            .setText(mVehicleLpRenewalDate);
                }
                if (mVehicleNotes != null && mVehicleNotes.length() > 0) {
                    ((EditText) rootView.findViewById(R.id.vehNotesEditText)).setText(mVehicleNotes);
                }
                break;
            case DETAIL_IN_VIEW_MODE:
                rootView = inflater.inflate(R.layout.vehicle_detail_view, container, false);
                // Show the vehicle's content
                ((TextView) rootView.findViewById(R.id.vehicleYearText))
                        .setText(mVehicleYear);
                ((TextView) rootView.findViewById(R.id.makeText)).setText(mVehicleMake);
                ((TextView) rootView.findViewById(R.id.modelText)).setText(mVehicleModel);
                try {
                    InputStream inputStream = getContext().getAssets().open(mVehicleImage);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    ((ImageView) rootView.findViewById(R.id.vehicleImage))
                            .setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((TextView) rootView.findViewById(R.id.vehicleVinText)).setText(mVehicleVin);
                if (mVehicleTypePosition==0) {
                    ((TextView) rootView.findViewById(R.id.vehicleLicensePlateText))
                            .setText(mVehicleLp);
                    ((TextView) rootView.findViewById(R.id.vehicleLpRenewalDateText))
                            .setText(mVehicleLpRenewalDate);
                }
                if (mVehicleTdEfficiency != null) {
                    ((TextView) rootView.findViewById(R.id.vehToDateText))
                            .setText(mVehicleTdEfficiency);
                }
                if (mVehicleNotes != null && mVehicleNotes.length() > 0) {
                    ((TextView) rootView.findViewById(R.id.vehNotesText)).setText(mVehicleNotes);
                    break;
                }
        }
        return rootView;
    }

    private void getVehicleData (Uri uri) {
        Cursor cursor = mContext.getContentResolver().query
                (uri, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mVehicleTypePosition = cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TYPE));
            mVehicleMake = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE));
            mVehicleModel = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL));
            mVehicleYear = dateConversionHelper.getYearAsString
                    (cursor.getLong(cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR)));
            mVehicleVin = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_VIN));
            mVehicleLp = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_LP));
            mVehicleLpRenewalDate = dateConversionHelper.getYearMonthAsString(cursor.getLong
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_REN_DATE)));
            mVehicleImage = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE));
            mVehicleTdEfficiency = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TD_EFF));
            mVehicleNotes = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_NOTE));
            mVehicleModOrder = cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER));
            cursor.close();
        }
    }
}
