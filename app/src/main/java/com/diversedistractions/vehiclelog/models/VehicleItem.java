package com.diversedistractions.vehiclelog.models;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.diversedistractions.vehiclelog.database.VehiclesTable;

import java.util.UUID;

public class VehicleItem implements Parcelable {

    private String vehicleId;
    private String vehicleType;
    private String vehicleMake;
    private String vehicleModel;
    private int vehicleYear;
    private String vehicleVin;
    private String vehicleLp;
    private int vehicleLpRenewalDate;
    private String vehicleImage;
    private String vehicleTdMilage;
    private String vehicleNotes;

    public VehicleItem() {
    }

    public VehicleItem(String vehicleId, String vehicleType, String vehicleMake,
                       String vehicleModel, int vehicleYear, String vehicleVin, String vehicleLp,
                       int vehicleLpRenewalDate, String vehicleImage, String vehicleTdMilage,
                       String vehicleNotes) {
        if (vehicleId == null) {
            vehicleId = UUID.randomUUID().toString();
        }

        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.vehicleVin = vehicleVin;
        this.vehicleLp = vehicleLp;
        this.vehicleLpRenewalDate = vehicleLpRenewalDate;
        this.vehicleImage = vehicleImage;
        this.vehicleTdMilage = vehicleTdMilage;
        this.vehicleNotes = vehicleNotes;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public String getVehicleLp() {
        return vehicleLp;
    }

    public void setVehicleLp(String vehicleLp) {
        this.vehicleLp = vehicleLp;
    }

    public int getVehicleLpRenewalDate() {
        return vehicleLpRenewalDate;
    }

    public void setVehicleLpRenewalDate(int vehicleLpRenewalDate) {
        this.vehicleLpRenewalDate = vehicleLpRenewalDate;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleTdMilage() {
        return vehicleTdMilage;
    }

    public void setVehicleTdMilage(String vehicleTdMilage) {
        this.vehicleTdMilage = vehicleTdMilage;
    }

    public String getVehicleNotes() {
        return vehicleNotes;
    }

    public void setVehicleNotes(String vehicleNotes) {
        this.vehicleNotes = vehicleNotes;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();

        values.put(VehiclesTable.COL_VEHICLE_ROW_ID, vehicleId);
        values.put(VehiclesTable.COL_VEHICLE_TYPE, vehicleType);
        values.put(VehiclesTable.COL_VEHICLE_MAKE, vehicleMake);
        values.put(VehiclesTable.COL_VEHICLE_MODEL, vehicleModel);
        values.put(VehiclesTable.COL_VEHICLE_YEAR, vehicleYear);
        values.put(VehiclesTable.COL_VEHICLE_VIN, vehicleVin);
        values.put(VehiclesTable.COL_VEHICLE_LP, vehicleLp);
        values.put(VehiclesTable.COL_VEHICLE_REN_DATE, vehicleLpRenewalDate);
        values.put(VehiclesTable.COL_VEHICLE_IMAGE, vehicleImage);
        values.put(VehiclesTable.COL_VEHICLE_TD_MPG, vehicleTdMilage);
        values.put(VehiclesTable.COL_VEHICLE_NOTE, vehicleNotes);

        return values;
    }

    @Override
    public String toString() {
        return "VehicleItem{" +
                "vehicleId='" + vehicleId + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleMake='" + vehicleMake + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleYear=" + vehicleYear +
                ", vehicleVin='" + vehicleVin + '\'' +
                ", vehicleLp='" + vehicleLp + '\'' +
                ", vehicleLpRenewalDate=" + vehicleLpRenewalDate +
                ", vehicleImage='" + vehicleImage + '\'' +
                ", vehicleTdMilage='" + vehicleTdMilage + '\'' +
                ", vehicleNotes='" + vehicleNotes + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehicleId);
        dest.writeString(vehicleType);
        dest.writeString(vehicleMake);
        dest.writeString(vehicleModel);
        dest.writeInt(vehicleYear);
        dest.writeString(vehicleVin);
        dest.writeString(vehicleLp);
        dest.writeInt(vehicleLpRenewalDate);
        dest.writeString(vehicleImage);
        dest.writeString(vehicleTdMilage);
        dest.writeString(vehicleNotes);
    }

    protected VehicleItem(Parcel in) {
        vehicleId = in.readString();
        vehicleType = in.readString();
        vehicleMake = in.readString();
        vehicleModel = in.readString();
        vehicleYear = in.readInt();
        vehicleVin = in.readString();
        vehicleLp = in.readString();
        vehicleLpRenewalDate = in.readInt();
        vehicleImage = in.readString();
        vehicleTdMilage = in.readString();
        vehicleNotes = in.readString();
    }

    public static final Parcelable.Creator<VehicleItem> CREATOR =
            new Parcelable.Creator<VehicleItem>() {
        @Override
        public VehicleItem createFromParcel(Parcel source) {
            return new VehicleItem(source);
        }

        @Override
        public VehicleItem[] newArray(int size) {
            return new VehicleItem[size];
        }
    };



}
