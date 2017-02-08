package com.diversedistractions.vehiclelog.dummy;

import com.diversedistractions.vehiclelog.database.VehiclesTable;
import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContentProvider {
    public static List<VehicleItem> vehicleItemList;
    public static Map<String, VehicleItem> vehicleItemMap;

    static {
        vehicleItemList = new ArrayList<>();
        vehicleItemMap = new HashMap<>();

        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_CAR, "Lincoln", "Continental", 1984,
                "12345678901234", "123ABC", 123456789, "vi_car.png", "12.5", "message"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_MOTORCYCLE, "BMW", "R1200RT", 2009,
                "43210987654321", "456DEF", 123456789, "vi_motorcycle.png", "48.7", "Note"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_CAR, "Lamborghini",
                "Aventador SV Roadster", 1977, "78901234567890", "789GHI", 123456789, "vi_car.png",
                "2.2", "Help me, I'm in dept up to my eyeballs!"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_TRUCK, "Ford", "F150", 2010,
                "45678901234567", "012JKL", 123456789, "vi_truck.png", "14.8", "5W-30"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_CAR, "Chevrolet", "Camero Z28", 2017,
                "12345678901234", "123ABC", 123456789, "vi_car.png", "12.5", "message"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_TRUCK, "Chevrolet", "Silverado", 1969,
                "43210987654321", "456DEF", 123456789, "vi_truck.png", "48.7", "Note"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_CAR, "Ford",
                "Thunderbird", 1957, "78901234567890", "789GHI", 123456789, "vi_car.png",
                "2.2", "Can you push?"));
        addItem(new VehicleItem(null, VehiclesTable.VEHICLE_TYPE_MOTORCYCLE, "Kawasaki",
                "Vulcan 750", 2005, "45678901234567", "012JKL", 123456789, "vi_motorcycle.png",
                "14.8", "5W-30"));
    }

    private static void addItem(VehicleItem item) {
        vehicleItemList.add(item);
        vehicleItemMap.put(item.getVehicleId(), item);
    }
}
