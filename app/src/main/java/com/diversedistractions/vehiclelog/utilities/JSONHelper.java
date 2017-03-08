package com.diversedistractions.vehiclelog.utilities;

import android.content.Context;
import android.os.Environment;

import com.diversedistractions.vehiclelog.models.VehicleItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JSONHelper {

    private static final String FILE_NAME = "vehiclesexport.json";
    private static final String TAG = "JSONHelper";

    public static boolean exportToJSON(Context context, List<VehicleItem> vehicleItemList) {

        VehicleItems vehicleData = new VehicleItems();

        vehicleData.setVehicleItems(vehicleItemList);

        //TODO: get all fuel entries from content provider and make a list
        //TODO: get all service entries from content provider and make a list

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

        FileReader reader = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            reader = new FileReader(file);
            Gson gson = new Gson();
            VehicleItems vehicleItems = gson.fromJson(reader, VehicleItems.class);
            return vehicleItems.getVehicleItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
