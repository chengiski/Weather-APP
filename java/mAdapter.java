package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<mAdapter.mViewHolder> {
    private ArrayList<String> mList;
    private Context context;

    public static class mViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public mViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.cityPhoto);
        }
    }
    public mAdapter(ArrayList<String> list, Context context){
        this.mList = list;
        this.context = context;
    }
    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photolist, parent, false);
        mViewHolder mvh = new mViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        String url = mList.get(position);
        Picasso.with(context).load(url).centerCrop().resize(1390,928 ).into(holder.imageView);
        //.error(R.drawable.progress_download)
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
