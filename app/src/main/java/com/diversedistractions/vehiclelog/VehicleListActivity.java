package com.diversedistractions.vehiclelog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.diversedistractions.vehiclelog.dummy.DummyContentProvider;
import com.diversedistractions.vehiclelog.models.VehicleItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VehicleListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    List<VehicleItem> vehicleItemList = DummyContentProvider.vehicleItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.vehicle_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.vehicle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new VehicleItemAdapter(this, vehicleItemList));
    }

    public class VehicleItemAdapter extends RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>{

        private final List<VehicleItem> mItems;
        private Context mContext;

        public VehicleItemAdapter(Context context, List<VehicleItem> items) {
            mItems = items;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View vehicleItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vehicle_list_item, parent, false);
            return new ViewHolder(vehicleItemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final VehicleItem vehicleItem = mItems.get(position);
//            holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);

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
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(VehicleDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                        VehicleDetailFragment fragment = new VehicleDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.vehicle_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, VehicleDetailActivity.class);
//                        intent.putExtra(VehicleDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                        context.startActivity(intent);
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView vYear;
            public TextView vMake;
            public TextView vModel;
            public ImageView vImage;
            public View mView;
//            public DummyContentProvider.DummyItem mItem;

            public ViewHolder(View vehicleItemView) {
                super(vehicleItemView);
                vYear = (TextView) vehicleItemView.findViewById(R.id.vehicleYear);
                vMake = (TextView) vehicleItemView.findViewById(R.id.makeText);
                vModel = (TextView) vehicleItemView.findViewById(R.id.modelText);
                vImage = (ImageView) vehicleItemView.findViewById(R.id.vehicleImage);
                mView = vehicleItemView;
            }

//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
        }
    }
}
