package com.diversedistractions.vehiclelog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diversedistractions.vehiclelog.models.Vehicle
import com.squareup.picasso.Picasso

class VehicleListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var vehicleImage: ImageView = view.findViewById(R.id.vehicleImage)
    var vehicleYear: TextView = view.findViewById(R.id.vehicleYear)
    var makeText: TextView = view.findViewById(R.id.makeText)
    var modelText: TextView = view.findViewById(R.id.modelText)
}

class VehicleRecyclerViewAdapter(private var vehicleList: List<Vehicle>)
    : RecyclerView.Adapter<VehicleListItemViewHolder>() {
    private val TAG = "VehicleRecyclerVA"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleListItemViewHolder {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, ".onCreateViewHolder called") //TODO: remove for final version
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_list_item, parent, false)
        return VehicleListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, ".getItemCount called") //TODO: remove for final version
        return if (vehicleList.isNotEmpty()) vehicleList.size else 0
    }

    override fun onBindViewHolder(holder: VehicleListItemViewHolder, position: Int) {
        // Called by the layout manager when it wants new data in an existing view
        val vehicleItem = vehicleList[position]
        Log.d(TAG, ".onBindViewHolder: ${vehicleItem.vehicleModel} --> $position") //TODO: remove for final version
        Picasso.with(holder.vehicleImage.context).load(vehicleItem.vehicleImage)
        holder.vehicleYear.text = vehicleItem.vehicleYear.toString()
        holder.makeText.text = vehicleItem.vehicleMake
        holder.modelText.text = vehicleItem.vehicleModel
    }
}