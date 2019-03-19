package com.example.jarv.async;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/*<!--add a new song to the database. It takes the following POST fields:-->
        <!--song - the song title;-->
        <!--artist - the artist;-->
        <!--year - the year of the song.-->*/

public class SecondActivity extends AppCompatActivity implements OnClickListener {

    private static final String BASE_URL = "http://www.free-map.org.uk/course/ws/addhit.php";

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.second_activity);
            Button addSong = (Button) findViewById(R.id.btnAddSong);
            addSong.setOnClickListener(this);
        }

        public void onClick(View view){
            EditText songET = (EditText) findViewById(R.id.newsong);
            EditText artistET = (EditText) findViewById(R.id.newartist);
            EditText yearET = (EditText) findViewById(R.id.newyear);

            String song = songET.getText().toString().trim();
            String artist = artistET.getText().toString().trim();
            String year = yearET.getText().toString().trim();

            if(song.isEmpty()){
                popupAlert("Song must be set");
            } else if (artist.isEmpty()) {
                popupAlert("Artist must be set");
            } else if (year.isEmpty()){
                popupAlert("Year must be set");
            } else {
                PostSongTask t = new PostSongTask();
                t.execute(artist, song, year);
            }
        }

    class PostSongTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... input) {
            String artist = input[0];
            String song = input[1];
            String year = input[2];
            String postData = "artist=" + artist
                    + "&song=" + song
                    + "&year=" + year;


            System.out.println("debug" + postData);

            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL);
                conn = (HttpURLConnection) url.openConnection();

                // For POST
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                out.write(postData.getBytes());
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                    return result.toString();
                } else {
                    System.out.println("debug error1");
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                System.out.println("debug error2");
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        public void onPostExecute(String result) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            TextView resultText = (TextView) findViewById(R.id.postResult);
            resultText.setVisibility(View.VISIBLE);
            resultText.setBackgroundColor(color);
            resultText.setText("Successful!");
        }
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent,0);
                return true;
            }
            if(item.getItemId() == R.id.secondActivity) {
                return false;
            }
            return false;
        }
        private void popupAlert(String message){
            new AlertDialog.Builder(this).setPositiveButton("OK",null).setMessage(message).show();
        }
}
