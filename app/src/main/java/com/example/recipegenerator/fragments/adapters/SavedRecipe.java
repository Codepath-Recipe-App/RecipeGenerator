package com.example.recipegenerator.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipegenerator.R;
import com.example.recipegenerator.fragments.models.Recipe;
import com.parse.ParseObject;

import java.util.List;

public class SavedRecipe extends RecyclerView.Adapter<SavedRecipe.ViewHolder>{


    Context context;
    List<ParseObject> savedrecipes;

    public SavedRecipe(Context context, List<ParseObject> recipes) {
        this.context = context;
        this.savedrecipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the movie at the passed in position
        ParseObject recipe = savedrecipes.get(position);
        // Bind the movie data into the VH
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return savedrecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

  //      RelativeLayout container;
        TextView tvTitle2;
        TextView tvMissingIng2;
        ImageView ivPhoto2;
        ViewHolder INSTANCE;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle2 = itemView.findViewById(R.id.tvTitle2);
            tvMissingIng2 = itemView.findViewById(R.id.tvMissingIng2);
            ivPhoto2 = itemView.findViewById(R.id.ivPhoto2);
 //           container = itemView.findViewById(R.id.container);
        }

        public void bind(ParseObject recipe) {
            tvTitle2.setText(recipe.getString("title"));
            Log.i("SavedRecipe", recipe.getString("title"));
            tvMissingIng2.setText(recipe.getString("summary"));
            Glide.with(context).load(recipe.getString("imageUrl")).into(ivPhoto2);
        }

        public ViewHolder getActivityInstance() {
            return INSTANCE;
        }

    }

}
