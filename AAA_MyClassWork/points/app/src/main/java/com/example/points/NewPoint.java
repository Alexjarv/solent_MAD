package com.example.points;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPoint extends AppCompatActivity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_point);

        Button add = (Button) findViewById(R.id.newPointBtn);
        add.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        EditText et_name = (EditText) findViewById(R.id.nameEdit);
        EditText et_type = (EditText) findViewById(R.id.typeEdit);
        EditText et_description = (EditText) findViewById(R.id.descEdit);

        String name = et_name.getText().toString();
        String type = et_type.getText().toString();
        String description = et_description.getText().toString();

        if (name.equals("") || type.equals("") || description.equals("")){
            Toast.makeText(this, "Please, enter all details.", Toast.LENGTH_LONG).show();
            return;
        } else {
            Intent intent = new Intent();
            Bundle bundle=new Bundle();

            bundle.putString("point_name", name);
            bundle.putString("point_type", type);
            bundle.putString("point_description", description);
            intent.putExtras(bundle);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
