package com.diversedistractions.vehiclelog.utilities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConversionHelper {

    public int getCurrentYearAsInteger() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public int getCurrentMonthAsInteger() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public long setIntegerYearToEpoch(int yr) {
        /*
         * Pull just the year out of the date and convert to epoch
         * time format as an integer for the vehicle model year
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.US);
        Date vy = null;
        try {
            vy = simpleDateFormat.parse(Integer.toString(yr));
        } catch (ParseException vye) {
            // TODO Auto-generated catch block
            vye.printStackTrace();
        }
        return vy != null ? vy.getTime() : 0;
    }

    public long setIntYearIntMonthToEpoch(int yr, int m) {
        /*
         * Pull the year and month out of the current date and convert to epoch
         * time format as an integer for the license plate renewal date
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy", Locale.US);
        Date rd = null;
        try {
            rd = simpleDateFormat.parse(Integer.toString(yr)+ "-" + Integer.toString(m));
        } catch (ParseException rde) {
            // TODO Auto-generated catch block
            rde.printStackTrace();
        }
        return rd != null ? rd.getTime() : 0;
    }

    public String getYearAsString(long d) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.US);
        return simpleDateFormat.format(d);
    }

    public String getYearMonthAsString(long d) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy", Locale.US);
        return simpleDateFormat.format(d);
    }
}
