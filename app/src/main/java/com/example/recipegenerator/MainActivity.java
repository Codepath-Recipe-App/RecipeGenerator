package com.example.recipegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    Button btnGenerate;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnGenerate = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        items = new ArrayList<>(); //delete later when able to save to parse server
        items.add("apples");
        items.add("flour");
        items.add("sugar");

  //      loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
 //               saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientItem = etItem.getText().toString();
                // Add item to the model
                items.add(ingredientItem);
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
  //              saveItems();
            }
        });
    }

 /*   private String[] getIngredientsArray() {
        String[] ingredientsArray;
        try {
            file = ParseUser.getCurrentUser().getParseFile("ingredients");
        } catch (Exception e) {
            ParseObject.put("ingredients", new file.createNewFile());
        }
    }
*/
    // This function will load items by getting from Parse Server
/*    private void loadItems() {
        //initialize items
        try {
            items = (ArrayList<String>) ParseUser.getCurrentUser().get("ingredients");
        } catch (Exception e) {
            items = new ArrayList<String>();
        }
    }
*/
    // This function saves items by posting to Parse Server
/*    private void saveItems() {
        String[] saveIngredient = new String[items.size()];
        for (int i = 0; i<items.size(); i++) saveIngredient[i] = items.get(i);
        //save items as array to Parse Server
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //Retrieve the object by id
        query.getInBackground("aWTaAJp1D6", ((object, e) -> {
            if (e == null) {
                object.put("ingredients", saveIngredient);
            } else {
                Toast.makeText(this, "saveItems error", Toast.LENGTH_SHORT).show();
            }
        }));
    }

 */
}