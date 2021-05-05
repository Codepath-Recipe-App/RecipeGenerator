package com.example.recipegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenerator.fragments.ComposeFragment;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static List<String> ingItems = new ArrayList<>();
    public String ingredientsObjectId;
    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                queryIngredients();
                loginUser(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signupUser(username, password);
            }
        });
    }
    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to login user" + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signupUser(String username, String password) {
        Log.i(TAG, "Attempting to sign up user" + username);
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Issue with sign up", e);
                    Toast.makeText(LoginActivity.this, "Issue with sign up!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    protected void queryIngredients() {
        ParseQuery<Ingredients> query = ParseQuery.getQuery(Ingredients.class);
        query.include(Ingredients.KEY_USER);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Ingredients>() {
            @Override
            public void done(List<Ingredients> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting ingredients", e);
                    return;
                }
                ParseObject object = objects.get(0);  // error: ParseObject cannot be cast to Ingredients
                ingredientsObjectId = object.getObjectId();
                // split ingredients string into array list
                //String ing = "apples,+sugar,+flour"; // this format
                String ing = (object).getString("ingredients");
                ingItems = new ArrayList<>();
                String[] i = ing.split(",[+]");
                for(String n: i) {
                    ingItems.add(n);
                }
                Log.i("queryIngredients: ", String.valueOf(ingItems));
                //itemsAdapter.notifyDataSetChanged();
                saveItems();
            }
        });
        //saveItems();
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDatafile(), ingItems);
        } catch (IOException e) {
            Log.e("LoginActivity", "Error writing items", e);
        }
    }

    public File getDatafile() {
        Log.i("LoginActivity", String.valueOf(getFilesDir()));
        return new File(getFilesDir(), "data.txt");
    }
}
