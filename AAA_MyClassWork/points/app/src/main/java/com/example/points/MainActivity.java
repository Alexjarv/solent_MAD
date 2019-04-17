package com.example.points;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String getURL = "http://www.free-map.org.uk/course/ws/get.php";
    private static final String postURL = "http://www.free-map.org.uk/course/ws/add.php";
    private static final Double DEFAULT_LAT = 50.9035;
    private static final Double DEFAULT_LON = -1.4042;
    private static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/points.csv";

    ItemizedIconOverlay<OverlayItem> items; //array of overlay items
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGesturerListener; //gesture listener variable
    OverlayItem currentLocation; //overlay item for current location.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        // set layout
        setContentView(R.layout.activity_main); //sets the layout of the application for main activity

        // check permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

        } else {
            // Permission has already been granted
        }



        // set location manager
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        Location newLocation = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // set gesture listener
        markerGesturerListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() + " : " + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, "Type: " + item.getUid(), Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        // set overlays
        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGesturerListener);
        loadFile(filePath); //download points

        //map fragment
        MapFragment mapfragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        mapfragment.setOverlays(items);

        //add current location from getLastKnownLocation (location manager).
        if (newLocation != null){
            // got a location, setting a new point.
            Double lat = newLocation.getLatitude();
            Double lon = newLocation.getLongitude();
            mapfragment.setLocation(lat,lon);
            currentLocation = new OverlayItem("Me", "You are here", "Now", new GeoPoint(lat, lon)); //create a new overlay item with the location of a new geo point.
            currentLocation.setMarker(typeToMarker("me")); //set marker for current location
            items.addItem(currentLocation); //add it
        } else {
            //cannot get a location, setting default values.
            mapfragment.setLocation(DEFAULT_LAT, DEFAULT_LON); //set lat and long to a map
            currentLocation = new OverlayItem("Me", "Default location", "Southampton", new GeoPoint(DEFAULT_LAT, DEFAULT_LON)); //create a new overlay item with the location of a new geo point.
            currentLocation.setMarker(typeToMarker("me")); //set marker for the new point. pass the type to the function.
            items.addItem(currentLocation); //add it
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        saveFile(filePath);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        saveFile(filePath);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
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

    //activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(currentLocation != null){
                    Bundle extras = intent.getExtras();
                    assert extras != null;
                    String name_in = extras.getString("point_name");
                    String type_in = extras.getString("point_type");
                    String description_in = extras.getString("point_description");
                    String latitude_in = String.valueOf(currentLocation.getPoint().getLatitude()); //String variable for autoupload
                    String longitude_in = String.valueOf(currentLocation.getPoint().getLongitude()); //String variable for autoupload

                    OverlayItem point; // Declare a variable.
                    point = new OverlayItem(type_in, name_in, description_in, new GeoPoint(currentLocation.getPoint().getLatitude(), currentLocation.getPoint().getLongitude())); //create a new point
                    // with variables from last activity.
                    point.setMarker(typeToMarker(type_in)); //set marker for the new point. pass the type to the function.
                    items.addItem(point); //add a new point to the items array.
                    Toast.makeText(this, "Marker added at current position.", Toast.LENGTH_LONG).show(); //display the result
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //get preferences
                    boolean autoupload = prefs.getBoolean("autoupload", true); //get autoupload from preferences.
                    if (autoupload){ //check if autoupload is set to "true".
                        PostPointsTask posting = new PostPointsTask(); //create a new task.
                        posting.execute(name_in, type_in, description_in, latitude_in, longitude_in); //pass the parameters to the task.
                    } else {
                        System.out.println("Debug: Auto-upload is turned off.");
                    }
                }
            }
        } else if (requestCode == 1){

        }

    }
    //!--activity_results--!

    //location manager
    public void onLocationChanged(Location newLoc) {

        //react to location being changed.
        //map fragment
        MapFragment mapfragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);

        if (currentLocation != null) {
            items.removeItem(currentLocation); //remove current loc
        }
        double newLat = newLoc.getLatitude(); //get the latitude
        double newLong = newLoc.getLongitude(); //get the longitude
        mapfragment.setLocation(newLat, newLong); //set lat and long to a map
        currentLocation = new OverlayItem("You are here", "Now", new GeoPoint(newLat, newLong)); //create a new overlay item with the location of a new geo point.
        currentLocation.setMarker(getResources().getDrawable(R.drawable.me)); //set marker for current location
        items.addItem(currentLocation); //add it
    }
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }
    //!--location_manager--!

    //menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        // react to the menu item being selected...

        if (item.getItemId() == R.id.new_point) {
            Intent intent = new Intent(this, NewPoint.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.points_list){
            ArrayList<String> entry_name = new ArrayList<String>();
            double[] value_lat = new double[items.size()];
            double[] value_long = new double[items.size()];
            for (int i = 0; i < items.size(); i++) { //loop through each item in the items array.
                OverlayItem currentItem = items.getItem(i); //get the item from items array.
                String name = currentItem.getTitle(); //get title from item
                Double lat = currentItem.getPoint().getLatitude(); //get latitude from item
                Double lon = currentItem.getPoint().getLongitude(); //get longitude fom item
                entry_name.add(name);
                value_lat[i] = lat;
                value_long[i] = lon;
            }
            Intent intent = new Intent(this, PointsListActivity.class);
            intent.putExtra("entry", entry_name);
            intent.putExtra("value_lat", value_lat);
            intent.putExtra("value_long", value_long);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.load_file) {
            loadFile(filePath);
        } else if (item.getItemId() == R.id.save_file){
            saveFile(filePath);
            Toast.makeText(this, "Markers saved to a " + filePath, Toast.LENGTH_LONG).show(); //display the result
        } else if (item.getItemId() == R.id.download){
            GetPointsTask task = new GetPointsTask();
            task.execute();
            saveFile(filePath);
        } else if (item.getItemId() == R.id.preferences){
            Intent intent = new Intent(this, Preferences.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return false;
    }
    //!--menu_items--!

    //save and load functions
    public void saveFile(String filePath){

        items.removeItem(currentLocation);
        try
        {
            File file = new File(filePath);
            PrintWriter pw =
                    new PrintWriter( new FileWriter(file));
            for (int i = 0; i < items.size(); i++) { //loop through each item in the items array.
                OverlayItem item = items.getItem(i); //get the item from items array.
                String name = item.getTitle(); //get title from item
                String type = item.getUid(); // get type from item
                String description = item.getSnippet(); //get description from item.
                String lat = String.valueOf(item.getPoint().getLatitude()); //get latitude from item
                String lon = String.valueOf(item.getPoint().getLongitude()); //get longitude fom item
                String result = name + "," + type + "," + description + "," + lat + "," + lon; //put everything together into CSV format.
                pw.println(result); //print result into a line.
            }
            pw.close(); // close the file to ensure data is flushed to file
        }
        catch(IOException e)
        {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("ERROR " + e).show();
        }
        items.addItem(currentLocation);
    }

    public void loadFile(String filepath){
        File file = new File(filepath);
        if (!file.exists()){
            Toast.makeText(this, "ERROR: file does not exist in the " + filePath, Toast.LENGTH_LONG).show(); //display the result
        } else {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line;
                while ((line = reader.readLine()) != null) { // loop through each line until it exists.
                    String[] components = line.split(","); //split the line into components where "," exists.
                    if (components.length == 5) {
                        String newTitle = components[0];
                        String newType = components[1];
                        String newSnippet = components[2];
                        double newLat = Double.parseDouble(components[3]);
                        double newLong = Double.parseDouble(components[4]);
                        OverlayItem newItem = new OverlayItem(newType, newTitle, newSnippet, new GeoPoint(newLat,newLong));
                        newItem.setMarker(typeToMarker(newType)); //set marker
                        items.addItem(newItem);
                    }
                }
                Toast.makeText(this, "Markers loaded from " + filePath, Toast.LENGTH_LONG).show(); //display the result
            } catch (IOException e) {
                new AlertDialog.Builder(this).setPositiveButton("OK", null).
                        setMessage("ERROR: " + e).show();
            }
        }
    }
    //!--save and load functions--!

    //type to marker function
    public Drawable typeToMarker(String type){
        Drawable marker;
        switch (type.toLowerCase()) {
            case "airport": marker = getResources().getDrawable(R.drawable.airport);
                break;
            case "bank": marker = getResources().getDrawable(R.drawable.bank);
                break;
            case "bar": marker = getResources().getDrawable(R.drawable.bar);
                break;
            case "cafe": marker = getResources().getDrawable(R.drawable.cafe);
                break;
            case "casino": marker = getResources().getDrawable(R.drawable.casino);
                break;
            case "church": marker = getResources().getDrawable(R.drawable.church);
                break;
            case "cinema": marker = getResources().getDrawable(R.drawable.cinema);
                break;
            case "city": marker = getResources().getDrawable(R.drawable.city);
                break;
            case "hotel": marker = getResources().getDrawable(R.drawable.hotel);
                break;
            case "info": marker = getResources().getDrawable(R.drawable.info);
                break;
            case "me": marker = getResources().getDrawable(R.drawable.me);
                break;
            case "museum": marker = getResources().getDrawable(R.drawable.museum);
                break;
            case "park": marker = getResources().getDrawable(R.drawable.park);
                break;
            case "pizza": marker = getResources().getDrawable(R.drawable.pizza);
                break;
            case "pub": marker = getResources().getDrawable(R.drawable.pub);
                break;
            case "restaurant": marker = getResources().getDrawable(R.drawable.restaurant);
                break;
            case "school": marker = getResources().getDrawable(R.drawable.school);
                break;
            case "shop": marker = getResources().getDrawable(R.drawable.shop);
                break;
            case "sight": marker = getResources().getDrawable(R.drawable.sight);
                break;
            case "town": marker = getResources().getDrawable(R.drawable.town);
                break;
            case "university": marker = getResources().getDrawable(R.drawable.university);
                break;
            default: marker = null;
                break;
        }
        return marker;
    }
    //!--type to marker--!

    //ASYNC tasks GET and POST

    //GET TASK
    class GetPointsTask extends AsyncTask<Void,Void,String>
    {
        public String doInBackground(Void... unused)
        {
            String queryURL = getURL + "?year=19&username=user003&format=csv"; //define a string with URL.
            HttpURLConnection conn = null; //define a HTTP connection variable.
            try
            {
                URL url = new URL(queryURL); //create a new URL object from url string
                conn = (HttpURLConnection) url.openConnection(); //open connection to a URL.
                InputStream in = conn.getInputStream(); //get input stream from the connection.
                if(conn.getResponseCode() == 200) //if response = OK
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in)); //reader for input stream.
                    StringBuffer result = new StringBuffer(); //string buffer "result"
                    String line;
                    while((line = br.readLine()) !=null) { //loop through each line in the input while it exists and put into "line" variable.
                        result.append(line).append("\n"); //append "result" with "line".
                    }
                    return result.toString(); //return "result"
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            }
            catch(IOException e) //error
            {
                return e.toString();
            }
            finally {
                //try to disconnect
                if(conn!=null) {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result)
        {
            try{
                BufferedReader reader = new BufferedReader(new StringReader(result));
                String line;
                while ((line = reader.readLine()) != null) { //loop through each line while it exists and put into "line" variable.
                    String[] components = line.split(","); //split the line into components where "," exists.
                    if (components.length == 5) {
                        String newName = components[0];
                        String newType = components[1];
                        String newDescription = components[2];
                        double newLat = Double.parseDouble(components[4]);
                        double newLong = Double.parseDouble(components[3]);

                        OverlayItem newItem = new OverlayItem(newType, newName, newDescription, new GeoPoint(newLat,newLong));
                        newItem.setMarker(typeToMarker(newType)); //function to return marker with a given type.
                        items.addItem(newItem);
                    }
                }
                Toast.makeText(MainActivity.this, "Markers loaded from web.", Toast.LENGTH_SHORT).show(); //display the result
            } catch (IOException e) {
                new AlertDialog.Builder(MainActivity.this).setMessage(e.toString()).setPositiveButton("OK", null).show();
            }
        }
    }

    //POST TASK
    class PostPointsTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... input) {
            String name = input[0];
            String type = input[1];
            String description = input[2];
            String lat = input[3];
            String lon = input[4];
            String postData = "username=user003&year=19&name=" + name + "&type=" + type + "&description=" + description + "&lon=" + lon + "&lat=" + lat;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(postURL); //declare a URL object.
                conn = (HttpURLConnection) url.openConnection(); //declare an open connection.

                // For POST
                conn.setDoOutput(true); //declare an output connection
                conn.setFixedLengthStreamingMode(postData.length()); //set fixed output length, the length of postData.

                OutputStream out = null; //declare an output stream variable.
                out = conn.getOutputStream(); //set an output stream.
                out.write(postData.getBytes()); //send bytes of postData to output stream.
                if (conn.getResponseCode() == 200) { //if RESULT = OK
                    InputStream in = conn.getInputStream(); //get input stream from the connection.
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = br.readLine()) != null) { //loop through each line in the input while it exists and put into "line" variable.
                        result.append(line).append("\n"); //append "result" with "line".
                    }
                    return result.toString(); //return result.
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                //error
                return e.toString();
            } finally {
                //try disconnect
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        public void onPostExecute(String result) {

            // react on the event after execution returning a string.

            String trimmed = result.trim();
            if (trimmed.equals("OK")){
                Toast.makeText(MainActivity.this, "Marker saved to web.", Toast.LENGTH_SHORT).show(); //display the result
            } else {
                new AlertDialog.Builder(MainActivity.this).setMessage("ERROR: " + result).setPositiveButton("OK", null).show();
            }

        }
    }
    //!--ASYNC tasks GET and POST--!
}
