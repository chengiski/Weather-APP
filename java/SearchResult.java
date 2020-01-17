package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class SearchResult extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static Context context;
    private String city;
    //Card one.
    private TextView tempView;
    private TextView sumView;
    private TextView cityView;
    private ImageView iconView;
    //Card two.
    private TextView humidView;
    private TextView windView;
    private TextView visibView;
    private TextView pressView;
    //Card three.
    private TableLayout tableView;
    //Click to open DetailActivity activity.
    private CardView card_view;
    //api response JSONObject
    private JSONObject weather;
    //Set a flag for favorite
    private boolean isFavor;
    private SharedPreferences favors;
    //progress bar
    private ConstraintLayout progressBar;
    private ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        requestQueue = Volley.newRequestQueue(this);
        SearchResult.context = getApplicationContext();
        //add a tool bar and enable an up arrow
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //progress bar
        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.container);
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        //Card one.
        tempView = findViewById(R.id.temp);
        sumView = findViewById(R.id.summary);
        cityView = findViewById(R.id.city);
        iconView = findViewById(R.id.icon);
        //Card two.
        humidView = findViewById(R.id.humid);
        windView = findViewById(R.id.windSpeed);
        visibView = findViewById(R.id.visibility);
        pressView = findViewById(R.id.pressure);
        //Card three
        tableView = (TableLayout)findViewById(R.id.table);
        //Get city and country from main activity
        Intent intent = getIntent();
        city = intent.getStringExtra("cityForSearch");
        Log.d("test1", city);
        TextView cityBar = findViewById(R.id.cityBar);
        cityBar.setText(city);
        cityView.setText(city);
        //fab
        final FloatingActionButton fab = findViewById(R.id.fab);
        favors = getSharedPreferences(
                "com.example.weatherapp", MODE_PRIVATE);
        if (favors.contains(city)){
            isFavor = true;
            fab.setImageResource(R.drawable.map_marker_minus);
        }else{
            isFavor = false;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (isFavor){
                fab.setImageResource(R.drawable.map_marker_plus);
                isFavor = false;
                favors.edit().remove(city).apply();
                Toast.makeText(SearchResult.context, city+" was removed from favorites",
                        Toast.LENGTH_LONG).show();
            }
            else {
                fab.setImageResource(R.drawable.map_marker_minus);
                isFavor = true;
                favors.edit().putString(city,city).apply();
                Toast.makeText(SearchResult.context, city+" was added to favorites",
                        Toast.LENGTH_LONG).show();
            }
            }
        });

        //Call method to get json
        jasonWeather(city);
        //Click to open DetailActivity activity.
        card_view = findViewById(R.id.card_view1);
        card_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDetailActivity();
            }
        });
    }
    public void openDetailActivity(){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("cityName", city);
        intent.putExtra("json", weather.toString());
        startActivity(intent);
    }

    private void jasonWeather(String city){
        String address = "street=&city=&state="+city;
        String url = "https://weather-search-259408.appspot.com/weather?"+address;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weather = response;
                            JSONObject currently = response.getJSONObject("currently");
                            //Card one.
                            int temp = Math.round(Float.parseFloat(currently.getString("temperature")));
                            tempView.setText(temp+"°F");
                            sumView.setText(currently.getString("summary"));
                            String icon = currently.getString("icon");
                            switch (icon){
                                case "clear-night":
                                    iconView.setImageResource(R.drawable.weather_night);
                                    break;
                                case "rain":
                                    iconView.setImageResource(R.drawable.weather_rainy);
                                    break;
                                case "snow":
                                    iconView.setImageResource(R.drawable.weather_snowy);
                                    break;
                                case "sleet":
                                    iconView.setImageResource(R.drawable.weather_snowy_rainy);
                                    break;
                                case "wind":
                                    iconView.setImageResource(R.drawable.weather_windy_variant);
                                    break;
                                case "fog":
                                    iconView.setImageResource(R.drawable.weather_fog);
                                    break;
                                case "cloudy":
                                    iconView.setImageResource(R.drawable.weather_cloudy);
                                    break;
                                case "partly-cloudy-day":
                                    iconView.setImageResource(R.drawable.weather_partly_cloudy);
                                    break;
                                case "partly-cloudy-night":
                                    iconView.setImageResource(R.drawable.weather_night_partly_cloudy);
                                    break;
                                default:
                                    iconView.setImageResource(R.drawable.weather_sunny);
                            }
                            //Card two.
                            int humid = Math.round(Float.parseFloat(currently.getString("humidity"))*100);
                            humidView.setText(humid+"%");
                            float windSpeed = ((float)Math.round(Float.parseFloat(currently.getString("windSpeed"))*100))/100;
                            windView.setText(windSpeed+" mph");
                            float vis = ((float)Math.round(Float.parseFloat(currently.getString("visibility"))*100))/100;
                            visibView.setText(vis+" km");
                            float press = ((float)Math.round(Float.parseFloat(currently.getString("pressure"))*100))/100;
                            pressView.setText(press+" mb");
                            //Card three.
                            JSONArray dailyData = response.getJSONObject("daily").getJSONArray("data");
                            createTable(dailyData);
                            //progress bar gone
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            sumView.setText("Please check the input format.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
    //Create table in card three
    private void createTable(JSONArray dailyData){
        for (int i = 0; i < dailyData.length(); i++){
            TableRow tr =  new TableRow(this);
            TextView text1 = new TextView(this);
            ImageView img2 = new ImageView(this);
            TextView text3 = new TextView(this);
            TextView text4 = new TextView(this);
            //Create a view that between two rows.
            View line = new View(this);
            line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 5));
            line.setBackgroundColor(Color.rgb(49, 49, 58));
            try {
                JSONObject data = dailyData.getJSONObject(i);
                //Convert time to MM/DD/YYYY format.
                Date date = new Date(Long.parseLong(data.getString("time"))*1000);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
                String year = Integer.toString(calendar.get(Calendar.YEAR));
                if (month.length() < 2){
                    month = "0" + month;
                }
                if (day.length() < 2){
                    day = "0" + day;
                }
                String time = "   "+month + "/" + day + "/" + year;
                text1.setText(time);

                String icon = data.getString("icon");
                switch (icon){
                    case "clear-night":
                        img2.setImageResource(R.drawable.weather_night);
                        break;
                    case "rain":
                        img2.setImageResource(R.drawable.weather_rainy);
                        break;
                    case "snow":
                        img2.setImageResource(R.drawable.weather_snowy);
                        break;
                    case "sleet":
                        img2.setImageResource(R.drawable.weather_snowy_rainy);
                        break;
                    case "wind":
                        img2.setImageResource(R.drawable.weather_windy_variant);
                        break;
                    case "fog":
                        img2.setImageResource(R.drawable.weather_fog);
                        break;
                    case "cloudy":
                        img2.setImageResource(R.drawable.weather_cloudy);
                        break;
                    case "partly-cloudy-day":
                        img2.setImageResource(R.drawable.weather_partly_cloudy);
                        break;
                    case "partly-cloudy-night":
                        img2.setImageResource(R.drawable.weather_night_partly_cloudy);
                        break;
                    default:
                        img2.setImageResource(R.drawable.weather_sunny);
                }
                String temperatureLow = Integer.toString(Math.round(Float.parseFloat(data.getString("temperatureLow"))));
                text3.setText(" "+temperatureLow);
                String temperatureHigh = Integer.toString(Math.round(Float.parseFloat(data.getString("temperatureHigh"))));
                text4.setText(temperatureHigh);
                text1.setTextColor(Color.parseColor("#ffffff"));
                text3.setTextColor(Color.parseColor("#ffffff"));
                text4.setTextColor(Color.parseColor("#ffffff"));
                text1.setTextSize(20);
                img2.setLayoutParams(new TableRow.LayoutParams(110,110));
                text3.setTextSize(28);
                text4.setTextSize(28);
                tr.addView(text1);
                tr.addView(img2);
                tr.addView(text3);
                tr.addView(text4);
                tr.setPadding(0,36,0,30);
                tr.setGravity(Gravity.CENTER);
                tableView.addView(tr);
                if (i < dailyData.length()-1){
                    tableView.addView(line);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
