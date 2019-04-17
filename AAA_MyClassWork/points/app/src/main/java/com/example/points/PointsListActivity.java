package com.example.points;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import java.util.ArrayList;

public class PointsListActivity extends ListFragment {

        public void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ArrayList<String> entry_name = savedInstanceState.getStringArrayList("entry");
            double[] value_lat = savedInstanceState.getDoubleArray("value_lat");
            double [] value_long = savedInstanceState.getDoubleArray("value_long");

            ArrayAdapter<String> adapter = new ArrayAdapter<String> (this.getActivity(),  android.R.layout.simple_list_item_1, entry_name);
            setListAdapter(adapter);
        }


        public void onListItemClick(ListView lv, View v, int index, long id)
        {
            //map fragment

        }

}
