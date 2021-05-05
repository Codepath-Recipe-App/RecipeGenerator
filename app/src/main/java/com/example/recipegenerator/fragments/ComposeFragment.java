package com.example.recipegenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipegenerator.Ingredients;
import com.example.recipegenerator.ItemsAdapter;
import com.example.recipegenerator.LoginActivity;
import com.example.recipegenerator.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {

    public static String ingredients;
    List<String> items;

    String ingredientsObjectId;

    public static final String TAG = "ComposeFragment";
    Button btnAdd;
    Button btnGenerate;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    public ComposeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listensers.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        etItem = view.findViewById(R.id.etItem);
        rvItems = view.findViewById(R.id.rvItems);

        //items = new ArrayList<>(); //delete later when able to save to parse server
        /*items.add("apples");
        items.add("flour");
        items.add("sugar"); */

        //      loadItems();
        //queryIngredients();
        if (!(items == null) && items.size() > 0) {
            items = LoginActivity.ingItems;
        } else {
            loadItems();
        }
        Log.i("AfterqueryIngredients:", String.valueOf(items));

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientItem = etItem.getText().toString();
                // Add item to the model
                items.add(ingredientItem);
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                saveItems();
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lead to next screen of generated recipes from the api call
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveIngredients(currentUser);
                //Log.i("aftersaveUpdate", ingredients);//String.valueOf(items));
            }
        });
        Log.i("endquery", String.valueOf(items));
    }

  /*  protected void queryIngredients() {
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
                items = new ArrayList<>();
                String[] i = ing.split(",[+]");
                for(String n: i) {
                    items.add(n);
                }
                Log.i("queryIngredients: ", String.valueOf(items));
                itemsAdapter.notifyDataSetChanged();
            }
        });
        //return items;
    }
*/


    private void saveIngredients(ParseUser currentUser) {
        //Ingredients ingredientsObject = new Ingredients();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("user", currentUser);
        ingredientsObjectId = "s6O487dklf"; // change later to dynamically catch object id
        query.getInBackground(ingredientsObjectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with saving ingredients " + ingredientsObjectId, e);
                    return;
                }
                ingredients = "";
                for (String i: items) {  // place in format needed for api call, ex:  "apples,+sugar,+flour"
                    ingredients += i + ",+";
                }
                ingredients = ingredients.substring(0, ingredients.length() - 2); // get rid of last ",+"
                Log.i("savedIngredients: ", ingredients);

                object.put("ingredients", ingredients);
                object.saveInBackground();

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new GenerateFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
    }

    public File getDatafile() {
        Log.i("ComposeFragment", String.valueOf(getContext().getFilesDir()));
        return new File(getContext().getFilesDir(), "data.txt");
    }

    public void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDatafile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("ComposeFragment", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDatafile(), items);
        } catch (IOException e) {
            Log.e("ComposeFragment", "Error writing items", e);
        }
    }

}