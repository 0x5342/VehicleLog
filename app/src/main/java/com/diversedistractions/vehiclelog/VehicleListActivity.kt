package com.diversedistractions.vehiclelog

/**
 * This is the main activity.
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.diversedistractions.vehiclelog.utilities.VehicleRecyclerViewAdapter
import kotlinx.android.synthetic.main.content_vehicle_list.*

// Tag for troubleshooting in Logcat
private const val TAG = "VehicleListActivity" //TODO: remove for final version

private val vehicleRecyclerViewAdapter = VehicleRecyclerViewAdapter(ArrayList())

class VehicleListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called") //TODO: remove for final version
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)

        activateToolbar(false)

        val fab: View = findViewById(R.id.fabNewVehicle)
        fab.setOnClickListener { view ->
            Log.d(TAG, "fabNewVehicle pressed")
            val intent = Intent(this, VehicleDetailActivity::class.java)
            intent.putExtra(VehicleDetailFragment.DETAIL_MODE,
                    VehicleDetailFragment.DETAIL_IN_CREATE_MODE)

            startActivity(intent)
        }

        vehicle_recycler_view.layoutManager = LinearLayoutManager(this)
//        vehicle_recycler_view.addOnItemTouchListener(VehicleListRecyclerItemClickListener(this, vehicle_recycler_view, this))
        vehicle_recycler_view.adapter = vehicleRecyclerViewAdapter

        Log.d(TAG, "onCreate ends") //TODO: remove for final version
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vehicle_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                // Show the settings screen
                val settingsIntent = Intent(this, PreferencesActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            R.id.action_export -> {
                //TODO: Create this code
//                loaderManager.initLoader<Cursor>(VehicleListActivity.JSON_LOADER, null, this)
                return true
            }
            R.id.action_import ->                 //TODO: Correct this code to use the content provider
//                List<VehicleItem> vehicleItems = JSONHelper.importFromJSON(this);
//                if (vehicleItems != null) {
//                    for (VehicleItem vehicleItem: vehicleItems) {
//                        //TODO: code to verify data format and import into the database
//                    }
//                }
                return true
        }
        return super.onOptionsItemSelected(item)
    }

}