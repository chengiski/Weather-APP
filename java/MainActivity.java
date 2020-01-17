package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static Context context;
    private RequestQueue requestQueue;
    //The map from SharedPreferences
    private Map<String, ?> cityMap;
    private int mapSize;
    //arraylist contains all JSONObject
    private ArrayList<String> JSONList;
    private ArrayList<String> cityNames;
    //Current Location
    private String cityName;
    private String cityStateCountry;
    private String currWeather;
    //progress bar
    private ConstraintLayout progressBar;
    private ConstraintLayout container;
    //tabs
    public static TabLayout tabs;
    public static MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        MainActivity.context = getApplicationContext();
        //progress bar
        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.container);
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        jsonRequest();
        cityNames = new ArrayList<>();
        JSONList = new ArrayList<>();
    }
    //Get data from SharedPreferences and use a map to get all of them.
    private void makeMap(){
        SharedPreferences favors = getSharedPreferences(
                "com.example.weatherapp", MODE_PRIVATE);
        cityMap = favors.getAll();
        mapSize = cityMap.size();
        if (mapSize == 0){
            //MainAdapter
            mainAdapter = new MainAdapter(context, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(mainAdapter);
            //Set up tabs
            tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager,true);
            //progress bar disappear
            progressBar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }else{
            for (Map.Entry<String,?> entry : cityMap.entrySet()){
                String city = entry.getKey();
                jasonWeather(city);
            }
        }
    }
    //Get current location.
    private void jsonRequest() {
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String lat = response.getString("lat");
                            String lon = response.getString("lon");
                            jsonWeather(lat, lon);
                            cityName = response.getString("city");
                            cityStateCountry = cityName+", "+response.getString("region")+", "+response.getString("countryCode");

                        } catch (JSONException e) {
                            e.printStackTrace();
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
    private void jsonWeather(String lat, String lon){
        String location = "Lat="+lat+"&Lon="+lon;
        String url = "https://weather-search-259408.appspot.com/currweather?"+location;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        currWeather = response.toString();
                        makeMap();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    //use city and country to get weather
    private void jasonWeather(String s){
        final String city = s;
        String address = "street=&city=&state="+city;
        String url = "https://weather-search-259408.appspot.com/weather?"+address;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        cityNames.add(city);
                        JSONList.add(response.toString());
                        //Check if all api call are done
                        if (JSONList.size() == mapSize){
                            //MainAdapter
                            mainAdapter = new MainAdapter(context, getSupportFragmentManager());
                            ViewPager viewPager = findViewById(R.id.view_pager);
                            viewPager.setAdapter(mainAdapter);
                            //Set up tabs
                            tabs = findViewById(R.id.tabs);
                            tabs.setupWithViewPager(viewPager,true);
                            //progress bar disappear
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
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

    //Search View and Auto Complete
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        //autoComplete
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setBackgroundColor(Color.parseColor("#1e1e1e"));
        searchAutoComplete.setDropDownBackgroundResource(R.color.white);
        final AutoAdapter adapter = new AutoAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(adapter);

        //ImageView searchClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        //searchClose.setBackgroundColor(Color.BLACK);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("test1", query);
                Intent intent = new Intent(MainActivity.context, SearchResult.class);
                Log.d("test1", "111111111");
                intent.putExtra("cityForSearch", query);
                Log.d("test1", "22222222");
                startActivity(intent);
                Log.d("test1", "3333333333");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String url = "https://weather-search-259408.appspot.com/autocomplete?City="+newText;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ArrayList<String> autoNames = new ArrayList<>();
                                    for (int i = 0; i < response.getJSONArray("predictions").length(); i++){
                                        String autoName = response.getJSONArray("predictions").getJSONObject(i).getString("description");
                                        autoNames.add(autoName);
                                    }

                                    String[] tempArr = new String[autoNames.size()];

                                    for(int i = 0; i < autoNames.size(); i++){
                                        tempArr[i] = autoNames.get(i);
                                    }
                                    //auto complete
                                    adapter.setData(autoNames);
                                    adapter.notifyDataSetChanged();

                                    searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            searchAutoComplete.setText("" + adapterView.getItemAtPosition(position));
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                requestQueue.add(jsonObjectRequest);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class MainAdapter extends FragmentStatePagerAdapter {
        private int counter;
        public MainAdapter(Context context, FragmentManager fm) {
            super(fm);
            counter = mapSize+1;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return CurrentFragment.newInstance(cityName, cityStateCountry, currWeather);
            }else{
                return DynamicFragment.newInstance(position, JSONList, cityNames);
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return counter;
        }

        //This is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }
        //Delete a page at a position
        public void removePage(int position) {
            counter--;
            JSONList.remove(position-1);
            cityNames.remove(position-1);
            // Notify the adapter that the data set is changed
            notifyDataSetChanged();
        }

    }

}

