package com.diversedistractions.vehiclelog

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

// Constant to make adding and retrieving vehicles from intents less prone to typos
internal const val VEHICLE_TRANSFER = "VEHICLE_TRANSFER"

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    internal fun activateToolbar(enableHome: Boolean) {

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}