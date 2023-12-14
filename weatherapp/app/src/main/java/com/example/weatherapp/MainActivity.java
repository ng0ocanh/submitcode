package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String API_KEY = "8a3128e73296fdd7a44e1856bee90c96";
    EditText edtProvince;
    Button btnok;
    TextView txtProvince, txtNational, txtTemper, txtTemperState, txtCurentTemper, txtCloud, txtHumidity, txtWind;
    ImageView imgWeathericon;
    String city = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        btnok.setOnClickListener(this);

        if(city== "") {
            getJsonWeather("hanoi");
        } else getJsonWeather(city);



    }
    public void getJsonWeather(String city){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID="+API_KEY+"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String temperState;

                        String humidity;
                        String speed;

                        JSONObject wind;
                        try {
                            JSONArray weatherArry = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArry.getJSONObject(0);
                            String icon = weatherObj.getString("icon");
                            String urlIcon = "https://openweathermap.org/img/wn/" +icon+ ".png";
                            Picasso.get().load(urlIcon).into(imgWeathericon);
                            temperState = weatherObj.getString("main");
                            txtTemperState.setText(temperState);
                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");
                            txtTemper.setText(temp + "°C");
                            humidity = main.getString("humidity");
                            txtHumidity.setText(humidity + "%");
                            wind = response.getJSONObject("wind");

                            speed = wind.getString("speed");
                            txtWind.setText(speed+ "m/s");
                            JSONObject clouds = response.getJSONObject("clouds");
                            String all = clouds.getString("all");
                            txtCloud.setText(all+"%");
                            String sDay = response.getString("dt");
                            long lDay = Long.parseLong(sDay);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, yyyy - MM-dd HH:mm:ss");
                            Date date = new Date(lDay*1000);
                            String currentTime = dateFormat.format(date);
                            txtCurentTemper.setText(currentTime);
                            String name = response.getString("name"); //city
                            txtProvince.setText(name);
                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");
                            txtNational.setText(country);






                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
//                        Toast.makeText(MainActivity.this, "" + icon, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "" + temperState, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "" + humidity, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "" + wind, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "" + all, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "" + currentTime, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "no data city" + city, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(JsonObjectRequest);
    }

    private void mapping() {
        edtProvince = findViewById(R.id.edtProvince);
        btnok = findViewById(R.id.btnok);

        txtNational = findViewById(R.id.txtNational);
        txtProvince = findViewById(R.id.txtProvince);
        txtTemper = findViewById(R.id.txtTemper);
        txtTemperState = findViewById(R.id.txtTemperState);
        txtCurentTemper = findViewById(R.id.txtCurentTemper);
        txtCloud = findViewById(R.id.txtCloud);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);
        imgWeathericon = findViewById(R.id.imgWeathericon);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnok) {
            city = edtProvince.getText().toString();
            if (!city.equals("")) {
                getJsonWeather(city);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }

        }
    }
    }

