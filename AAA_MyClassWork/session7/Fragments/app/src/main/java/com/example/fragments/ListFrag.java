package com.example.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListFrag extends ListFragment
{
    String[] entries =  { "Tim Berners-Lee", "John Lennon", "Linus Torvalds", "Barack Obama"},
            entryValues = { "Inventor of the World Wide Web.", "Singer and songwriter from the Beatles whose life was cut tragically short in 1980.",
                    "Original developer of Linux.", "Current president of the US." };

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this.getActivity(),  android.R.layout.simple_list_item_1, entries);
        setListAdapter(adapter);
    }


    public void onListItemClick(ListView lv, View v, int index, long id)
    {
        FragActivity activity = (FragActivity)getActivity();
        activity.receiveText(entryValues[index]);
    }
}
