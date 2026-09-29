package com.example.smartpantrymanager;

import java.util.ArrayList;

public class Recipe {

    private String name;

    // Each ingredient now contains:
    // name + required quantity + unit
    private ArrayList<RecipeIngredient> requiredIngredients;

    private String instructions;
    private int imageResource;


    public Recipe(String name,
                  ArrayList<RecipeIngredient> requiredIngredients,
                  String instructions,
                  int imageResource) {

        this.name = name;
        this.requiredIngredients = requiredIngredients;
        this.instructions = instructions;
        this.imageResource = imageResource;
    }


    public String getName() {
        return name;
    }


    public ArrayList<RecipeIngredient> getRequiredIngredients() {
        return requiredIngredients;
    }


    public String getInstructions() {
        return instructions;
    }


    public int getImageResource() {
        return imageResource;
    }
}