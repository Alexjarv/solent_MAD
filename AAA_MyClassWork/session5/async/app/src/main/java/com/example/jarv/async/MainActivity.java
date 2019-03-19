package com.example.jarv.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String URL = "http://www.free-map.org.uk/course/ws/hits.php";
    private static final String ARTIST_QUERY="format=json&artist=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go = (Button)findViewById(R.id.btn1);
        go.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        EditText artist = (EditText) findViewById(R.id.artist);
        MainActivity.MyTask t = new MainActivity.MyTask();
        t.execute(artist.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.async_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mainActivity) {
            return false;
        }
        if(item.getItemId() == R.id.secondActivity) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        return false;
    }

    class MyTask extends AsyncTask<String,Void,String>
        {
            public String doInBackground(String... input)
            {
                String artist = input[0];
                String queryURL = URL + "?"+ARTIST_QUERY+artist;
                HttpURLConnection conn = null;
                try
                {
                    URL url = new URL(queryURL);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();
                    if(conn.getResponseCode() == 200)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        StringBuffer result = new StringBuffer();
                        String line;
                        while((line = br.readLine()) !=null)
                        {
                            result.append(line).append("\n");
                        }
                        return result.toString();
                    }
                    else
                    {
                        return "HTTP ERROR: " + conn.getResponseCode();
                    }
                }
                catch(IOException e)
                {
                    return e.toString();
                }
                finally
                {
                    if(conn!=null)
                    {
                        conn.disconnect();
                    }
                }
            }

            public void onPostExecute(String result)
            {
                try{
                    TextView tv1 = (TextView) findViewById(R.id.artistResult);
                    JSONArray jsonArr = new JSONArray(result);
                    String text="";

                    for(int i=0; i<jsonArr.length(); i++)
                    {
                        JSONObject curObj = jsonArr.getJSONObject(i);
                        String id = curObj.getString("ID"),
                                title = curObj.getString("title"),
                                artist = curObj.getString("artist"),
                                day = curObj.getString("day"),
                                month = curObj.getString("month"),
                                year = curObj.getString("year"),
                                chart = curObj.getString("chart"),
                                likes = curObj.getString("likes"),
                                downloads = curObj.getString("downloads"),
                                genre = curObj.getString("genre"),
                                price = curObj.getString("price"),
                                quantity = curObj.getString("quantity");
                        text += id + ":" + " " + title + " by " + artist + ", released " + day + " " + month + " " + year + ", chart position " +
                                chart + ", likes " + likes + ", downloads " + downloads + ", genre " + genre + ", price " + price + ", quantity " + quantity + " |" +  "\n";
                    }
                    tv1.setText(text);
                } catch (JSONException e){
                    new AlertDialog.Builder(MainActivity.this).setMessage(e.toString()).setPositiveButton("OK", null).show();
                }
            }
        }
}

