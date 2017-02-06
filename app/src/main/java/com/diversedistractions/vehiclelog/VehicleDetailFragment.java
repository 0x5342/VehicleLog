package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diversedistractions.vehiclelog.dummy.DummyContentProvider;
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
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "vehicle_item";

    /**
     * The vehicle this fragment is presenting.
     */
    private VehicleItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getString(R.string.vehicle_view));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vehicle_detail_view, container, false);

        // Show the dummy content
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.vehicleYearText))
                    .setText(Integer.toString(mItem.getVehicleYear()));
            ((TextView) rootView.findViewById(R.id.makeText)).setText(mItem.getVehicleMake());
            ((TextView) rootView.findViewById(R.id.modelText)).setText(mItem.getVehicleModel());
            try {
                String vehicleImageFile = mItem.getVehicleImage();
                InputStream inputStream = getContext().getAssets().open(vehicleImageFile);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                ((ImageView) rootView.findViewById(R.id.vehicleImage)).setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.vehicleVinText)).setText(mItem.getVehicleVin());
            ((TextView) rootView.findViewById(R.id.vehicleLicensePlateText))
                    .setText(mItem.getVehicleLp());
            ((TextView) rootView.findViewById(R.id.vehicleLpRenewalDateText))
                    .setText(Integer.toString(mItem.getVehicleLpRenewalDate()));
            ((TextView) rootView.findViewById(R.id.vehToDateText)).setText(mItem.getVehicleTdMilage());
            ((TextView) rootView.findViewById(R.id.vehNotesText)).setText(mItem.getVehicleNotes());
        }

        return rootView;
    }
}
