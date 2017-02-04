package com.diversedistractions.vehiclelog;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>{

    public static final String VEHICLE_ID_KEY = "vehicle_id_key";
    public static final String VEHICLE_KEY = "vehicle_key";
    private List<VehicleItem> mItems;
    private Context mContext;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public VehicleItemAdapter(Context context, List<VehicleItem> items) {
        this.mContext = context;
        this.mItems = items;
    }


    @Override
    public VehicleItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View vehicleItemView = inflater.inflate(R.layout.vehicle_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(vehicleItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VehicleItemAdapter.ViewHolder holder, int position) {
        final VehicleItem vehicleItem = mItems.get(position);

        try {
            holder.vYear.setText(Integer.toString(vehicleItem.getVehicleYear()));
            holder.vMake.setText(vehicleItem.getVehicleMake());
            holder.vModel.setText(vehicleItem.getVehicleModel());
            String vehicleImageFile = vehicleItem.getVehicleImage();
            InputStream inputStream = mContext.getAssets().open(vehicleImageFile);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.vImage.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You selected: " + vehicleItem.getVehicleYear() + " " +
                        vehicleItem.getVehicleMake() + " " + vehicleItem.getVehicleModel(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long-clicked: " + vehicleItem.getVehicleYear() + " " +
                                vehicleItem.getVehicleMake() + " " + vehicleItem.getVehicleModel(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vYear;
        public TextView vMake;
        public TextView vModel;
        public ImageView vImage;
        public View mView;
        public ViewHolder (View vehicleItemView){
            super(vehicleItemView);

            vYear = (TextView) vehicleItemView.findViewById(R.id.vehicleYear);
            vMake = (TextView) vehicleItemView.findViewById(R.id.makeText);
            vModel = (TextView) vehicleItemView.findViewById(R.id.modelText);
            vImage = (ImageView) vehicleItemView.findViewById(R.id.vehicleImage);
            mView = vehicleItemView;
        }
    }
}
