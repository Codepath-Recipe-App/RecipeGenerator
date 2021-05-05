package com.example.recipegenerator.fragments;

import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipegenerator.BuildConfig;
import com.example.recipegenerator.R;
import com.example.recipegenerator.fragments.adapters.RecipeAdapter;
import com.example.recipegenerator.fragments.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

//import static com.example.recipegenerator.fragments.ComposeFragment.ingredients;

public class GenerateFragment extends Fragment {

    public static final String FIND_BY_INGREDIENTS_URL = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=";
    public static final String SUMMARIZE_RECIPE_URL = "https://api.spoonacular.com/recipes/";//   {id}/summary";
    public String consumerKey = "ad9c914a894a4e45929376dcd68dc922"; // BuildConfig.SA_KEY; //build
    public static final String TAG = "GenerateFragment";

    List<Recipe> recipes;

    private RecyclerView rvRecipes;

    public GenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //rvRecipes = view.findViewById(R.id.rvRecipes);
        String ingredientsURL = FIND_BY_INGREDIENTS_URL + ComposeFragment.ingredients + "&number=5&apiKey=" + consumerKey;
        recipes = new ArrayList<>();
        rvRecipes = view.findViewById(R.id.rvRecipes);

        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipes);
        rvRecipes.setAdapter(recipeAdapter); //maybe error because onCreate runs before onViewCreated
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ingredientsURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.i(TAG, ingredientsURL);
                Log.d(TAG, "onSuccess");
                JSONArray jsonArray = json.jsonArray;
                Log.i(TAG, "Results: " + jsonArray.toString());
                try {
                    recipes.addAll(Recipe.fromJsonArray(jsonArray));
                    recipeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}

