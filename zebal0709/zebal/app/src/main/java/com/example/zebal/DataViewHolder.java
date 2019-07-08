package com.example.zebal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataViewHolder extends RecyclerView.ViewHolder {

    TextView name, number;
    ImageView profileImage;
    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.row_name);
        number = itemView.findViewById(R.id.row_number);
        profileImage = itemView.findViewById(R.id.row_image);
    }
}
