package com.example.recipegenerator.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipegenerator.BuildConfig;
import com.example.recipegenerator.R;
import com.example.recipegenerator.fragments.adapters.RecipeAdapter;
import com.example.recipegenerator.fragments.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class DetailsFragment extends Fragment {

    //public static final String FIND_BY_INGREDIENTS_URL = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=";
    public static final String SUMMARIZE_RECIPE_URL = "https://api.spoonacular.com/recipes/";//   {id}/summary";
    public String consumerKey = BuildConfig.SA_KEY; //build
    public static final String TAG = "DetailsFragment";

    TextView tvTitle2;
    TextView tvMissingIng2;
    ImageView ivPhoto2;
    TextView tvSummary;
    Button btnSave;
    String recipeid;
    String title;
    String summary;
    String photopath;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        tvMissingIng2 = view.findViewById(R.id.tvMissingIng2);
        tvSummary = view.findViewById(R.id.tvSummary);
        ivPhoto2 = view.findViewById(R.id.ivPhoto2);
        btnSave = view.findViewById(R.id.btnSave);

        //Recipe recipe = RecipeAdapter.ViewHolder.getData();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString("title", "");
            tvTitle2.setText(title);
            tvMissingIng2.setText(bundle.getString("missedIngredients", ""));
            photopath = bundle.getString("image", "");
            Glide.with(this).load(photopath).into(ivPhoto2);
            recipeid = bundle.getString("id", "");
        }
        String summaryUrl = SUMMARIZE_RECIPE_URL + recipeid + "/summary?apiKey=" + consumerKey;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(summaryUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.i(TAG, summaryUrl);
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                Log.i(TAG, "Results: " + jsonObject.toString());
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        summary = String.valueOf(Html.fromHtml(jsonObject.getString("summary"), Html.FROM_HTML_MODE_COMPACT));
                        tvSummary.setText(summary);
                    } else {
                        summary = jsonObject.getString("summary");
                        tvSummary.setText(summary);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveRecipe(currentUser);
            }
        });

    }


    private void saveRecipe(ParseUser currentUser) {
        ParseObject object = new ParseObject("Post");
        object.put("title", title);
        object.put("user", currentUser);
        object.put("recipeid", recipeid);
        object.put("summary", summary);
        object.put("imageUrl", photopath);
        object.saveInBackground();
    }

}
