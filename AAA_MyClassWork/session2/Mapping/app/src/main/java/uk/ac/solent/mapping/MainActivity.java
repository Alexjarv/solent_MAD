package uk.ac.solent.mapping;

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

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    //declare the variables here
    MapView mv;
    public static final Double DEFAULT_LAT = 59.43;
    public static final Double DEFAULT_LON = 24.72;
    public static final Integer DEFAULT_ZOOM = 11;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        // set default values to latitude and longtitude
        EditText latEditText = (EditText) findViewById(R.id.latitude);
        latEditText.setText(DEFAULT_LAT.toString());
        EditText lonEditText = (EditText) findViewById(R.id.longitude);
        lonEditText.setText(DEFAULT_LON.toString());

        // create buttons and set listeners on them.
        Button go = (Button) findViewById(R.id.btn1);
        go.setOnClickListener(this);
        Button cancel = (Button) findViewById(R.id.btn2);
        cancel.setOnClickListener(this);

        // map parameters
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(DEFAULT_ZOOM);
        mv.getController().setCenter(new GeoPoint(DEFAULT_LAT,DEFAULT_LON));
    }

    @Override
    public void onClick(View v) {

    }
}

/*
// lat +90 to -90
    private Double parseLat(EditText geoEditText) {
        String input = geoEditText.getText().toString();
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

    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
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
        Double lon = parseLat(lonEditText);
        Double lat = parseLat(latEditText);
        if (lon != null && lat != null) {
            mv.getController().setCenter(new GeoPoint(lat, lon));
        }

}
 */