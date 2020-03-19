package com.diversedistractions.vehiclelog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;


/**
 * A simple {@link //Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVehicleImageChoiceFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehicleImageChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleImageChoiceFragment extends DialogFragment {

    private OnVehicleImageChoiceFragmentInteractionListener mListener;

    public VehicleImageChoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleImageChoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleImageChoiceFragment newInstance() {
        VehicleImageChoiceFragment fragment = new VehicleImageChoiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.dialog_vehicle_image_choice, container, false);

        Button buttonOk = (Button) rootView.findViewById(R.id.btnImageChoiceOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int choice = 0;
                int selectedRadioButton = ((RadioGroup) rootView
                        .findViewById(R.id.rBtnGroupGetVehicleImage)).getCheckedRadioButtonId();
                switch (selectedRadioButton){
                    case R.id.rBtnAppIcon:
                        choice = VehicleDetailActivity.CHOOSE_APP_ICON;
                        break;
                    case R.id.rBtnChooseImage:
                        choice = VehicleDetailActivity.CHOOSE_IMAGE_ON_DEVICE;
                        break;
                    case R.id.rBtnTakePhoto:
                        choice = VehicleDetailActivity.TAKE_PHOTO;
                }
                if (mListener != null) {
                    mListener.onVehicleImageChoiceFragmentInteraction(choice);
                }
                dismiss();
            }
        });

        Button buttonCancel = (Button) rootView.findViewById(R.id.btnImageChoiceCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleImageChoiceFragmentInteractionListener) {
            mListener = (OnVehicleImageChoiceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleImageChoiceFragmentInteractionListener");
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
    public interface OnVehicleImageChoiceFragmentInteractionListener {
        void onVehicleImageChoiceFragmentInteraction(int choice);
    }
}
