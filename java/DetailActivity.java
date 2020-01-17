package com.example.weatherapp;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends AppCompatActivity {

    private int[] tabIcons = new int[]{R.drawable.calendar_today, R.drawable.trending_up, R.drawable.google_photos};
    private TextView city;
    private ImageView twitter;

    private RequestQueue requestQueue;

    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        requestQueue = Volley.newRequestQueue(this);
        //add a tool bar and enable an up arrow
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get city state country from main activity
        city = findViewById(R.id.city);
        Intent intent = getIntent();
        city.setText(intent.getStringExtra("city"));
        cityName = intent.getStringExtra("cityName");
        String weather = intent.getStringExtra("json");
        //SectionsPagerAdapter
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.setJSONString(weather);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        //tabs
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons(tabs);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                }
                else if (position == 1){
                    tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                }
                else {
                    tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    tabs.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        //Twitter
        try {
            JSONObject jsonObject = new JSONObject(weather);
            final int temp = Math.round(Float.parseFloat(jsonObject.getJSONObject("currently").getString("temperature")));
            twitter = findViewById(R.id.twitter);
            twitter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openTwitter(temp);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //CityPhotos
        String cityString = "City="+cityName;
        String url = "https://weather-search-259408.appspot.com/cityphoto?"+cityString;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sectionsPagerAdapter.setcityPhotos(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }
    private void setupTabIcons(TabLayout tabs) {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void openTwitter(int temp){
        Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=Check Out "+city.getText()+"'s Weather! It is "+temp+"°F! %23CSCI571WeatherSearch");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}