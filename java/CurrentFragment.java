package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentFragment extends Fragment {

    private String cityName;
    private String cityStateCountry;
    private JSONObject currWeather;
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
    //Card one's view
    private CardView card_view;

    private OnFragmentInteractionListener mListener;

    public CurrentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentFragment.
     */
    public static CurrentFragment newInstance(String cityName, String cityStateCountry, String currWeather) {
        CurrentFragment fragment = new CurrentFragment();
        Bundle args = new Bundle();
        args.putString("cityName",cityName);
        args.putString("cityStateCountry",cityStateCountry);
        args.putString("currWeather",currWeather);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString("cityName");
            cityStateCountry = getArguments().getString("cityStateCountry");
            try {
                currWeather = new JSONObject(getArguments().getString("currWeather"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Card one.
        tempView = getView().findViewById(R.id.temp);
        sumView = getView().findViewById(R.id.summary);
        cityView = getView().findViewById(R.id.city);
        iconView = getView().findViewById(R.id.icon);
        //Card two.
        humidView = getView().findViewById(R.id.humid);
        windView = getView().findViewById(R.id.windSpeed);
        visibView = getView().findViewById(R.id.visibility);
        pressView = getView().findViewById(R.id.pressure);
        //Card three
        tableView = (TableLayout)getView().findViewById(R.id.table);
        //Click to open DetailActivity activity.
        card_view = getView().findViewById(R.id.card_view1);
        card_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDetailActivity();
            }
        });
        setView();
    }

    private void setView(){
        cityView.setText(cityStateCountry);
        try {
            JSONObject currently = currWeather.getJSONObject("currently");
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
            JSONArray dailyData = currWeather.getJSONObject("daily").getJSONArray("data");
            createTable(dailyData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //Create table in card three
    private void createTable(JSONArray dailyData){
        for (int i = 0; i < dailyData.length(); i++){
            TableRow tr =  new TableRow(MainActivity.context);
            TextView text1 = new TextView(MainActivity.context);
            ImageView img2 = new ImageView(MainActivity.context);
            TextView text3 = new TextView(MainActivity.context);
            TextView text4 = new TextView(MainActivity.context);
            //Create a view that between two rows.
            View line = new View(MainActivity.context);
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
    //Click card one to open detail activity
    public void openDetailActivity(){
        Intent intent = new Intent(MainActivity.context, DetailActivity.class);
        intent.putExtra("city", cityStateCountry);
        intent.putExtra("cityName", cityName);
        intent.putExtra("json", currWeather.toString());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
