package com.diversedistractions.vehiclelog.utilities

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diversedistractions.vehiclelog.R
import com.diversedistractions.vehiclelog.database.VehiclesTable
import com.diversedistractions.vehiclelog.utilities.IconFromAssetsFragment.IconItem
import com.diversedistractions.vehiclelog.utilities.IconFromAssetsFragment.OnListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display a [IconItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class IconFromAssetsRecyclerViewAdapter(var dialog: Dialog, private val mValues: List<IconItem>,
                                        private val mListener: OnListFragmentInteractionListener?) :
                            RecyclerView.Adapter<IconFromAssetsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_iconfromassets_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        try {
            holder.mImageView.setImageDrawable(Drawable.createFromStream(holder.mImageView.context.assets
                    .open(VehiclesTable.VEHICLE_ICONS_FOLDER + mValues[position].content), null))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.mView.setOnClickListener {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem!!.content)
                dialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImageView: ImageView
        var mItem: IconItem? = null

        init {
            mImageView = mView.findViewById<View>(R.id.imageViewIconFromAssets) as ImageView
        }
    }

}