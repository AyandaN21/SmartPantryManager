package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PantryDataSource {

    private SQLiteDatabase database;
    private PantryDBHelper dbHelper;

    public PantryDataSource(Context context) {
        dbHelper = new PantryDBHelper(context);
    }

    // Open database
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Close database
    public void close() {
        dbHelper.close();
    }

    // Add ingredient
    public boolean insertIngredient(Ingredient ingredient) {

        ContentValues values = new ContentValues();

        values.put(PantryDBHelper.COLUMN_NAME,
                ingredient.getName());

        values.put(PantryDBHelper.COLUMN_QUANTITY,
                ingredient.getQuantity());

        values.put(PantryDBHelper.COLUMN_UNIT,
                ingredient.getUnit());

        values.put(PantryDBHelper.COLUMN_EXPIRY,
                ingredient.getExpiryDate());

        long result = database.insert(
                PantryDBHelper.TABLE_PANTRY,
                null,
                values
        );

        return result != -1;
    }

    // Update ingredient
    public boolean updateIngredient(Ingredient ingredient) {

        ContentValues values = new ContentValues();

        values.put(PantryDBHelper.COLUMN_NAME,
                ingredient.getName());

        values.put(PantryDBHelper.COLUMN_QUANTITY,
                ingredient.getQuantity());

        values.put(PantryDBHelper.COLUMN_UNIT,
                ingredient.getUnit());

        values.put(PantryDBHelper.COLUMN_EXPIRY,
                ingredient.getExpiryDate());

        int result = database.update(
                PantryDBHelper.TABLE_PANTRY,
                values,
                PantryDBHelper.COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(ingredient.getId())
                }
        );

        return result > 0;
    }

    // Delete ingredient
    public boolean deleteIngredient(int id) {

        int result = database.delete(
                PantryDBHelper.TABLE_PANTRY,
                PantryDBHelper.COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        return result > 0;
    }

    // Get all ingredients
    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> ingredientList =
                new ArrayList<>();

        Cursor cursor = database.query(
                PantryDBHelper.TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                PantryDBHelper.COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                Ingredient ingredient = new Ingredient();

                int idIndex =
                        cursor.getColumnIndexOrThrow(
                                PantryDBHelper.COLUMN_ID);

                int nameIndex =
                        cursor.getColumnIndexOrThrow(
                                PantryDBHelper.COLUMN_NAME);

                int quantityIndex =
                        cursor.getColumnIndexOrThrow(
                                PantryDBHelper.COLUMN_QUANTITY);

                int unitIndex =
                        cursor.getColumnIndexOrThrow(
                                PantryDBHelper.COLUMN_UNIT);

                int expiryIndex =
                        cursor.getColumnIndexOrThrow(
                                PantryDBHelper.COLUMN_EXPIRY);

                ingredient.setId(
                        cursor.getInt(idIndex));

                ingredient.setName(
                        cursor.getString(nameIndex));

                ingredient.setQuantity(
                        cursor.getDouble(quantityIndex));

                ingredient.setUnit(
                        cursor.getString(unitIndex));

                ingredient.setExpiryDate(
                        cursor.getString(expiryIndex));

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredientList;
    }
}