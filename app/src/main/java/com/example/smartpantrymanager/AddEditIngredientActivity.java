package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtIngredientName;
    private EditText edtQuantity;
    private EditText edtUnit;

    private TextView tvSelectedExpiryDate;
    private TextView tvIngredientFormTitle;

    private Button btnChooseDate;
    private Button btnSaveIngredient;
    private Button btnCancel;

    private String selectedExpiryDate = "";

    private int ingredientId = -1;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        // Connect Java to XML
        tvIngredientFormTitle =
                findViewById(R.id.tvIngredientFormTitle);

        edtIngredientName =
                findViewById(R.id.edtIngredientName);

        edtQuantity =
                findViewById(R.id.edtQuantity);

        edtUnit =
                findViewById(R.id.edtUnit);

        tvSelectedExpiryDate =
                findViewById(R.id.tvSelectedExpiryDate);

        btnChooseDate =
                findViewById(R.id.btnChooseDate);

        btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        btnCancel =
                findViewById(R.id.btnCancel);


        // Check if editing an existing ingredient
        if (getIntent().hasExtra("ingredient_id")) {

            editMode = true;

            // Change title
            tvIngredientFormTitle.setText("Edit Ingredient");

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String name =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double quantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String unit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String expiry =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );


            // Show existing details
            edtIngredientName.setText(name);

            edtQuantity.setText(
                    String.valueOf(quantity)
            );

            edtUnit.setText(unit);


            if (expiry != null &&
                    !expiry.isEmpty()) {

                selectedExpiryDate = expiry;

                tvSelectedExpiryDate.setText(
                        expiry
                );

            } else {

                selectedExpiryDate = "";

                tvSelectedExpiryDate.setText(
                        "No expiry date selected"
                );
            }


            // Change save button
            btnSaveIngredient.setText(
                    "UPDATE INGREDIENT"
            );

        } else {

            // Adding a new ingredient
            tvIngredientFormTitle.setText(
                    "Add Ingredient"
            );
        }


        // Choose expiry date
        btnChooseDate.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        showDatePicker();
                    }
                });


        // Save or update ingredient
        btnSaveIngredient.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        saveIngredient();
                    }
                });


        // Cancel
        btnCancel.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }


    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dialog =
                new DatePickerDialog(

                        AddEditIngredientActivity.this,

                        (datePicker,
                         selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            selectedExpiryDate =
                                    selectedDay
                                            + "/"
                                            + (selectedMonth + 1)
                                            + "/"
                                            + selectedYear;

                            tvSelectedExpiryDate.setText(
                                    selectedExpiryDate
                            );
                        },

                        year,
                        month,
                        day
                );

        dialog.show();
    }


    private void saveIngredient() {

        String ingredientName =
                edtIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                edtQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                edtUnit
                        .getText()
                        .toString()
                        .trim();


        if (ingredientName.isEmpty()) {

            edtIngredientName.setError(
                    "Please enter ingredient name"
            );

            return;
        }


        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Please enter quantity"
            );

            return;
        }


        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            edtQuantity.setError(
                    "Please enter a valid quantity"
            );

            return;
        }


        if (quantity <= 0) {

            edtQuantity.setError(
                    "Quantity must be greater than 0"
            );

            return;
        }


        if (unit.isEmpty()) {

            edtUnit.setError(
                    "Please enter unit"
            );

            return;
        }


        Ingredient ingredient =
                new Ingredient(
                        ingredientName,
                        quantity,
                        unit,
                        selectedExpiryDate
                );


        PantryDataSource dataSource =
                new PantryDataSource(
                        AddEditIngredientActivity.this
                );

        dataSource.open();

        boolean successful;


        if (editMode) {

            ingredient.setId(
                    ingredientId
            );

            successful =
                    dataSource.updateIngredient(
                            ingredient
                    );

        } else {

            successful =
                    dataSource.insertIngredient(
                            ingredient
                    );
        }


        dataSource.close();


        if (successful) {

            if (editMode) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully",
                        Toast.LENGTH_SHORT
                ).show();
            }

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not save ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}