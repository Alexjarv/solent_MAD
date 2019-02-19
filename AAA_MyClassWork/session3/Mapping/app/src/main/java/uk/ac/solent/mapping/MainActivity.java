package uk.ac.solent.mapping;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity
{
    //declare the variables here
    MapView mv;
    public static final Double DEFAULT_LAT = 51.05;
    public static final Double DEFAULT_LON = -0.72;
    public static final Integer DEFAULT_ZOOM = 16;
    public boolean isRecording;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            isRecording = savedInstanceState.getBoolean ("isRecording");
        }

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main); //sets the layout of the application for main activity

        // set default values to latitude and longtitude
        TextView latTextView = (TextView) findViewById(R.id.tv2_value);
        latTextView.setText(DEFAULT_LAT.toString());
        TextView lonTextView = (TextView) findViewById(R.id.tv3_value);
        lonTextView.setText(DEFAULT_LON.toString());
        TextView zoomTextView = (TextView) findViewById(R.id.tv4_value);
        zoomTextView.setText(DEFAULT_ZOOM.toString());

        // map parameters
        mv = (MapView) findViewById(R.id.map1); // find the map by id and puts it into variable MV
        mv.setBuiltInZoomControls(true); //enables to zoom in the map
        mv.getController().setZoom(DEFAULT_ZOOM); //sets zoom level to variable declared as DEFAULT_ZOOM
        mv.getController().setCenter(new GeoPoint(DEFAULT_LAT,DEFAULT_LON)); //sets the center of the map to variables DEFAULT_LAT and DEFAULT_LON
    }

  //  public void onResume()
//    {
//        super.onResume();

 //   }

    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean ("isRecording", isRecording);
        editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) //create a menu option
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.listChooseMap)
        {
            //react to the menu item being selected
            Intent intent = new Intent(this, ListMapChooseActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        else if (item.getItemId() == R.id.setlocation){
            Intent intent = new Intent(this, SetLocationActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (item.getItemId() == R.id.setDefaults){
            Intent intent = new Intent(this, MyPrefsActivity.class);
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.listActivity) {
            Intent intent = new Intent(this, ExampleListActivity.class);
            startActivityForResult(intent, 3);
            return true;
        } else if (item.getItemId() == R.id.listActivity2) {
            Intent intent = new Intent(this, ExampleListActivity2.class);
            startActivityForResult(intent, 4);
            return true;
        } else if (item.getItemId() == R.id.choosemap) {
            Intent intent = new Intent(this, MapChooseActivity.class);
            startActivityForResult(intent, 5);
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if (resultCode == RESULT_OK){
                Bundle extras = intent.getExtras();
                boolean hikebikemap = extras.getBoolean("com.example.hikebikemap");
                if (hikebikemap == true){
                    mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
                }
                else{
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        } else if(requestCode == 1){
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String latitude = extras.getString("lat_results");
                String longtitude = extras.getString("lon_results");

                TextView latTextView = (TextView) findViewById(R.id.tv2_value);
                latTextView.setText(latitude);
                TextView lonTextView = (TextView) findViewById(R.id.tv3_value);
                lonTextView.setText(longtitude);

                double latitudeDouble =DEFAULT_LAT;
                double longtitudeDouble =DEFAULT_LON ;


                try {
                   latitudeDouble = Double.parseDouble(latitude);
                    longtitudeDouble = Double.parseDouble(longtitude);
                } catch (Exception ex){
                    System.out.println("DEBUG problem "+ex.toString());
                }

                System.out.println("DEBUG ****************** returned lat="+latitudeDouble +" returned lon="+longtitudeDouble);



                mv = (MapView) findViewById(R.id.map1); // find the map by id and puts it into variable MV
                mv.getController().setCenter(new GeoPoint(latitudeDouble, longtitudeDouble));
                mv.setBuiltInZoomControls(true); //enables to zoom in the map
                mv.getController().setZoom(DEFAULT_ZOOM); //sets zoom level to variable declared as DEFAULT_ZOOM

            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                double lat = Double.parseDouble ( prefs.getString("lat", "50.9") );
                double lon = Double.parseDouble ( prefs.getString("lon", "-1.4") );
                int zoom = Integer.parseInt ( prefs.getString("zoom", "11") );
                boolean autodownload = prefs.getBoolean("autodownload", true);

               // do something with the preference data...

                TextView latTextView = (TextView) findViewById(R.id.tv2_value);
                latTextView.setText(Double.toString(lat));
                TextView lonTextView = (TextView) findViewById(R.id.tv3_value);
                lonTextView.setText(Double.toString(lon));

             // map parameters
                mv = (MapView) findViewById(R.id.map1); // find the map by id and puts it into variable MV
                mv.setBuiltInZoomControls(true); //enables to zoom in the map
                mv.getController().setZoom(zoom); //sets zoom level to variable declared as DEFAULT_ZOOM
                mv.getController().setCenter(new GeoPoint(lat,lon)); //sets the center of the map to variables DEFAULT_LAT and DEFAULT_LON
            }
        }
    }

    // function to create a popup message
    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
    }

    // lat +90 to -90
    private Double parseLat(EditText geoEditText) { //declare a function parseLat with parameter EditText which is named geoEditText
        String input = geoEditText.getText().toString(); // gets the text of geoEditText transfers into string and puts it into variable called string.
        try {
            Double latitude = Double.parseDouble(input);
            if (latitude > 90 || latitude < -90) {
                geoEditText.setText("");
                geoEditText.setHint("invalid latitude: " + input);
                String message = "invalid latitude";
                popupMessage(message);
                return null;
            }
            return latitude;
        } catch (Exception e) {
            geoEditText.setText("");
            geoEditText.setHint("invalid latitude: " + input);
            String message = "invalid latitude: " + input;
            popupMessage(message);
            return null;
        }
    }

    //  long +180 to -180
    private Double parseLong(EditText geoEditText) { //declare a function parseLat with parameter EditText which is named geoEditText
        String input = geoEditText.getText().toString(); // gets the text of geoEditText transfers into string and puts it into variable called string.
        try {
            Double longitude = Double.parseDouble(input);
            if (longitude > 180 || longitude < -180) {
                geoEditText.setText("");
                geoEditText.setHint("invalid longitude: " + input);
                String message = "invalid logitude: " + input;
                popupMessage(message);
                return null;
            }
            return longitude;
        } catch (Exception e) {
            geoEditText.setText("");
            geoEditText.setHint("invalid longitude: " + input);
            String message = "invalid longitude: " + input;
            popupMessage(message);
            return null;
        }
    }

    /*
    @Override
    /public void onClick(View view) {
        EditText lonEditText = (EditText) findViewById(R.id.longitude);
        EditText latEditText = (EditText) findViewById(R.id.latitude);

        switch (view.getId()) {
            case R.id.btn1: // ok - just continue
                break;
            case R.id.btn2: // reset default
                lonEditText.setText(DEFAULT_LON.toString());
                latEditText.setText(DEFAULT_LAT.toString());
                mv.getController().setZoom(DEFAULT_ZOOM);
                break;
            default:
                break;
        }

        // load and check values
        Double lon = parseLong(lonEditText);
        Double lat = parseLat(latEditText);
        if (lon != null && lat != null) {
            mv.getController().setCenter(new GeoPoint(lat, lon));
        }

    }
    */
}


