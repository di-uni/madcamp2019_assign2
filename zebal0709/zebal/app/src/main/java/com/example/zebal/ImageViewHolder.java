package com.example.zebal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.gallery_image);
    }
}
