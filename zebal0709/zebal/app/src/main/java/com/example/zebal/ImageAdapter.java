package com.example.zebal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    ArrayList<ImageList> list;
    Context context;


    public ImageAdapter(ArrayList<ImageList> list, Context context){
        this.list = list;
        this.context = context;
    }

    public ImageAdapter(){
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gallery_image, viewGroup, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, int position){
        ImageList currentList = list.get(position);
        if (currentList.getImage() == null){
            Log.d("Image in currentList", "empty(function onBindViewHolder)");
        }
        else {
            Picasso.with(context).load(currentList.getImage()).placeholder(R.drawable.gallery_image).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}