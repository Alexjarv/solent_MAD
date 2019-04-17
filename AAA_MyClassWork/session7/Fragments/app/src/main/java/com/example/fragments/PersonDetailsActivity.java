package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PersonDetailsActivity extends AppCompatActivity
{

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)
        {
            finish();
            return;
        }

        setContentView(R.layout.persondetailsfrag);

        Intent intent = this.getIntent();
        if(intent!=null)
        {
            String value = intent.getExtras().getString("contents");
            if(value==null)
                value = "Contents not found";
            PersonDetailsFragment frag = (PersonDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.personDetailsFrag);
            frag.setText(value);
        }
    }

}

