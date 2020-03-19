package com.diversedistractions.vehiclelog.models

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import com.diversedistractions.vehiclelog.database.VehiclesTable

class Vehicle(
        private var vehicleId:  Int, private var vehicleType: Int, private var vehicleMake: String?,
        private var vehicleModel: String?, private var vehicleYear: Long,
        private var vehicleVin: String?, private var vehicleLp: String?,
        private var vehicleLpRenewalDate: Long, private var vehicleImage: String?,
        private var vehicleNotes: String?, private var vehicleTdEfficiency: String?,
        private var vehicleModOrder: Int) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()
    )

    override fun toString(): String {
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
                ", vehicleNotes='" + vehicleNotes + '\'' +
                ", vehicleTdEfficiency='" + vehicleTdEfficiency + '\'' +
                ", vehicleModOrder='" + vehicleModOrder + '\'' +
                '}'
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(vehicleId)
        parcel.writeInt(vehicleType)
        parcel.writeString(vehicleMake)
        parcel.writeString(vehicleModel)
        parcel.writeLong(vehicleYear)
        parcel.writeString(vehicleVin)
        parcel.writeString(vehicleLp)
        parcel.writeLong(vehicleLpRenewalDate)
        parcel.writeString(vehicleImage)
        parcel.writeString(vehicleNotes)
        parcel.writeString(vehicleTdEfficiency)
        parcel.writeInt(vehicleModOrder)
    }

    fun toValues(): ContentValues? {
        val values = ContentValues()
        values.put(VehiclesTable.COL_VEHICLE_ID, vehicleId)
        values.put(VehiclesTable.COL_VEHICLE_TYPE, vehicleType)
        values.put(VehiclesTable.COL_VEHICLE_MAKE, vehicleMake)
        values.put(VehiclesTable.COL_VEHICLE_MODEL, vehicleModel)
        values.put(VehiclesTable.COL_VEHICLE_YEAR, vehicleYear)
        values.put(VehiclesTable.COL_VEHICLE_VIN, vehicleVin)
        values.put(VehiclesTable.COL_VEHICLE_LP, vehicleLp)
        values.put(VehiclesTable.COL_VEHICLE_REN_DATE, vehicleLpRenewalDate)
        values.put(VehiclesTable.COL_VEHICLE_IMAGE, vehicleImage)
        values.put(VehiclesTable.COL_VEHICLE_NOTE, vehicleNotes)
        values.put(VehiclesTable.COL_VEHICLE_TD_EFF, vehicleTdEfficiency)
        values.put(VehiclesTable.COL_VEHICLE_MODIFIED_ORDER, vehicleModOrder)
        return values
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vehicle> {
        override fun createFromParcel(parcel: Parcel): Vehicle {
            return Vehicle(parcel)
        }

        override fun newArray(size: Int): Array<Vehicle?> {
            return arrayOfNulls(size)
        }
    }

}