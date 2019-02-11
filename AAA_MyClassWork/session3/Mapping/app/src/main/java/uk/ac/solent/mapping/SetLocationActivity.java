package uk.ac.solent.mapping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SetLocationActivity extends AppCompatActivity implements OnClickListener {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Button setLocationButton = (Button) findViewById(R.id.setLocationButton);
        setLocationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        EditText latEditText = (EditText) findViewById(R.id.latitudeEditText);
        EditText lonEditText = (EditText) findViewById(R.id.longitudeEdiText);
        String latResult = latEditText.getText().toString();
        String lonResult = lonEditText.getText().toString();
        bundle.putString("lon_results", lonResult);
        bundle.putString("lat_results", latResult);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
