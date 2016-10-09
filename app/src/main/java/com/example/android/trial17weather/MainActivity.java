package com.example.android.trial17weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while ( data != -1){

                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String message = null;
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather", weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("desc",jsonPart.getString("description"));
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if (main != "" && description != ""){
                     message += main + ":" + description+ "\r\n";

                    }
                    if (message != ""){
                        resultTextView.setText(message);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
                    }

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
                e.printStackTrace();
            }catch ( Exception e){
                Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
                e.printStackTrace();
            }
            Log.i("WEB",s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }

    public  void checkWeather(View view){

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        try {
            String city = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=d47fa40f72be0f1627bec9753a6ad275");
        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
            e.printStackTrace();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_LONG);
            e.printStackTrace();
        }



    }
}
