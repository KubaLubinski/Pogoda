package com.example.pogoda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    TextView tvResult;
    private final String URL="https://api.openweathermap.org/data/2.5/weather";
    private final String APPID="e53301e27efa0b66d05045d91b2742d3";
    String output = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String city = etCity.getText().toString().trim();
        String apiURL = "";
        if(city.equals("")){
            clearResult();
            Toast.makeText(getApplicationContext(), R.string.brak, Toast.LENGTH_SHORT).show();
        } else {
            apiURL = URL + "?q=" + city + "&units=metric&lang=pl&appid=" + APPID;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");

                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");

                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");

                        String cityName = jsonResponse.getString("name");

                        output += "Bieżąca pogoda w mieście " + cityName
                                + "\n Temperatura: " + temp + "C"
                                + "\n Temperatura odczuwalna: " + feelsLike + "C"
                                + "\n Wilgotność: " + humidity + "%"
                                + "\n Opis: " + description
                                + "\n Wiatr: " + wind + "m/s"
                                + "\n Zachmurzenie: " + clouds + "%"
                                + "\n Ciśnienienie: " + pressure + " hPa";
                        tvResult.setText(output);
                        tvResult.setBackgroundColor(0x883DADF2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    clearResult();
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public  void clearResult(){
        output = "";
        tvResult.setText(output);
        tvResult.setBackgroundColor(0x00FFFFFF);
    }
}