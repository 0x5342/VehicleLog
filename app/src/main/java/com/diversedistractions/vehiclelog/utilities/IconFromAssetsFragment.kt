package com.diversedistractions.vehiclelog.utilities

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diversedistractions.vehiclelog.R
import java.io.IOException
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class IconFromAssetsFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : DialogFragment() {
    private var mListener: OnListFragmentInteractionListener? = null
    var icons: Array<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        icons = assetsFolderIcons
        val numFiles = icons!!.size
        ICONS.clear()
        for (i in 1..numFiles) {
            addItem(createIconItem(i))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        val view = inflater.inflate(R.layout.fragment_iconfromassets_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = IconFromAssetsRecyclerViewAdapter(dialog,
                    ICONS, mListener)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(imageString: String?)
    }

    private val assetsFolderIcons: Array<String>?
        private get() {
            val icons: Array<String>?
            icons = try {
                activity.assets.list("icons")
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return icons
        }

    /**
     * An icon item representing an icon location.
     */
    class IconItem(val id: String, val content: String)

    private fun addItem(item: IconItem) {
        ICONS.add(item)
        ICON_MAP[item.id] = item
    }

    private fun createIconItem(position: Int): IconItem {
        return IconItem(position.toString(), icons!![position - 1])
    }

    companion object {
        /**
         * An array of icon items.
         */
        val ICONS: MutableList<IconItem> = ArrayList()

        /**
         * A map of icon link items, by ID.
         */
        val ICON_MAP: MutableMap<String, IconItem> = HashMap()
        @JvmStatic
        fun newInstance(): IconFromAssetsFragment {
            return IconFromAssetsFragment()
        }
    }
}