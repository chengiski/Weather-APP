package com.example.weatherapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    private String weather;
    private TextView windView;
    private TextView pressView;
    private TextView precView;
    private TextView tempView;
    private TextView sumView;
    private TextView humidView;
    private TextView visView;
    private TextView cloudView;
    private TextView ozoneView;

    private TextView iconText;
    private ImageView iconView;

    private OnFragmentInteractionListener mListener;
    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param s Parameter 1.
     * @return A new instance of fragment TodayFragment.
     */
    public static TodayFragment newInstance(String s) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString("weather",s);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        windView = getView().findViewById(R.id.windSpeed);
        pressView = getView().findViewById(R.id.pressure);
        precView = getView().findViewById(R.id.precipitation);
        tempView = getView().findViewById(R.id.temp);
        sumView = getView().findViewById(R.id.summary);
        humidView = getView().findViewById(R.id.humidity);
        visView = getView().findViewById(R.id.visibility);
        cloudView = getView().findViewById(R.id.cloudCover);
        ozoneView = getView().findViewById(R.id.ozone);
        iconText = getView().findViewById(R.id.iconText);
        iconView = getView().findViewById(R.id.icon);

        try {
            JSONObject jsonObject = new JSONObject(weather);
            JSONObject currently = jsonObject.getJSONObject("currently");

            float windSpeed = ((float)Math.round(Float.parseFloat(currently.getString("windSpeed"))*100))/100;
            windView.setText(windSpeed+" mph");
            float press = ((float)Math.round(Float.parseFloat(currently.getString("pressure"))*100))/100;
            pressView.setText(press+" mb");
            float prec = ((float)Math.round(Float.parseFloat(currently.getString("precipIntensity"))*100))/100;
            precView.setText(prec+" mmph");
            int temp = Math.round(Float.parseFloat(currently.getString("temperature")));
            tempView.setText(temp+"°F");
            int humid = Math.round(Float.parseFloat(currently.getString("humidity"))*100);
            humidView.setText(humid+"%");
            float vis = ((float)Math.round(Float.parseFloat(currently.getString("visibility"))*100))/100;
            visView.setText(vis+" km");
            int cloud = Math.round(Float.parseFloat(currently.getString("cloudCover"))*100);
            cloudView.setText(cloud+"%");
            float ozone = ((float)Math.round(Float.parseFloat(currently.getString("ozone"))*100))/100;
            ozoneView.setText(ozone+" DU");

            String icon = currently.getString("icon");
            switch (icon){
                case "clear-night":
                    iconView.setImageResource(R.drawable.weather_night);
                    iconText.setText("clear night");
                    break;
                case "rain":
                    iconView.setImageResource(R.drawable.weather_rainy);
                    iconText.setText("rain");
                    break;
                case "snow":
                    iconView.setImageResource(R.drawable.weather_snowy);
                    iconText.setText("snow");
                    break;
                case "sleet":
                    iconView.setImageResource(R.drawable.weather_snowy_rainy);
                    iconText.setText("sleet");
                    break;
                case "wind":
                    iconView.setImageResource(R.drawable.weather_windy_variant);
                    iconText.setText("wind");
                    break;
                case "fog":
                    iconView.setImageResource(R.drawable.weather_fog);
                    iconText.setText("fog");
                    break;
                case "cloudy":
                    iconView.setImageResource(R.drawable.weather_cloudy);
                    iconText.setText("cloudy");
                    break;
                case "partly-cloudy-day":
                    iconView.setImageResource(R.drawable.weather_partly_cloudy);
                    iconText.setText("cloudy day");
                    break;
                case "partly-cloudy-night":
                    iconView.setImageResource(R.drawable.weather_night_partly_cloudy);
                    iconText.setText("cloudy night");
                    break;
                default:
                    iconView.setImageResource(R.drawable.weather_sunny);
                    iconText.setText("clear day");
            }
        }
        catch (JSONException e) {
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
        return inflater.inflate(R.layout.fragment_today, container, false);
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
