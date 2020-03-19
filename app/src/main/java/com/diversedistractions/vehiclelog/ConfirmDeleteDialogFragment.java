package com.diversedistractions.vehiclelog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.diversedistractions.vehiclelog.utilities.DateConversionHelper;

public class ConfirmDeleteDialogFragment extends DialogFragment {

    public static final String VEHICLE_KEY = "vehicle_key";
    DateConversionHelper dateConversionHelper;
    private ConfirmationListener mListener;

    public ConfirmDeleteDialogFragment() {}

    public static ConfirmDeleteDialogFragment newInstance (VehicleItem vehicleItem) {
        Bundle args = new Bundle();
        args.putParcelable(VEHICLE_KEY, vehicleItem);
        ConfirmDeleteDialogFragment confirmDeleteDialogFragment =
                new ConfirmDeleteDialogFragment();
        confirmDeleteDialogFragment.setArguments(args);
        return confirmDeleteDialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof ConfirmationListener) {
            mListener = (ConfirmationListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement ConfirmationListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmationListener) {
            mListener = (ConfirmationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ConfirmationListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dateConversionHelper = new DateConversionHelper();
        VehicleItem vehicleItem = getArguments().getParcelable(VEHICLE_KEY);
        String vYear = dateConversionHelper.getYearAsString(vehicleItem.getVehicleYear());
        String vMake = vehicleItem.getVehicleMake();
        String vModel = vehicleItem.getVehicleModel();

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.confirm_delete));
        alertDialog.setMessage(getString(R.string.sure_want_to_delete)+"\n"+
                vYear + " " + vMake + " " + vModel + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onConfirmVehicleDeleteInteraction(true);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onConfirmVehicleDeleteInteraction(false);
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }

    public interface ConfirmationListener {
        void onConfirmVehicleDeleteInteraction(boolean choice);
    }
}
