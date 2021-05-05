package com.example.recipegenerator.fragments.models;

import android.graphics.Movie;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    String title;
    String photoPath;
    String missingIng;
    String summary; // {id}/summary  call here?
    String recipeId;

    public Recipe(JSONObject jsonObject) throws JSONException { // , JSONObject jsonSummary   // if sending both objects together
        photoPath = jsonObject.getString("image");
        title = jsonObject.getString("title");
        recipeId = String.valueOf(jsonObject.getInt("id"));
        JSONArray missIng = jsonObject.getJSONArray("missedIngredients"); // [{name:""}, {}]
        missingIng = "Missing Ingredients: ";
        for (int i = 0; i < missIng.length(); i++) {
            JSONObject row = missIng.getJSONObject(i);
            missingIng += row.getString("name") + ", ";
        }
        missingIng = missingIng.substring(0, missingIng.length() - 2);
        //summary = jsonSummary.getString("summary");  // if sending both objects together
    }

    public static List<Recipe> fromJsonArray(JSONArray recipeJsonArray) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipeJsonArray.length(); i++) {
            recipes.add(new Recipe(recipeJsonArray.getJSONObject(i)));
        }
        return recipes;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getMissingIng() {
        return missingIng;
    }

    public String getSummary() {
        return summary;
    }
}
