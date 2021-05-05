package com.example.recipegenerator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipegenerator.R;
import com.example.recipegenerator.fragments.adapters.RecipeAdapter;
import com.example.recipegenerator.fragments.adapters.SavedRecipe;
import com.example.recipegenerator.fragments.models.Recipe;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView tvUsernameProfile;
    ImageView ivProfile;
    RecyclerView rvSavedRecipes;
    List<ParseObject> savedRecipes;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSavedRecipes = view.findViewById(R.id.rvSavedRecipes);
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        ivProfile = view.findViewById(R.id.ivProfile);

        savedRecipes = new ArrayList<>();
        SavedRecipe recipeAdapter = new SavedRecipe(getContext(), savedRecipes);
        rvSavedRecipes.setAdapter(recipeAdapter);
        rvSavedRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("User");
        queryUser.whereEqualTo("objectId", "aWTaAJp1D6"); //ParseUser.getCurrentUser());
        queryUser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
               // Log.i("ProfileFragment", String.valueOf(objects.get(0)));
                try {
                    ParseObject object = objects.get(0);
                    tvUsernameProfile.setText(object.getString("username"));
                    Glide.with(getContext()).load(object.getString("profileImg")).into(ivProfile);
                } catch (Exception exception) {
                    Log.i("ProfileFragment", "no user object");
                }
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                savedRecipes.addAll(parseObjects);
                Log.i("ProfileFragment", savedRecipes.get(0).getString("title"));
                recipeAdapter.notifyDataSetChanged();
            }
        });

    }

}
