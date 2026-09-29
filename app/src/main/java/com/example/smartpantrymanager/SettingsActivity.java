package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroupUnits;
    private RadioButton radioMetric;
    private RadioButton radioImperial;
    private Button btnSaveSettings;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        radioGroupUnits =
                findViewById(R.id.radioGroupUnits);

        radioMetric =
                findViewById(R.id.radioMetric);

        radioImperial =
                findViewById(R.id.radioImperial);

        btnSaveSettings =
                findViewById(R.id.btnSaveSettings);

        preferences =
                getSharedPreferences(
                        "SmartPantrySettings",
                        MODE_PRIVATE
                );

        String savedUnit =
                preferences.getString(
                        "measurement_unit",
                        "metric"
                );

        if (savedUnit.equals("imperial")) {

            radioImperial.setChecked(true);

        } else {

            radioMetric.setChecked(true);
        }


        btnSaveSettings.setOnClickListener(view -> {

            String selectedUnit;

            int selectedId =
                    radioGroupUnits
                            .getCheckedRadioButtonId();

            if (selectedId == R.id.radioImperial) {

                selectedUnit = "imperial";

            } else {

                selectedUnit = "metric";
            }

            SharedPreferences.Editor editor =
                    preferences.edit();

            editor.putString(
                    "measurement_unit",
                    selectedUnit
            );

            editor.apply();

            Toast.makeText(
                    SettingsActivity.this,
                    "Settings saved",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}