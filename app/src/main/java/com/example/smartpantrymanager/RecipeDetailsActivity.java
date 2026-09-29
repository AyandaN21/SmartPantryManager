package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_details
        );

        // Connect the views
        ImageView imgRecipe =
                findViewById(
                        R.id.imgDetailRecipe
                );

        TextView tvRecipeName =
                findViewById(
                        R.id.tvDetailRecipeName
                );

        TextView tvIngredients =
                findViewById(
                        R.id.tvDetailIngredients
                );

        TextView tvInstructions =
                findViewById(
                        R.id.tvDetailInstructions
                );

        // Receive recipe information
        String name =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        String ingredients =
                getIntent().getStringExtra(
                        "recipe_ingredients"
                );

        String instructions =
                getIntent().getStringExtra(
                        "recipe_instructions"
                );

        int imageResource =
                getIntent().getIntExtra(
                        "recipe_image",
                        0
                );

        // Display recipe information
        tvRecipeName.setText(name);

        tvIngredients.setText(
                ingredients
        );

        tvInstructions.setText(
                instructions
        );

        // Display recipe image
        if (imageResource != 0) {
            imgRecipe.setImageResource(
                    imageResource
            );
        }
    }
}