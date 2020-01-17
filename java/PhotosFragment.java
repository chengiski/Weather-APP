package com.example.weatherapp;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {

    private ArrayList<String> photoURL;
    private static Context contextDetail;
    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;


    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param s Parameter 1.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String s, Context context) {
        contextDetail = context;
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString("photo",s);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(contextDetail);
        mAdapter = new mAdapter(photoURL, contextDetail);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String cityPhotos = getArguments().getString("photo");
            try {
                JSONObject jsonObject = new JSONObject(cityPhotos);
                photoURL = new ArrayList<>();
                for (int i = 0; i < 8; i++){
                    String url = jsonObject.getJSONArray("items").getJSONObject(i).getString("link");
                    photoURL.add(url);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false);
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
