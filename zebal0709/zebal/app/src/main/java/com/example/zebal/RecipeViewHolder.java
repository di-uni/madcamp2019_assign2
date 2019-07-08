package com.example.zebal;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView food;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        food = itemView.findViewById(R.id.row_food);
    }

}
