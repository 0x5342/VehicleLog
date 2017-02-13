package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.io.IOException;
import java.io.InputStream;

/**
 * A fragment representing a single Vehicle detail screen.
 * This fragment is either contained in a {@link VehicleListActivity}
 * in two-pane mode (on tablets) or a {@link VehicleDetailActivity}
 * on handsets.
 */
public class VehicleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item that this fragment
     * represents.
     */
    public static final String ARG_ITEM_URI = "item_uri";
    public static final String DETAIL_MODE = "detail_mode";
    public static final int DETAIL_IN_CREATE_MODE = 0;
    public static final int DETAIL_IN_VIEW_MODE = 1;
    public static final int DETAIL_IN_EDIT_MODE = 2;

    /**
     * The vehicle this fragment is presenting.
     */
    private VehicleItem vehicleItem;
    private int mPosition;
    private Uri mVehicleUri;
    private Context mContext;
    private String vehicleType;
    private String vehicleMake;
    private String vehicleModel;
    private int vehicleYear;
    private String vehicleVin;
    private String vehicleLp;
    private int vehicleLpRenewalDate;
    private String vehicleImage;
    private String vehicleNotes;
    private String vehicleTdEfficiency;
    private int vehicleModOrder;
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
        // Set the title in the appBar (if active) to match whether creating, viewing, or editing.
        if (appBarLayout != null) {
            switch (mMode){
                case 0:
                    appBarLayout.setTitle(getString(R.string.new_vehicle));
                    break;
                case 1:
                    appBarLayout.setTitle(getString(R.string.vehicle_view));
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
                ((EditText) rootView.findViewById(R.id.vehicleYearEditText))
                        .setText(Integer.toString(vehicleYear));
                ((EditText) rootView.findViewById(R.id.makeEditText)).setText(vehicleMake);
                ((EditText) rootView.findViewById(R.id.modelEditText)).setText(vehicleModel);
                try {
                    InputStream inputStream = getContext().getAssets().open(vehicleImage);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    ((ImageButton) rootView.findViewById(R.id.vehicleImageButton))
                            .setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((EditText) rootView.findViewById(R.id.vehicleVinEditText)).setText(vehicleVin);
                ((EditText) rootView.findViewById(R.id.vehicleLicensePlateEditText))
                        .setText(vehicleLp);
                ((EditText) rootView.findViewById(R.id.vehicleLpRenewalDateEditText))
                        .setText(Integer.toString(vehicleLpRenewalDate));
                ((EditText) rootView.findViewById(R.id.vehNotesEditText)).setText(vehicleNotes);
                break;
            case DETAIL_IN_VIEW_MODE:
                rootView = inflater.inflate(R.layout.vehicle_detail_view, container, false);
                // Show the vehicle's content
                ((TextView) rootView.findViewById(R.id.vehicleYearText))
                        .setText(Integer.toString(vehicleYear));
                ((TextView) rootView.findViewById(R.id.makeText)).setText(vehicleMake);
                ((TextView) rootView.findViewById(R.id.modelText)).setText(vehicleModel);
                try {
                    InputStream inputStream = getContext().getAssets().open(vehicleImage);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    ((ImageView) rootView.findViewById(R.id.vehicleImage))
                            .setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((TextView) rootView.findViewById(R.id.vehicleVinText)).setText(vehicleVin);
                ((TextView) rootView.findViewById(R.id.vehicleLicensePlateText))
                        .setText(vehicleLp);
                ((TextView) rootView.findViewById(R.id.vehicleLpRenewalDateText))
                        .setText(Integer.toString(vehicleLpRenewalDate));
                ((TextView) rootView.findViewById(R.id.vehToDateText))
                        .setText(vehicleTdEfficiency);
                ((TextView) rootView.findViewById(R.id.vehNotesText)).setText(vehicleNotes);
                break;
        }
        return rootView;
    }

    private void getVehicleData (Uri uri) {
        Cursor cursor = mContext.getContentResolver().query
                (uri, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            vehicleType = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TYPE));
            vehicleMake = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MAKE));
            vehicleModel = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODEL));
            vehicleYear = cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_YEAR));
            vehicleVin = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_VIN));
            vehicleLp = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_LP));
            vehicleLpRenewalDate = cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_REN_DATE));
            vehicleImage = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_IMAGE));
            vehicleTdEfficiency = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_TD_EFF));
            vehicleNotes = cursor.getString
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_NOTE));
            vehicleModOrder = cursor.getInt
                    (cursor.getColumnIndex(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER));
            cursor.close();
        }
    }
}
