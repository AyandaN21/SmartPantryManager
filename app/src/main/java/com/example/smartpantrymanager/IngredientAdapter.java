package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredientList;

    public IngredientAdapter(Context context,
                             ArrayList<Ingredient> ingredientList) {

        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                );

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient =
                ingredientList.get(position);

        holder.tvIngredientName.setText(
                ingredient.getName()
        );

        String quantityText =
                ingredient.getQuantity()
                        + " "
                        + ingredient.getUnit();

        holder.tvIngredientQuantity.setText(
                quantityText
        );

        String expiryDate =
                ingredient.getExpiryDate();

        if (expiryDate == null || expiryDate.isEmpty()) {

            holder.tvIngredientExpiry.setText(
                    "Expiry: Not provided"
            );

        } else {

            holder.tvIngredientExpiry.setText(
                    "Expiry: " + expiryDate
            );
        }


        // EDIT BUTTON
        holder.btnEditIngredient.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        context,
                                        AddEditIngredientActivity.class
                                );

                        intent.putExtra(
                                "ingredient_id",
                                ingredient.getId()
                        );

                        intent.putExtra(
                                "ingredient_name",
                                ingredient.getName()
                        );

                        intent.putExtra(
                                "ingredient_quantity",
                                ingredient.getQuantity()
                        );

                        intent.putExtra(
                                "ingredient_unit",
                                ingredient.getUnit()
                        );

                        intent.putExtra(
                                "ingredient_expiry",
                                ingredient.getExpiryDate()
                        );

                        context.startActivity(intent);
                    }
                });


        // DELETE BUTTON
        holder.btnDeleteIngredient.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        showDeleteDialog(
                                ingredient,
                                holder.getAdapterPosition()
                        );
                    }
                });
    }


    private void showDeleteDialog(
            Ingredient ingredient,
            int position) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        builder.setTitle(
                "Delete Ingredient"
        );

        builder.setMessage(
                "Are you sure you want to delete "
                        + ingredient.getName()
                        + "?"
        );

        builder.setPositiveButton(
                "YES",
                (dialogInterface, i) -> {

                    PantryDataSource dataSource =
                            new PantryDataSource(context);

                    dataSource.open();

                    boolean deleted =
                            dataSource.deleteIngredient(
                                    ingredient.getId()
                            );

                    dataSource.close();

                    if (deleted) {

                        ingredientList.remove(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(
                                position,
                                ingredientList.size()
                        );

                        Toast.makeText(
                                context,
                                "Ingredient deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                context,
                                "Could not delete ingredient",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        builder.setNegativeButton(
                "NO",
                null
        );

        builder.show();
    }


    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;
        TextView tvIngredientExpiry;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public IngredientViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvIngredientName =
                    itemView.findViewById(
                            R.id.tvIngredientName
                    );

            tvIngredientQuantity =
                    itemView.findViewById(
                            R.id.tvIngredientQuantity
                    );

            tvIngredientExpiry =
                    itemView.findViewById(
                            R.id.tvIngredientExpiry
                    );

            btnEditIngredient =
                    itemView.findViewById(
                            R.id.btnEditIngredient
                    );

            btnDeleteIngredient =
                    itemView.findViewById(
                            R.id.btnDeleteIngredient
                    );
        }
    }
}