package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FragActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);
    }
    public void receiveText(String value)
    {
        PersonDetailsFragment frag = (PersonDetailsFragment)this.getSupportFragmentManager().findFragmentById(R.id.personDetailsFrag);

        if (frag == null || !frag.isInLayout())
        {
            Intent intent = new Intent (this, PersonDetailsActivity.class);
            intent.putExtra("contents", value);
            startActivity(intent);
        }
        else
        {
            // set its contents
            frag.setText(value);
        }
    }
}
