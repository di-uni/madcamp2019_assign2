package com.example.zebal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    ArrayList<RecipeList> list;
    Context context;

    public RecipeAdapter(ArrayList<RecipeList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeList currentList = list.get(position);
        holder.food.setText(currentList.getFood());
    }

    @Override
    public int getItemCount() { return list.size(); }
}
