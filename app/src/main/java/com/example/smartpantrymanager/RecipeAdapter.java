package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipeList;


    public RecipeAdapter(Context context,
                         ArrayList<Recipe> recipeList) {

        this.context = context;
        this.recipeList = recipeList;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipeList.get(position);


        // =====================================================
        // DISPLAY RECIPE IMAGE
        // =====================================================

        holder.imgRecipe.setImageResource(
                recipe.getImageResource()
        );


        // =====================================================
        // DISPLAY RECIPE NAME
        // =====================================================

        holder.tvRecipeName.setText(
                recipe.getName()
        );


        // =====================================================
        // DISPLAY INGREDIENTS WITH QUANTITY AND UNIT
        // =====================================================

        String ingredientsText =
                buildIngredientsText(recipe);

        holder.tvRecipeIngredients.setText(
                ingredientsText
        );


        // =====================================================
        // OPEN RECIPE DETAILS
        // =====================================================

        holder.btnViewRecipe.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        context,
                                        RecipeDetailsActivity.class
                                );


                        // Recipe name
                        intent.putExtra(
                                "recipe_name",
                                recipe.getName()
                        );


                        // Recipe ingredients
                        intent.putExtra(
                                "recipe_ingredients",
                                buildIngredientsForDetails(recipe)
                        );


                        // Recipe instructions
                        intent.putExtra(
                                "recipe_instructions",
                                recipe.getInstructions()
                        );


                        // Recipe image
                        intent.putExtra(
                                "recipe_image",
                                recipe.getImageResource()
                        );


                        context.startActivity(intent);
                    }
                });
    }


    // =========================================================
    // INGREDIENT TEXT FOR RECIPE CARD
    // =========================================================

    private String buildIngredientsText(Recipe recipe) {

        StringBuilder builder =
                new StringBuilder("Ingredients: ");


        ArrayList<RecipeIngredient> ingredients =
                recipe.getRequiredIngredients();


        for (int i = 0;
             i < ingredients.size();
             i++) {

            RecipeIngredient ingredient =
                    ingredients.get(i);


            builder.append(
                    ingredient.getName()
            );

            builder.append(" ");

            builder.append(
                    formatQuantity(
                            ingredient.getRequiredQuantity()
                    )
            );

            builder.append(" ");

            builder.append(
                    ingredient.getUnit()
            );


            // Add comma except after last ingredient
            if (i < ingredients.size() - 1) {

                builder.append(", ");
            }
        }


        return builder.toString();
    }


    // =========================================================
    // INGREDIENT TEXT FOR RECIPE DETAILS
    // =========================================================

    private String buildIngredientsForDetails(
            Recipe recipe) {

        StringBuilder builder =
                new StringBuilder();


        for (RecipeIngredient ingredient :
                recipe.getRequiredIngredients()) {

            builder.append("• ");

            builder.append(
                    ingredient.getName()
            );

            builder.append(" — ");

            builder.append(
                    formatQuantity(
                            ingredient.getRequiredQuantity()
                    )
            );

            builder.append(" ");

            builder.append(
                    ingredient.getUnit()
            );

            builder.append("\n");
        }


        return builder.toString().trim();
    }


    // =========================================================
    // FORMAT QUANTITY
    // =========================================================

    private String formatQuantity(double quantity) {

        /*
         * If quantity is a whole number:
         *
         * 2.0 becomes 2
         *
         * If quantity has decimals:
         *
         * 0.5 stays 0.5
         */

        if (quantity == (long) quantity) {

            return String.valueOf(
                    (long) quantity
            );
        }

        return String.valueOf(quantity);
    }


    // =========================================================
    // NUMBER OF RECIPES
    // =========================================================

    @Override
    public int getItemCount() {

        return recipeList.size();
    }


    // =========================================================
    // VIEW HOLDER
    // =========================================================

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgRecipe;
        TextView tvRecipeName;
        TextView tvRecipeIngredients;
        Button btnViewRecipe;


        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);


            imgRecipe =
                    itemView.findViewById(
                            R.id.imgRecipe
                    );


            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );


            tvRecipeIngredients =
                    itemView.findViewById(
                            R.id.tvRecipeIngredients
                    );


            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );
        }
    }
}