package uk.ac.solent.fileio;

import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuSave){
            EditText et = (EditText) findViewById(R.id.editText);
            String saveET = et.getText().toString();
            save(saveET);
        }
        if(item.getItemId() == R.id.menuLoad){
            String returnedText = load();
            EditText et = (EditText) findViewById(R.id.editText);
            et.setText(returnedText.toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    public String load(){
        String text="";
        String line="";
        try{
            FileReader fr = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt");
            BufferedReader reader = new BufferedReader(fr);
            String[] components = line.split(",");
            while((line = reader.readLine()) != null){
                text = text+line;
            }
        }
        catch(IOException e){
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("ERROR " + e).show();
        }
        return text;
    }

    public void save(String text){
        try
        {
            PrintWriter pw =
                    new PrintWriter( new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt"));

            pw.println(text);

            pw.close(); // close the file to ensure data is flushed to file
        }
        catch(IOException e)
        {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("ERROR " + e).show();
        }
    }
}
