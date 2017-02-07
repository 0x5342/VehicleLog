package com.diversedistractions.vehiclelog.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class JSONHelper {

    private static final String FILE_NAME = "vehiclesexport.json";
    private static final String TAG = "JSONHelper";

    public static boolean exportToJSON(Context context, List<VehicleItem> vehicleItemList) {

        VehicleItems vehicleData = new VehicleItems();
        vehicleData.setVehicleItems(vehicleItemList);

        Gson gson = new Gson();
        String jsonString = gson.toJson(vehicleData);

        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    public static List<VehicleItem> importFromJSON(Context context) {
        return null;
    }

    static class VehicleItems {
        List<VehicleItem> vehicleItems;

        public List<VehicleItem> getVehicleItems() {
            return vehicleItems;
        }

        public void setVehicleItems(List<VehicleItem> vehicleItems) {
            this.vehicleItems = vehicleItems;
        }
    }

}
