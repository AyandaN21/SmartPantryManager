package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;
    private TextView tvRecipeMessage;

    private ArrayList<Recipe> allRecipes;
    private ArrayList<Recipe> suggestedRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_suggested_recipes
        );


        recyclerRecipes =
                findViewById(
                        R.id.recyclerRecipes
                );


        tvRecipeMessage =
                findViewById(
                        R.id.tvRecipeMessage
                );


        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // Load recipes from SQLite
        loadRecipesFromDatabase();


        // Find recipes the user can make
        findSuggestedRecipes();
    }


    // =========================================================
    // LOAD RECIPES FROM DATABASE
    // =========================================================

    private void loadRecipesFromDatabase() {

        RecipeDataSource recipeDataSource =
                new RecipeDataSource(this);


        recipeDataSource.open();


        allRecipes =
                recipeDataSource.getAllRecipes();


        recipeDataSource.close();
    }


    // =========================================================
    // FIND SUGGESTED RECIPES
    // =========================================================

    private void findSuggestedRecipes() {

        PantryDataSource dataSource =
                new PantryDataSource(this);


        dataSource.open();


        ArrayList<Ingredient> pantryIngredients =
                dataSource.getAllIngredients();


        dataSource.close();


        suggestedRecipes =
                new ArrayList<>();


        // Check every recipe
        for (Recipe recipe : allRecipes) {

            boolean canMakeRecipe = true;


            // Check every ingredient required by the recipe
            for (RecipeIngredient requiredIngredient :
                    recipe.getRequiredIngredients()) {


                boolean enoughIngredient =
                        hasEnoughIngredient(
                                requiredIngredient,
                                pantryIngredients
                        );


                /*
                 * STRICT MATCHING:
                 *
                 * If ONE required ingredient is:
                 *
                 * - missing
                 * - too small in quantity
                 * - using an incompatible unit
                 *
                 * then the recipe must not appear.
                 */

                if (!enoughIngredient) {

                    canMakeRecipe = false;

                    break;
                }
            }


            if (canMakeRecipe) {

                suggestedRecipes.add(
                        recipe
                );
            }
        }


        // =====================================================
        // DISPLAY MESSAGE
        // =====================================================

        if (suggestedRecipes.isEmpty()) {

            tvRecipeMessage.setText(
                    "No recipes available yet. " +
                            "Add more ingredients or increase " +
                            "the quantities in your pantry."
            );

        } else {

            tvRecipeMessage.setText(
                    "You can make "
                            + suggestedRecipes.size()
                            + " recipe(s) with your pantry."
            );
        }


        // =====================================================
        // DISPLAY RECIPES
        // =====================================================

        RecipeAdapter adapter =
                new RecipeAdapter(
                        this,
                        suggestedRecipes
                );


        recyclerRecipes.setAdapter(
                adapter
        );
    }


    // =========================================================
    // CHECK IF PANTRY HAS ENOUGH OF ONE INGREDIENT
    // =========================================================

    private boolean hasEnoughIngredient(
            RecipeIngredient requiredIngredient,
            ArrayList<Ingredient> pantryIngredients) {


        /*
         * We add together matching pantry entries.
         *
         * Example:
         *
         * Eggs 1 piece
         * Eggs 2 pieces
         *
         * Total = 3 pieces
         */

        double totalAvailable = 0;


        String requiredName =
                normalizeIngredientName(
                        requiredIngredient.getName()
                );


        String requiredUnit =
                normalizeUnit(
                        requiredIngredient.getUnit()
                );


        for (Ingredient pantryIngredient :
                pantryIngredients) {


            String pantryName =
                    normalizeIngredientName(
                            pantryIngredient.getName()
                    );


            // Ingredient names must match
            if (!requiredName.equals(pantryName)) {

                continue;
            }


            String pantryUnit =
                    normalizeUnit(
                            pantryIngredient.getUnit()
                    );


            /*
             * Convert both quantities into a common
             * measurement where possible.
             */

            double convertedPantryQuantity =
                    convertQuantity(
                            pantryIngredient.getQuantity(),
                            pantryUnit,
                            requiredUnit
                    );


            /*
             * -1 means the units are not compatible.
             *
             * Example:
             *
             * Chicken 1 litre
             * cannot satisfy
             * Chicken 150 g
             */

            if (convertedPantryQuantity >= 0) {

                totalAvailable +=
                        convertedPantryQuantity;
            }
        }


        return totalAvailable >=
                requiredIngredient.getRequiredQuantity();
    }


    // =========================================================
    // NORMALIZE INGREDIENT NAMES
    // =========================================================

    private String normalizeIngredientName(
            String name) {


        String normalized =
                name.trim()
                        .toLowerCase(Locale.ROOT);


        /*
         * Handle common plural forms used
         * by our recipe collection.
         */

        if (normalized.equals("eggs")) {
            return "egg";
        }


        if (normalized.equals("tomatoes")) {
            return "tomato";
        }


        if (normalized.equals("potatoes")) {
            return "potato";
        }


        if (normalized.equals("berries")) {
            return "berry";
        }


        if (normalized.equals("bananas")) {
            return "banana";
        }


        if (normalized.equals("avocados")) {
            return "avocado";
        }


        if (normalized.equals("wraps")) {
            return "wrap";
        }


        return normalized;
    }


    // =========================================================
    // NORMALIZE UNITS
    // =========================================================

    private String normalizeUnit(
            String unit) {


        String normalized =
                unit.trim()
                        .toLowerCase(Locale.ROOT);


        // -------------------------
        // PIECES
        // -------------------------

        if (normalized.equals("piece")
                || normalized.equals("pieces")
                || normalized.equals("pc")
                || normalized.equals("pcs")
                || normalized.equals("unit")
                || normalized.equals("units")) {

            return "piece";
        }


        // -------------------------
        // SLICES
        // -------------------------

        if (normalized.equals("slice")
                || normalized.equals("slices")) {

            return "slice";
        }


        // -------------------------
        // GRAMS
        // -------------------------

        if (normalized.equals("g")
                || normalized.equals("gram")
                || normalized.equals("grams")) {

            return "g";
        }


        // -------------------------
        // KILOGRAMS
        // -------------------------

        if (normalized.equals("kg")
                || normalized.equals("kilogram")
                || normalized.equals("kilograms")) {

            return "kg";
        }


        // -------------------------
        // MILLILITRES
        // -------------------------

        if (normalized.equals("ml")
                || normalized.equals("millilitre")
                || normalized.equals("millilitres")
                || normalized.equals("milliliter")
                || normalized.equals("milliliters")) {

            return "ml";
        }


        // -------------------------
        // LITRES
        // -------------------------

        if (normalized.equals("l")
                || normalized.equals("litre")
                || normalized.equals("litres")
                || normalized.equals("liter")
                || normalized.equals("liters")) {

            return "l";
        }


        // -------------------------
        // CUPS
        // -------------------------

        if (normalized.equals("cup")
                || normalized.equals("cups")) {

            return "cup";
        }


        // -------------------------
        // TABLESPOONS
        // -------------------------

        if (normalized.equals("tbsp")
                || normalized.equals("tablespoon")
                || normalized.equals("tablespoons")) {

            return "tbsp";
        }


        // -------------------------
        // TEASPOONS
        // -------------------------

        if (normalized.equals("tsp")
                || normalized.equals("teaspoon")
                || normalized.equals("teaspoons")) {

            return "tsp";
        }


        return normalized;
    }


    // =========================================================
    // CONVERT QUANTITY
    // =========================================================

    private double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit) {


        // Same unit - no conversion needed
        if (fromUnit.equals(toUnit)) {

            return quantity;
        }


        // =====================================================
        // MASS CONVERSIONS
        // =====================================================

        // Kilograms -> grams
        if (fromUnit.equals("kg")
                && toUnit.equals("g")) {

            return quantity * 1000;
        }


        // Grams -> kilograms
        if (fromUnit.equals("g")
                && toUnit.equals("kg")) {

            return quantity / 1000;
        }


        // =====================================================
        // VOLUME CONVERSIONS
        // =====================================================

        // Litres -> millilitres
        if (fromUnit.equals("l")
                && toUnit.equals("ml")) {

            return quantity * 1000;
        }


        // Millilitres -> litres
        if (fromUnit.equals("ml")
                && toUnit.equals("l")) {

            return quantity / 1000;
        }


        /*
         * Units are incompatible.
         *
         * We return -1 so the ingredient
         * is not counted.
         */

        return -1;
    }
}