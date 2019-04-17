package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.osmdroid.config.Configuration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fragmentexercise1) {
            // react to the menu item being selected...
            Intent intent = new Intent(this, FragActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.fragmentexercise2) {

            Intent intent = new Intent(this, MapActivity.class);
            startActivityForResult(intent, 0);

            return true;
        } else if (item.getItemId() == R.id.submenuactivity) {

            Intent intent = new Intent(this, SubMenuActivity.class);
            startActivityForResult(intent, 0);

            return true;
        }
        return false;
    }
}
