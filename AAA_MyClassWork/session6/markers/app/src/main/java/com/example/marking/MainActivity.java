package com.example.marking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final double DEFAULT_LAT = 50.90;
    private static final double DEFAULT_LONG = -1.40;
    private static final int DEFAULT_ZOOM = 11;

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    ArrayList<OverlayItem> item = new ArrayList<OverlayItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.overlaymap);

        mv.getController().setZoom(DEFAULT_ZOOM);
        GeoPoint geo;
        mv.getController().setCenter(geo = new GeoPoint(DEFAULT_LAT, DEFAULT_LONG));

        TextView ScreenLat = (TextView) findViewById(R.id.tvlat);
        TextView ScreenLong = (TextView) findViewById(R.id.tvlon);
        TextView ScreenMap = (TextView) findViewById(R.id.tvmap);

        ScreenLat.setText(Double.toString(geo.getLatitude()));
        ScreenLong.setText(Double.toString(geo.getLongitude()));
        ScreenMap.setText(Integer.toString(mv.getZoomLevel()));

        try{
            FileReader fr = new FileReader("/sdcard/restaturants.txt");
            BufferedReader reader = new BufferedReader(fr);
            String line = "";
            while((line = reader.readLine()) != null){
                String[] components = line.split(",");
                if(components.length == 5){
                    double currentLat = Double.parseDouble(components[4]);
                    double currentLon = Double.parseDouble(components[3]);
                    String currentTitle = components[0];
                    String currentDesc = components[2];
                    String currentType = components[1];
                    OverlayItem currentItem = new OverlayItem(currentTitle, currentDesc, new GeoPoint(currentLat, currentLon));
                    Drawable marker =  typeToMarker(currentType);
                    if (marker!=null) currentItem.setMarker(marker);
                    item.add(currentItem);
                }
            }
            reader.close();
        }
        catch(IOException e){
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("ERROR " + e).show();
        }

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_LONG).show();
                return true;
            }
            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        OverlayItem oldtown = new OverlayItem("Old Town", "Tallinn's Most Famous Place", new GeoPoint(DEFAULT_LAT, DEFAULT_LONG));
        for (int i=0; i<item.size(); i++){
            items.addItem(item.get(i));
        }
        items.addItem(oldtown);
        mv.getOverlays().add(items);
    }

    private Drawable typeToMarker(String type){
        // NOTE is just this.getDrawable() if supporting API 21+ only
        Drawable marker = null;
        switch (type) {
            case "pub":  marker = getResources().getDrawable(R.drawable.pub);
                break;
            case "Fish and Chips":  marker = getResources().getDrawable(R.drawable.fishnchips);
                break;
            case "Chinese":  marker = getResources().getDrawable(R.drawable.chinese);
                break;
            case "Japanese":  marker = getResources().getDrawable(R.drawable.japanese);
                break;
            case "Thai":  marker = getResources().getDrawable(R.drawable.thai);
                break;
            case "French":  marker = getResources().getDrawable(R.drawable.french);
                break;
            case "Italian":  marker = getResources().getDrawable(R.drawable.italian);
                break;
            case "Pizza":  marker = getResources().getDrawable(R.drawable.pizza);
                break;
            case "Indian":  marker = getResources().getDrawable(R.drawable.indian);
                break;
            case "Mexican":  marker = getResources().getDrawable(R.drawable.mexican);
                break;
            case "General":  marker = getResources().getDrawable(R.drawable.general);
                break;

            default: marker = null;
                break;
        }

        return marker;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
