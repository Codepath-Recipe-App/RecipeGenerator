package com.example.recipegenerator;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Ingredients")
public class Ingredients extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_INGREDIENTS = "ingredients";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public String getIngredients() {
        return getString(KEY_INGREDIENTS);
    }

    public void setIngredients(String ingredients) {
        put(KEY_INGREDIENTS, ingredients);
    }
}
