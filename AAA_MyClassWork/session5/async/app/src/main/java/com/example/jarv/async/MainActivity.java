package com.example.jarv.async;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.text.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go = (Button)findViewById(R.id.btn1);
        go.setOnClickListener(MainActivity.this);
    }

    public void onClick(View v)
    {
        MyTask t = new MyTask();
        t.execute();
    }

    class MyTask extends AsyncTask<Void,Void,String>
        {
            public String doInBackground(Void... unused)
            {
                HttpURLConnection conn = null;
                try
                {
                    URL url = new URL("http://server.com/people.php");
                    conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();
                    if(conn.getResponseCode() == 200)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String result = "", line;
                        while((line = br.readLine()) !=null)
                        {
                            result += line;
                        }
                        return result;
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
                EditText et1 = (EditText)findViewById(R.id.et1);
                et1.setText(result);
            }
        }
}

