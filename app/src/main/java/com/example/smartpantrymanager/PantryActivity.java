package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private Button btnAddIngredient;

    private IngredientAdapter ingredientAdapter;
    private ArrayList<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        // Connect Java to XML
        recyclerPantry =
                findViewById(R.id.recyclerPantry);

        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        // RecyclerView layout
        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Add Ingredient button
        btnAddIngredient.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        PantryActivity.this,
                                        AddEditIngredientActivity.class
                                );

                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadIngredients();
    }

    // Load ingredients from SQLite
    private void loadIngredients() {

        PantryDataSource dataSource =
                new PantryDataSource(this);

        dataSource.open();

        ingredientList =
                dataSource.getAllIngredients();

        dataSource.close();

        ingredientAdapter =
                new IngredientAdapter(
                        this,
                        ingredientList
                );

        recyclerPantry.setAdapter(
                ingredientAdapter
        );
    }
}