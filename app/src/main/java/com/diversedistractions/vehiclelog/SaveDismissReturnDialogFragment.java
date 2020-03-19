package com.diversedistractions.vehiclelog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


/**
 * A simple {@link //Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaveDismissReturnDialogFragment.OnSaveDismissReturnInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaveDismissReturnDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveDismissReturnDialogFragment extends DialogFragment {

    private OnSaveDismissReturnInteractionListener mListener;

    public SaveDismissReturnDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SaveDismissReturnDialogFragment.
     */
    public static SaveDismissReturnDialogFragment newInstance() {
        return new SaveDismissReturnDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.do_you_want_to_save_your_changes));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSaveDismissReturnInteraction(VehicleDetailActivity.SAVE_EDIT);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.return_to_edit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSaveDismissReturnInteraction(VehicleDetailActivity.RETURN_TO_EDIT);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSaveDismissReturnInteraction(VehicleDetailActivity.CANCEL_EDIT);
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveDismissReturnInteractionListener) {
            mListener = (OnSaveDismissReturnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSaveDismissReturnInteractionListener {
        void onSaveDismissReturnInteraction(int choice);
    }
}
