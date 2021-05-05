package com.example.recipegenerator.fragments.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipegenerator.R;
import com.example.recipegenerator.fragments.DetailsFragment;
import com.example.recipegenerator.fragments.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    Context context;
    List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
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
        Recipe recipe = recipes.get(position);
        // Bind the movie data into the VH
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvMissingIng;
        ImageView ivPhoto;
        ViewHolder INSTANCE;
        //Recipe data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle2);
            tvMissingIng = itemView.findViewById(R.id.tvMissingIng2);
            ivPhoto = itemView.findViewById(R.id.ivPhoto2);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Recipe recipe) {
            tvTitle.setText(recipe.getTitle());
            tvMissingIng.setText(recipe.getMissingIng());
            Log.i("RecipeAdapter", recipe.getMissingIng());
            //data = recipe;
            Glide.with(context).load(recipe.getPhotoPath()).into(ivPhoto);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, recipe.getTitle(), Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

                    Bundle arguments = new Bundle();
                    arguments.putString("title", recipe.getTitle());
                    arguments.putString("missedIngredients", recipe.getMissingIng());
                    arguments.putString("image", recipe.getPhotoPath());
                    arguments.putString("id", recipe.getRecipeId());

                    Fragment fragment = new DetailsFragment();
                    fragment.setArguments(arguments);//
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    //Intent i = new Intent(context, DetailsFragment.class);
                    //i.putExtra("recipe", Parcels.wrap(recipe));
                    //context.startActivity(i);
                }
            });
        }

        public ViewHolder getActivityInstance() {
            return INSTANCE;
        }

    }
}
