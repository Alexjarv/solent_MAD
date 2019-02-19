package uk.ac.solent.mapping;


import android.content.Intent;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class MyPrefsActivity extends PreferenceActivity
{
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
    public void onPause() {

        super.onPause();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void onStop() {

        super.onStop();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
