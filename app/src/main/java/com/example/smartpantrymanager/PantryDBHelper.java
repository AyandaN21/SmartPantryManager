package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 3;


    // =========================================================
    // PANTRY TABLE
    // =========================================================

    public static final String TABLE_PANTRY = "pantry";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY = "expiry_date";

    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY + " TEXT);";


    // =========================================================
    // RECIPES TABLE
    // =========================================================

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INGREDIENTS = "ingredients";
    public static final String RECIPE_INSTRUCTIONS = "instructions";
    public static final String RECIPE_IMAGE = "image_name";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPE_NAME + " TEXT NOT NULL, " +
                    RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                    RECIPE_INSTRUCTIONS + " TEXT NOT NULL, " +
                    RECIPE_IMAGE + " TEXT NOT NULL);";


    // =========================================================
    // RECIPE INGREDIENTS TABLE
    // =========================================================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String RI_ID = "_id";
    public static final String RI_RECIPE_ID = "recipe_id";
    public static final String RI_NAME = "ingredient_name";
    public static final String RI_QUANTITY = "required_quantity";
    public static final String RI_UNIT = "unit";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                    RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RI_RECIPE_ID + " INTEGER NOT NULL, " +
                    RI_NAME + " TEXT NOT NULL, " +
                    RI_QUANTITY + " REAL NOT NULL, " +
                    RI_UNIT + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + RI_RECIPE_ID + ") REFERENCES " +
                    TABLE_RECIPES + "(" + RECIPE_ID + "));";


    public PantryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // =========================================================
    // CREATE DATABASE
    // =========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);

        seedRecipes(db);
    }


    // =========================================================
    // UPGRADE DATABASE
    // =========================================================

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        // Version 1 -> Version 2
        // Add recipes without deleting pantry data.
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_RECIPES);
            seedRecipesWithoutRequirements(db);
        }

        // Version 2 -> Version 3
        // Add quantity/unit requirements.
        if (oldVersion < 3) {

            db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);

            seedRecipeRequirements(db);
        }
    }


    // =========================================================
    // ADD RECIPE
    // =========================================================

    private long addRecipe(SQLiteDatabase db,
                           String name,
                           String ingredients,
                           String instructions,
                           String imageName) {

        ContentValues values = new ContentValues();

        values.put(RECIPE_NAME, name);
        values.put(RECIPE_INGREDIENTS, ingredients);
        values.put(RECIPE_INSTRUCTIONS, instructions);
        values.put(RECIPE_IMAGE, imageName);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }


    // =========================================================
    // ADD RECIPE REQUIREMENT
    // =========================================================

    private void addRecipeIngredient(SQLiteDatabase db,
                                     long recipeId,
                                     String name,
                                     double quantity,
                                     String unit) {

        ContentValues values = new ContentValues();

        values.put(RI_RECIPE_ID, recipeId);
        values.put(RI_NAME, name);
        values.put(RI_QUANTITY, quantity);
        values.put(RI_UNIT, unit);

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }


    // =========================================================
    // SEED COMPLETE DATABASE
    // =========================================================

    private void seedRecipes(SQLiteDatabase db) {

        long recipeId;


        // =====================================================
        // BREAKFAST
        // =====================================================

        recipeId = addRecipe(
                db,
                "Banana Pancakes",
                "Banana|Eggs|Flour",
                "1. Mash the banana.\n\n" +
                        "2. Add eggs and flour.\n\n" +
                        "3. Mix until smooth.\n\n" +
                        "4. Pour small amounts into a heated pan.\n\n" +
                        "5. Cook both sides until golden.",
                "banana_pancakes"
        );

        addRecipeIngredient(db, recipeId, "Banana", 1, "piece");
        addRecipeIngredient(db, recipeId, "Egg", 1, "piece");
        addRecipeIngredient(db, recipeId, "Flour", 1, "cup");


        recipeId = addRecipe(
                db,
                "Scrambled Eggs",
                "Eggs|Milk",
                "1. Crack the eggs into a bowl.\n\n" +
                        "2. Add milk and mix well.\n\n" +
                        "3. Pour into a heated pan.\n\n" +
                        "4. Stir until cooked.\n\n" +
                        "5. Serve while warm.",
                "scrambled_eggs"
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "Milk", 100, "ml");


        recipeId = addRecipe(
                db,
                "French Toast",
                "Bread|Eggs|Milk",
                "1. Beat the eggs and milk together.\n\n" +
                        "2. Dip the bread into the mixture.\n\n" +
                        "3. Place the bread in a heated pan.\n\n" +
                        "4. Cook both sides until golden.\n\n" +
                        "5. Serve warm.",
                "french_toast"
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "slices");
        addRecipeIngredient(db, recipeId, "Egg", 1, "piece");
        addRecipeIngredient(db, recipeId, "Milk", 100, "ml");


        recipeId = addRecipe(
                db,
                "Cheese Omelette",
                "Eggs|Cheese",
                "1. Beat the eggs in a bowl.\n\n" +
                        "2. Pour the eggs into a heated pan.\n\n" +
                        "3. Add cheese.\n\n" +
                        "4. Fold the omelette.\n\n" +
                        "5. Cook until the cheese melts.",
                "cheese_omelette"
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "Cheese", 50, "g");


        recipeId = addRecipe(
                db,
                "Overnight Oats",
                "Oats|Milk",
                "1. Add oats to a container.\n\n" +
                        "2. Pour in the milk.\n\n" +
                        "3. Mix well.\n\n" +
                        "4. Cover the container.\n\n" +
                        "5. Leave it in the fridge overnight.",
                "overnight_oats"
        );

        addRecipeIngredient(db, recipeId, "Oats", 1, "cup");
        addRecipeIngredient(db, recipeId, "Milk", 1, "cup");


        // =====================================================
        // LUNCH
        // =====================================================

        recipeId = addRecipe(
                db,
                "Avocado & Egg on Toast",
                "Avocado|Eggs|Bread",
                "1. Toast the bread.\n\n" +
                        "2. Mash the avocado.\n\n" +
                        "3. Cook the egg.\n\n" +
                        "4. Spread avocado onto the toast.\n\n" +
                        "5. Place the egg on top and serve.",
                "avocado_egg_toast"
        );

        addRecipeIngredient(db, recipeId, "Avocado", 1, "piece");
        addRecipeIngredient(db, recipeId, "Egg", 1, "piece");
        addRecipeIngredient(db, recipeId, "Bread", 1, "slice");


        recipeId = addRecipe(
                db,
                "Chicken Wrap",
                "Chicken|Wrap",
                "1. Cook the chicken until fully cooked.\n\n" +
                        "2. Cut the chicken into pieces.\n\n" +
                        "3. Place the chicken onto the wrap.\n\n" +
                        "4. Fold the wrap.\n\n" +
                        "5. Serve.",
                "chicken_wrap"
        );

        addRecipeIngredient(db, recipeId, "Chicken", 150, "g");
        addRecipeIngredient(db, recipeId, "Wrap", 1, "piece");


        recipeId = addRecipe(
                db,
                "Tuna Mayo Sandwich",
                "Tuna|Mayonnaise|Bread",
                "1. Place tuna in a bowl.\n\n" +
                        "2. Add mayonnaise.\n\n" +
                        "3. Mix well.\n\n" +
                        "4. Spread the mixture onto bread.\n\n" +
                        "5. Close the sandwich and serve.",
                "tuna_mayo_sandwich"
        );

        addRecipeIngredient(db, recipeId, "Tuna", 100, "g");
        addRecipeIngredient(db, recipeId, "Mayonnaise", 2, "tbsp");
        addRecipeIngredient(db, recipeId, "Bread", 2, "slices");


        recipeId = addRecipe(
                db,
                "Chicken Salad",
                "Chicken|Lettuce|Tomato",
                "1. Cook the chicken.\n\n" +
                        "2. Cut the chicken into pieces.\n\n" +
                        "3. Chop the lettuce and tomato.\n\n" +
                        "4. Place everything into a bowl.\n\n" +
                        "5. Mix and serve.",
                "chicken_salad"
        );

        addRecipeIngredient(db, recipeId, "Chicken", 150, "g");
        addRecipeIngredient(db, recipeId, "Lettuce", 1, "cup");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "piece");


        recipeId = addRecipe(
                db,
                "Egg Fried Rice",
                "Eggs|Rice",
                "1. Cook the rice.\n\n" +
                        "2. Crack the eggs into a heated pan.\n\n" +
                        "3. Stir the eggs until cooked.\n\n" +
                        "4. Add the cooked rice.\n\n" +
                        "5. Mix together and serve.",
                "egg_fried_rice"
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "Rice", 1, "cup");


        // =====================================================
        // DINNER
        // =====================================================

        recipeId = addRecipe(
                db,
                "Tomato & Cheese Pasta",
                "Pasta|Tomato|Cheese",
                "1. Cook the pasta.\n\n" +
                        "2. Chop and cook the tomato.\n\n" +
                        "3. Add the cooked pasta.\n\n" +
                        "4. Add cheese.\n\n" +
                        "5. Mix until the cheese melts.",
                "tomato_cheese_pasta"
        );

        addRecipeIngredient(db, recipeId, "Pasta", 1, "cup");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "piece");
        addRecipeIngredient(db, recipeId, "Cheese", 50, "g");


        recipeId = addRecipe(
                db,
                "Chicken & Rice",
                "Chicken|Rice",
                "1. Cook the rice.\n\n" +
                        "2. Season the chicken.\n\n" +
                        "3. Cook the chicken until fully cooked.\n\n" +
                        "4. Place the chicken with the rice.\n\n" +
                        "5. Serve warm.",
                "chicken_rice"
        );

        addRecipeIngredient(db, recipeId, "Chicken", 150, "g");
        addRecipeIngredient(db, recipeId, "Rice", 1, "cup");


        recipeId = addRecipe(
                db,
                "Spaghetti & Mince",
                "Spaghetti|Mince|Tomato",
                "1. Cook the spaghetti.\n\n" +
                        "2. Cook the mince in a pan.\n\n" +
                        "3. Add chopped tomato to the mince.\n\n" +
                        "4. Cook until the sauce is ready.\n\n" +
                        "5. Serve with the spaghetti.",
                "spaghetti_mince"
        );

        addRecipeIngredient(db, recipeId, "Spaghetti", 200, "g");
        addRecipeIngredient(db, recipeId, "Mince", 150, "g");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "piece");


        recipeId = addRecipe(
                db,
                "Chicken Pasta",
                "Chicken|Pasta",
                "1. Cook the pasta.\n\n" +
                        "2. Cut the chicken into pieces.\n\n" +
                        "3. Cook the chicken until fully cooked.\n\n" +
                        "4. Add the pasta to the chicken.\n\n" +
                        "5. Mix and serve.",
                "chicken_pasta"
        );

        addRecipeIngredient(db, recipeId, "Chicken", 150, "g");
        addRecipeIngredient(db, recipeId, "Pasta", 1, "cup");


        recipeId = addRecipe(
                db,
                "Pap & Mince",
                "Pap|Mince",
                "1. Prepare the pap.\n\n" +
                        "2. Cook the mince in a pan.\n\n" +
                        "3. Season the mince.\n\n" +
                        "4. Cook until the mince is ready.\n\n" +
                        "5. Serve the mince with the pap.",
                "pap_mince"
        );

        addRecipeIngredient(db, recipeId, "Pap", 1, "cup");
        addRecipeIngredient(db, recipeId, "Mince", 150, "g");


        // =====================================================
        // SNACKS
        // =====================================================

        recipeId = addRecipe(
                db,
                "Cheese Toast",
                "Bread|Cheese",
                "1. Place cheese onto the bread.\n\n" +
                        "2. Toast until the cheese melts.\n\n" +
                        "3. Serve warm.",
                "cheese_toast"
        );

        addRecipeIngredient(db, recipeId, "Bread", 1, "slice");
        addRecipeIngredient(db, recipeId, "Cheese", 30, "g");


        recipeId = addRecipe(
                db,
                "Berry Smoothie",
                "Berries|Milk",
                "1. Place the berries into a blender.\n\n" +
                        "2. Add milk.\n\n" +
                        "3. Blend until smooth.\n\n" +
                        "4. Pour into a glass.\n\n" +
                        "5. Serve cold.",
                "berry_smoothie"
        );

        addRecipeIngredient(db, recipeId, "Berry", 1, "cup");
        addRecipeIngredient(db, recipeId, "Milk", 250, "ml");


        recipeId = addRecipe(
                db,
                "Cinnamon Toast",
                "Bread|Cinnamon",
                "1. Toast the bread.\n\n" +
                        "2. Sprinkle cinnamon over the toast.\n\n" +
                        "3. Serve while warm.",
                "cinnamon_toast"
        );

        addRecipeIngredient(db, recipeId, "Bread", 1, "slice");
        addRecipeIngredient(db, recipeId, "Cinnamon", 1, "tsp");


        recipeId = addRecipe(
                db,
                "Potato Wedges",
                "Potato",
                "1. Wash the potatoes.\n\n" +
                        "2. Cut them into wedges.\n\n" +
                        "3. Season the wedges.\n\n" +
                        "4. Bake until golden and crispy.\n\n" +
                        "5. Serve warm.",
                "potato_wedges"
        );

        addRecipeIngredient(db, recipeId, "Potato", 2, "pieces");


        recipeId = addRecipe(
                db,
                "Tomato & Cheese Toast",
                "Bread|Tomato|Cheese",
                "1. Place tomato slices onto the bread.\n\n" +
                        "2. Add cheese on top.\n\n" +
                        "3. Toast until the cheese melts.\n\n" +
                        "4. Remove carefully.\n\n" +
                        "5. Serve warm.",
                "tomato_cheese_toast"
        );

        addRecipeIngredient(db, recipeId, "Bread", 1, "slice");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "piece");
        addRecipeIngredient(db, recipeId, "Cheese", 30, "g");
    }


    // =========================================================
    // VERSION 1 -> VERSION 2 SUPPORT
    // =========================================================

    private void seedRecipesWithoutRequirements(SQLiteDatabase db) {

        addRecipe(db, "Banana Pancakes",
                "Banana|Eggs|Flour",
                "1. Mash the banana.\n\n2. Add eggs and flour.\n\n3. Mix until smooth.\n\n4. Pour small amounts into a heated pan.\n\n5. Cook both sides until golden.",
                "banana_pancakes");

        addRecipe(db, "Scrambled Eggs",
                "Eggs|Milk",
                "1. Crack the eggs into a bowl.\n\n2. Add milk and mix well.\n\n3. Pour into a heated pan.\n\n4. Stir until cooked.\n\n5. Serve while warm.",
                "scrambled_eggs");

        addRecipe(db, "French Toast",
                "Bread|Eggs|Milk",
                "1. Beat the eggs and milk together.\n\n2. Dip the bread into the mixture.\n\n3. Place the bread in a heated pan.\n\n4. Cook both sides until golden.\n\n5. Serve warm.",
                "french_toast");

        addRecipe(db, "Cheese Omelette",
                "Eggs|Cheese",
                "1. Beat the eggs in a bowl.\n\n2. Pour the eggs into a heated pan.\n\n3. Add cheese.\n\n4. Fold the omelette.\n\n5. Cook until the cheese melts.",
                "cheese_omelette");

        addRecipe(db, "Overnight Oats",
                "Oats|Milk",
                "1. Add oats to a container.\n\n2. Pour in the milk.\n\n3. Mix well.\n\n4. Cover the container.\n\n5. Leave it in the fridge overnight.",
                "overnight_oats");

        addRecipe(db, "Avocado & Egg on Toast",
                "Avocado|Eggs|Bread",
                "1. Toast the bread.\n\n2. Mash the avocado.\n\n3. Cook the egg.\n\n4. Spread avocado onto the toast.\n\n5. Place the egg on top and serve.",
                "avocado_egg_toast");

        addRecipe(db, "Chicken Wrap",
                "Chicken|Wrap",
                "1. Cook the chicken until fully cooked.\n\n2. Cut the chicken into pieces.\n\n3. Place the chicken onto the wrap.\n\n4. Fold the wrap.\n\n5. Serve.",
                "chicken_wrap");

        addRecipe(db, "Tuna Mayo Sandwich",
                "Tuna|Mayonnaise|Bread",
                "1. Place tuna in a bowl.\n\n2. Add mayonnaise.\n\n3. Mix well.\n\n4. Spread the mixture onto bread.\n\n5. Close the sandwich and serve.",
                "tuna_mayo_sandwich");

        addRecipe(db, "Chicken Salad",
                "Chicken|Lettuce|Tomato",
                "1. Cook the chicken.\n\n2. Cut the chicken into pieces.\n\n3. Chop the lettuce and tomato.\n\n4. Place everything into a bowl.\n\n5. Mix and serve.",
                "chicken_salad");

        addRecipe(db, "Egg Fried Rice",
                "Eggs|Rice",
                "1. Cook the rice.\n\n2. Crack the eggs into a heated pan.\n\n3. Stir the eggs until cooked.\n\n4. Add the cooked rice.\n\n5. Mix together and serve.",
                "egg_fried_rice");

        addRecipe(db, "Tomato & Cheese Pasta",
                "Pasta|Tomato|Cheese",
                "1. Cook the pasta.\n\n2. Chop and cook the tomato.\n\n3. Add the cooked pasta.\n\n4. Add cheese.\n\n5. Mix until the cheese melts.",
                "tomato_cheese_pasta");

        addRecipe(db, "Chicken & Rice",
                "Chicken|Rice",
                "1. Cook the rice.\n\n2. Season the chicken.\n\n3. Cook the chicken until fully cooked.\n\n4. Place the chicken with the rice.\n\n5. Serve warm.",
                "chicken_rice");

        addRecipe(db, "Spaghetti & Mince",
                "Spaghetti|Mince|Tomato",
                "1. Cook the spaghetti.\n\n2. Cook the mince in a pan.\n\n3. Add chopped tomato to the mince.\n\n4. Cook until the sauce is ready.\n\n5. Serve with the spaghetti.",
                "spaghetti_mince");

        addRecipe(db, "Chicken Pasta",
                "Chicken|Pasta",
                "1. Cook the pasta.\n\n2. Cut the chicken into pieces.\n\n3. Cook the chicken until fully cooked.\n\n4. Add the pasta to the chicken.\n\n5. Mix and serve.",
                "chicken_pasta");

        addRecipe(db, "Pap & Mince",
                "Pap|Mince",
                "1. Prepare the pap.\n\n2. Cook the mince in a pan.\n\n3. Season the mince.\n\n4. Cook until the mince is ready.\n\n5. Serve the mince with the pap.",
                "pap_mince");

        addRecipe(db, "Cheese Toast",
                "Bread|Cheese",
                "1. Place cheese onto the bread.\n\n2. Toast until the cheese melts.\n\n3. Serve warm.",
                "cheese_toast");

        addRecipe(db, "Berry Smoothie",
                "Berries|Milk",
                "1. Place the berries into a blender.\n\n2. Add milk.\n\n3. Blend until smooth.\n\n4. Pour into a glass.\n\n5. Serve cold.",
                "berry_smoothie");

        addRecipe(db, "Cinnamon Toast",
                "Bread|Cinnamon",
                "1. Toast the bread.\n\n2. Sprinkle cinnamon over the toast.\n\n3. Serve while warm.",
                "cinnamon_toast");

        addRecipe(db, "Potato Wedges",
                "Potato",
                "1. Wash the potatoes.\n\n2. Cut them into wedges.\n\n3. Season the wedges.\n\n4. Bake until golden and crispy.\n\n5. Serve warm.",
                "potato_wedges");

        addRecipe(db, "Tomato & Cheese Toast",
                "Bread|Tomato|Cheese",
                "1. Place tomato slices onto the bread.\n\n2. Add cheese on top.\n\n3. Toast until the cheese melts.\n\n4. Remove carefully.\n\n5. Serve warm.",
                "tomato_cheese_toast");
    }


    // =========================================================
    // VERSION 2 -> VERSION 3 SUPPORT
    // =========================================================

    private void seedRecipeRequirements(SQLiteDatabase db) {

        // The existing version 2 recipes have IDs 1 - 20.

        addRecipeIngredient(db, 1, "Banana", 1, "piece");
        addRecipeIngredient(db, 1, "Egg", 1, "piece");
        addRecipeIngredient(db, 1, "Flour", 1, "cup");

        addRecipeIngredient(db, 2, "Egg", 2, "pieces");
        addRecipeIngredient(db, 2, "Milk", 100, "ml");

        addRecipeIngredient(db, 3, "Bread", 2, "slices");
        addRecipeIngredient(db, 3, "Egg", 1, "piece");
        addRecipeIngredient(db, 3, "Milk", 100, "ml");

        addRecipeIngredient(db, 4, "Egg", 2, "pieces");
        addRecipeIngredient(db, 4, "Cheese", 50, "g");

        addRecipeIngredient(db, 5, "Oats", 1, "cup");
        addRecipeIngredient(db, 5, "Milk", 1, "cup");

        addRecipeIngredient(db, 6, "Avocado", 1, "piece");
        addRecipeIngredient(db, 6, "Egg", 1, "piece");
        addRecipeIngredient(db, 6, "Bread", 1, "slice");

        addRecipeIngredient(db, 7, "Chicken", 150, "g");
        addRecipeIngredient(db, 7, "Wrap", 1, "piece");

        addRecipeIngredient(db, 8, "Tuna", 100, "g");
        addRecipeIngredient(db, 8, "Mayonnaise", 2, "tbsp");
        addRecipeIngredient(db, 8, "Bread", 2, "slices");

        addRecipeIngredient(db, 9, "Chicken", 150, "g");
        addRecipeIngredient(db, 9, "Lettuce", 1, "cup");
        addRecipeIngredient(db, 9, "Tomato", 1, "piece");

        addRecipeIngredient(db, 10, "Egg", 2, "pieces");
        addRecipeIngredient(db, 10, "Rice", 1, "cup");

        addRecipeIngredient(db, 11, "Pasta", 1, "cup");
        addRecipeIngredient(db, 11, "Tomato", 1, "piece");
        addRecipeIngredient(db, 11, "Cheese", 50, "g");

        addRecipeIngredient(db, 12, "Chicken", 150, "g");
        addRecipeIngredient(db, 12, "Rice", 1, "cup");

        addRecipeIngredient(db, 13, "Spaghetti", 200, "g");
        addRecipeIngredient(db, 13, "Mince", 150, "g");
        addRecipeIngredient(db, 13, "Tomato", 1, "piece");

        addRecipeIngredient(db, 14, "Chicken", 150, "g");
        addRecipeIngredient(db, 14, "Pasta", 1, "cup");

        addRecipeIngredient(db, 15, "Pap", 1, "cup");
        addRecipeIngredient(db, 15, "Mince", 150, "g");

        addRecipeIngredient(db, 16, "Bread", 1, "slice");
        addRecipeIngredient(db, 16, "Cheese", 30, "g");

        addRecipeIngredient(db, 17, "Berry", 1, "cup");
        addRecipeIngredient(db, 17, "Milk", 250, "ml");

        addRecipeIngredient(db, 18, "Bread", 1, "slice");
        addRecipeIngredient(db, 18, "Cinnamon", 1, "tsp");

        addRecipeIngredient(db, 19, "Potato", 2, "pieces");

        addRecipeIngredient(db, 20, "Bread", 1, "slice");
        addRecipeIngredient(db, 20, "Tomato", 1, "piece");
        addRecipeIngredient(db, 20, "Cheese", 30, "g");
    }
}