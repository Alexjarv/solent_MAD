package uk.ac.solent.mapping;

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

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    //declare the variables here
    MapView mv;
    public static final Double DEFAULT_LAT = 51.05;
    public static final Double DEFAULT_LON = -0.72;
    public static final Integer DEFAULT_ZOOM = 16;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main); //sets the layout of the application for main activity

        // set default values to latitude and longtitude
        EditText latEditText = (EditText) findViewById(R.id.latitude);
        latEditText.setText(DEFAULT_LAT.toString());
        EditText lonEditText = (EditText) findViewById(R.id.longitude);
        lonEditText.setText(DEFAULT_LON.toString());

        // create buttons and set listeners on them.
        Button go = (Button) findViewById(R.id.btn1); // finds button by id and puts it into variable GO
        go.setOnClickListener(this); //button go is set to listener for events (this) means this button.
        Button cancel = (Button) findViewById(R.id.btn2); // finds button by id and puts it into variable CANCEL
        cancel.setOnClickListener(this); //button cancel is set to listener for events (this) means this button.

        // map parameters
        mv = (MapView) findViewById(R.id.map1); // find the map by id and puts it into variable MV
        mv.setBuiltInZoomControls(true); //enables to zoom in the map
        mv.getController().setZoom(DEFAULT_ZOOM); //sets zoom level to variable declared as DEFAULT_ZOOM
        mv.getController().setCenter(new GeoPoint(DEFAULT_LAT,DEFAULT_LON)); //sets the center of the map to variables DEFAULT_LAT and DEFAULT_LON
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.choosemap)
        {
            //react to the menu item being selected
            Intent intent = new Intent(this, MapChooseActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
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
    private Double parseLong(EditText geoEditText) {
        String input = geoEditText.getText().toString();
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

    // function to create a popup message
    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
    }

    @Override
    public void onClick(View view) {
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
}


