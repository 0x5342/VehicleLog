package com.diversedistractions.vehiclelog.utilities;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diversedistractions.vehiclelog.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class IconFromAssetsFragment extends DialogFragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * An array of icon items.
     */
    public static final List<IconItem> ICONS = new ArrayList<IconItem>();
    /**
     * A map of icon link items, by ID.
     */
    public static final Map<String, IconItem> ICON_MAP = new HashMap<String, IconItem>();
    String[] icons = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IconFromAssetsFragment() {
    }

    public static IconFromAssetsFragment newInstance() {
        IconFromAssetsFragment fragment = new IconFromAssetsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        icons = getAssetsFolderIcons();
        int numFiles = icons.length;

        ICONS.clear();
        for (int i = 1; i <= numFiles; i++) {
            addItem(createIconItem(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iconfromassets_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new IconFromAssetsRecyclerViewAdapter(getDialog(),
                    ICONS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String imageString);
    }

    private String[] getAssetsFolderIcons(){
        String[] icons;
        try {
            icons = getActivity().getAssets().list("icons");
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return icons;
    }

    /**
     * An icon item representing an icon location.
     */
    public static class IconItem {
        public final String id;
        public final String content;

        public IconItem(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }
    private void addItem(IconItem item) {
        ICONS.add(item);
        ICON_MAP.put(item.id, item);
    }
    private IconItem createIconItem(int position) {
        return new IconItem(String.valueOf(position), icons[position-1]);
    }
}
