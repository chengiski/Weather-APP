package com.example.weatherapp;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyFragment extends Fragment {

    private String weather;

    private ImageView iconView;
    private TextView sumText;

    private LineChart lineChart;

    private OnFragmentInteractionListener mListener;

    public WeeklyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param s Parameter 1.
     * @return A new instance of fragment WeeklyFragment.
     */
    public static WeeklyFragment newInstance(String s) {
        WeeklyFragment fragment = new WeeklyFragment();
        Bundle args = new Bundle();
        args.putString("weather",s);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        iconView = getView().findViewById(R.id.icon);
        sumText = getView().findViewById(R.id.summary);
        lineChart = getView().findViewById(R.id.lineChart);
        try {
            JSONObject jsonObject = new JSONObject(weather);
            sumText.setText(jsonObject.getJSONObject("daily").getString("summary"));
            String icon = jsonObject.getJSONObject("daily").getString("icon");
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
            //Create an ArrayList to draw line chart
            ArrayList<Entry> highArray = new ArrayList<Entry>();
            ArrayList<Entry> lowArray = new ArrayList<Entry>();
            JSONArray dailyData = jsonObject.getJSONObject("daily").getJSONArray("data");
            for (int i = 0; i < dailyData.length(); i++){
                Float high = Float.parseFloat(dailyData.getJSONObject(i).getString("temperatureHigh"));
                Float low = Float.parseFloat(dailyData.getJSONObject(i).getString("temperatureLow"));
                highArray.add(new Entry(i,high));
                lowArray.add(new Entry(i,low));
            }
            LineDataSet highTemp = new LineDataSet(highArray,"Maximum Temperature");
            highTemp.setColor(Color.parseColor("#faab1a"));
            LineDataSet lowTemp = new LineDataSet(lowArray,"Minimum Temperature");
            lowTemp.setColor(Color.parseColor("#bb86fc"));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lowTemp);
            dataSets.add(highTemp);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            lineChart.invalidate();

            //set Color
            lineChart.getAxisLeft().setTextColor(Color.WHITE);
            lineChart.getAxisRight().setTextColor(Color.WHITE);
            lineChart.getXAxis().setTextColor(Color.WHITE);
            //Set label color to white.
            lineChart.getLegend().setTextColor(Color.WHITE);
            lineChart.getLegend().setTextSize(15);
            highTemp.setFormSize(14);
            lowTemp.setFormSize(14);
            lineChart.getLegend().setXEntrySpace(20);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = getArguments().getString("weather");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
