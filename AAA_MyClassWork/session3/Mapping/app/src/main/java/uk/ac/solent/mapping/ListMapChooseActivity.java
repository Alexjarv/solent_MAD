package uk.ac.solent.mapping;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListMapChooseActivity extends ListActivity {
    String[] names, details;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new String[] {"Regular Map", "Hike/Bike Map"};
        details = new String[] {"regular map", "extra detailed map"};
        MyAdapter adapter = new MyAdapter();
        setListAdapter(adapter);

    }
    public void onListItemClick(ListView lv, View view, int index, long id){
        //sss
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        boolean hikebikemap = false;
        if (index == 1) {
            hikebikemap = true;
        } else if (index == 2) {
            hikebikemap = false;
        }
        bundle.putBoolean("com.example.hikebikemap", hikebikemap);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
    }

    public class MyAdapter extends ArrayAdapter<String>{
        public MyAdapter() {
            // We have to use ExampleListActivity.this to refer to the outer class (the activity)
            super(ListMapChooseActivity.this, android.R.layout.simple_list_item_1, names);
        }
        public View getView(int index, View convertView, ViewGroup parent){
            View view = convertView;
            if(view==null)
            {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.poientry, parent, false);
            }
            TextView title = (TextView)view.findViewById(R.id.poi_name), detail =
                    (TextView)view.findViewById(R.id.poi_desc);
            title.setText(names[index]);
            detail.setText(details[index]);
            return view;
        }
    }
}
