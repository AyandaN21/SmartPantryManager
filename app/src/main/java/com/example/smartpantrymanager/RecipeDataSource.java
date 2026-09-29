package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RecipeDataSource {

    private SQLiteDatabase database;
    private PantryDBHelper dbHelper;
    private Context context;


    public RecipeDataSource(Context context) {

        this.context = context;
        dbHelper = new PantryDBHelper(context);
    }


    // =========================================================
    // OPEN DATABASE
    // =========================================================

    public void open() {

        database = dbHelper.getReadableDatabase();
    }


    // =========================================================
    // CLOSE DATABASE
    // =========================================================

    public void close() {

        dbHelper.close();
    }


    // =========================================================
    // GET ALL RECIPES
    // =========================================================

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes =
                new ArrayList<>();


        Cursor cursor = database.query(

                PantryDBHelper.TABLE_RECIPES,

                new String[]{
                        PantryDBHelper.RECIPE_ID,
                        PantryDBHelper.RECIPE_NAME,
                        PantryDBHelper.RECIPE_INSTRUCTIONS,
                        PantryDBHelper.RECIPE_IMAGE
                },

                null,
                null,
                null,
                null,

                PantryDBHelper.RECIPE_ID + " ASC"
        );


        if (cursor.moveToFirst()) {

            do {

                // Get recipe ID
                long recipeId =
                        cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RECIPE_ID
                                )
                        );


                // Get recipe name
                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RECIPE_NAME
                                )
                        );


                // Get recipe instructions
                String instructions =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RECIPE_INSTRUCTIONS
                                )
                        );


                // Get image name
                String imageName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RECIPE_IMAGE
                                )
                        );


                // Get this recipe's required ingredients
                ArrayList<RecipeIngredient> requiredIngredients =
                        getRecipeIngredients(recipeId);


                // Find matching image inside drawable
                int imageResource =
                        context.getResources()
                                .getIdentifier(
                                        imageName,
                                        "drawable",
                                        context.getPackageName()
                                );


                // Create Recipe object
                Recipe recipe =
                        new Recipe(
                                name,
                                requiredIngredients,
                                instructions,
                                imageResource
                        );


                recipes.add(recipe);


            } while (cursor.moveToNext());
        }


        cursor.close();

        return recipes;
    }


    // =========================================================
    // GET INGREDIENT REQUIREMENTS FOR ONE RECIPE
    // =========================================================

    private ArrayList<RecipeIngredient> getRecipeIngredients(
            long recipeId) {

        ArrayList<RecipeIngredient> ingredients =
                new ArrayList<>();


        Cursor cursor = database.query(

                PantryDBHelper.TABLE_RECIPE_INGREDIENTS,

                new String[]{
                        PantryDBHelper.RI_NAME,
                        PantryDBHelper.RI_QUANTITY,
                        PantryDBHelper.RI_UNIT
                },

                PantryDBHelper.RI_RECIPE_ID + " = ?",

                new String[]{
                        String.valueOf(recipeId)
                },

                null,
                null,
                PantryDBHelper.RI_ID + " ASC"
        );


        if (cursor.moveToFirst()) {

            do {

                String ingredientName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RI_NAME
                                )
                        );


                double requiredQuantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RI_QUANTITY
                                )
                        );


                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        PantryDBHelper.RI_UNIT
                                )
                        );


                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                ingredientName,
                                requiredQuantity,
                                unit
                        );


                ingredients.add(ingredient);


            } while (cursor.moveToNext());
        }


        cursor.close();

        return ingredients;
    }
}