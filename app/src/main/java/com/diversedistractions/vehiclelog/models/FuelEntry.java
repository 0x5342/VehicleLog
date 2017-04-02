package com.diversedistractions.vehiclelog.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FuelEntry implements Parcelable {

    private int fuelEntryId;
    private int vehicleId;
    private long fuelEntryDateEpoch;
    private float meter;
    private float quantity;
    private float cost;
    private boolean partialTank;
    private String fuelEntryEfficiency;

    public FuelEntry() {
    }

    public FuelEntry(int fuelEntryId, int vehicleId, long fuelEntryDateEpoch, float meter,
                     float quantity, float cost, boolean partialTank, String fuelEntryEfficiency) {
        this.fuelEntryId = fuelEntryId;
        this.vehicleId = vehicleId;
        this.fuelEntryDateEpoch = fuelEntryDateEpoch;
        this.meter = meter;
        this.quantity = quantity;
        this.cost = cost;
        this.partialTank = partialTank;
        this.fuelEntryEfficiency = fuelEntryEfficiency;
    }

    public int getFuelEntryId() {
        return fuelEntryId;
    }

    public void setFuelEntryId(int fuelEntryId) {
        this.fuelEntryId = fuelEntryId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getFuelEntryDateEpoch() {
        return fuelEntryDateEpoch;
    }

    public void setFuelEntryDateEpoch(long fuelEntryDateEpoch) {
        this.fuelEntryDateEpoch = fuelEntryDateEpoch;
    }

    public float getMeter() {
        return meter;
    }

    public void setMeter(float meter) {
        this.meter = meter;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public boolean isPartialTank() {
        return partialTank;
    }

    public void setPartialTank(boolean partialTank) {
        this.partialTank = partialTank;
    }

    public String getFuelEntryEfficiency() {
        return fuelEntryEfficiency;
    }

    public void setFuelEntryEfficiency(String fuelEntryEfficiency) {
        this.fuelEntryEfficiency = fuelEntryEfficiency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fuelEntryId);
        dest.writeInt(this.vehicleId);
        dest.writeLong(this.fuelEntryDateEpoch);
        dest.writeFloat(this.meter);
        dest.writeFloat(this.quantity);
        dest.writeFloat(this.cost);
        dest.writeByte(this.partialTank ? (byte) 1 : (byte) 0);
        dest.writeString(this.fuelEntryEfficiency);
    }

    protected FuelEntry(Parcel in) {
        this.fuelEntryId = in.readInt();
        this.vehicleId = in.readInt();
        this.fuelEntryDateEpoch = in.readLong();
        this.meter = in.readFloat();
        this.quantity = in.readFloat();
        this.cost = in.readFloat();
        this.partialTank = in.readByte() != 0;
        this.fuelEntryEfficiency = in.readString();
    }

    public static final Parcelable.Creator<FuelEntry> CREATOR = new Parcelable.Creator<FuelEntry>() {
        @Override
        public FuelEntry createFromParcel(Parcel source) {
            return new FuelEntry(source);
        }

        @Override
        public FuelEntry[] newArray(int size) {
            return new FuelEntry[size];
        }
    };
}
