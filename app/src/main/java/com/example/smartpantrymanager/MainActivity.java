package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        btnPantry =
                findViewById(R.id.btnPantry);

        btnRecipes =
                findViewById(R.id.btnRecipes);

        btnSettings =
                findViewById(R.id.btnSettings);


        // =====================================================
        // OPEN PANTRY
        // =====================================================

        btnPantry.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        PantryActivity.class
                                );

                        startActivity(intent);
                    }
                });


        // =====================================================
        // OPEN SUGGESTED RECIPES
        // =====================================================

        btnRecipes.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        SuggestedRecipesActivity.class
                                );

                        startActivity(intent);
                    }
                });


        // =====================================================
        // OPEN SETTINGS
        // =====================================================

        btnSettings.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        SettingsActivity.class
                                );

                        startActivity(intent);
                    }
                });
    }


    // =========================================================
    // CREATE TOOLBAR MENU
    // =========================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }


    // =========================================================
    // HANDLE MENU CLICKS
    // =========================================================

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {


        int itemId = item.getItemId();


        // HOME
        if (itemId == R.id.menuHome) {

            return true;
        }


        // MY PANTRY
        if (itemId == R.id.menuPantry) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    );

            startActivity(intent);

            return true;
        }


        // SUGGESTED RECIPES
        if (itemId == R.id.menuRecipes) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);

            return true;
        }


        // SETTINGS
        if (itemId == R.id.menuSettings) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}