package com.diversedistractions.vehiclelog.utilities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;

import com.diversedistractions.vehiclelog.R;
import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.lang.reflect.Field;
import java.util.Calendar;

public class CustomDatePickerDialogFragment extends DialogFragment {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String SHOW_DAY = "show_day";
    public static final String SHOW_MONTH = "show_month";
    public static final String SHOW_YEAR = "show_year";
    public static final String VEHICLE_KEY = "vehicle_key";
    public static final String DATE_FIELD = "date_field";
    private int day;
    private int month;
    private int year;
    private boolean showDay;
    private boolean showMonth;
    private boolean showYear;

    public static CustomDatePickerDialogFragment newInstance(VehicleItem vehicleItem,
                                                             String dateField){
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putParcelable(VEHICLE_KEY, vehicleItem);
        args.putString(DATE_FIELD, dateField);
        CustomDatePickerDialogFragment fragment = new CustomDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.custom_date_picker, container, false);

        VehicleItem vehicleItem = getArguments().getParcelable(VEHICLE_KEY);
        String dateField = getArguments().getString(DATE_FIELD);

        // Get an instance of calendar with today's date
        Calendar c = Calendar.getInstance();
        switch (dateField) {
            case VehiclesTable.COL_VEHICLE_YEAR: // Set the variables needed for the vehicle year
                // If this is modifying a vehicle, use the saved date
                if (vehicleItem != null) {c.setTimeInMillis(vehicleItem.getVehicleYear());}
                showDay = false;
                showMonth = false;
                showYear = true;
                break;
            case VehiclesTable.COL_VEHICLE_REN_DATE: // Set the variables for the LP renewal date
                // If this is modifying a vehicle, use the saved date
                if (vehicleItem != null) {c.setTimeInMillis(vehicleItem.getVehicleLpRenewalDate());}
                showDay = false;
                showMonth = true;
                showYear = true;
                break;
        }
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Button buttonSave = (Button) rootView.findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button buttonCancel = (Button) rootView.findViewById(R.id.btnCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.customDatePicker);
        datePicker.init(year,month,day,null);
        // Modify the DatePicker to only show the fields desired
        initCustomDatePicker(datePicker, showDay, showMonth, showYear);

        return rootView;
    }

    /**
     * Initiates a custom version of DatePicker that allows one to hide the day, month, or year
     * portion of the DatePicker. This needs to be handled differently between the Lollipop version
     * of Android on and versions previous to Lollipop. The build version is checked and appropriate
     * code executed to hide or show desired fields.
     * @param dp: the datePicker that this will modify
     * @param showDay: a boolean to choose whether to show the day portion of the DatePicker
     * @param showMonth: a boolean to choose whether to show the month portion of the DatePicker
     * @param showYear: a boolean to choose whether to show the year portion of the DatePicker
     */
    public void initCustomDatePicker(DatePicker dp, boolean showDay,
                                     boolean showMonth, boolean showYear){
        int year    = dp.getYear();
        int month   = dp.getMonth();
        int day     = dp.getDayOfMonth();

        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                long dateAsEpoch = dateConversionHelper.setIntYearIntMonthToEpoch(year,monthOfYear+1);
//
//                mVehicleLpRenewalDate = dateConversionHelper.getYearMonthAsString(dateAsEpoch);
//                button.setText(mVehicleLpRenewalDate);
//                month_i = monthOfYear + 1;
//                Log.e("selected month:", Integer.toString(month_i));
                //Add whatever you need to handle Date changes
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int daySpinnerId = Resources.getSystem().getIdentifier(DAY, "id", "android");
            if (daySpinnerId != 0)
            {
                View daySpinner = dp.findViewById(daySpinnerId);
                if (daySpinner != null)
                {
                    if (showDay) {
                        daySpinner.setVisibility(View.VISIBLE);
                    } else {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            }

            int monthSpinnerId = Resources.getSystem().getIdentifier(MONTH, "id", "android");
            if (monthSpinnerId != 0)
            {
                View monthSpinner = dp.findViewById(monthSpinnerId);
                if (monthSpinner != null)
                {
                    if (showMonth) {
                        monthSpinner.setVisibility(View.VISIBLE);
                    } else {
                        monthSpinner.setVisibility(View.GONE);
                    }
                }
            }

            int yearSpinnerId = Resources.getSystem().getIdentifier(YEAR, "id", "android");
            if (yearSpinnerId != 0)
            {
                View yearSpinner = dp.findViewById(yearSpinnerId);
                if (yearSpinner != null)
                {
                    if (showYear) {
                        yearSpinner.setVisibility(View.VISIBLE);
                    } else {
                        yearSpinner.setVisibility(View.GONE);
                    }
                }
            }
        } else { //Older SDK versions
            Field f[] = dp.getClass().getDeclaredFields();
            for (Field field : f)
            {
                if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner"))
                {
                    field.setAccessible(true);
                    Object dayPicker = null;
                    try {
                        dayPicker = field.get(dp);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (showDay) {
                        ((View) dayPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }

                if(field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner"))
                {
                    field.setAccessible(true);
                    Object monthPicker = null;
                    try {
                        monthPicker = field.get(dp);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (showMonth) {
                        ((View) monthPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) monthPicker).setVisibility(View.GONE);
                    }
                }

                if(field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner"))
                {
                    field.setAccessible(true);
                    Object yearPicker = null;
                    try {
                        yearPicker = field.get(dp);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (showYear) {
                        ((View) yearPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) yearPicker).setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}