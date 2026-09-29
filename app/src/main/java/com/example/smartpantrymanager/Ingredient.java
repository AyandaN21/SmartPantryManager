package com.example.smartpantrymanager;

public class Ingredient {

    // Ingredient information
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    // Empty constructor
    public Ingredient() {
    }

    // Constructor used when creating a new ingredient
    public Ingredient(String name, double quantity,
                      String unit, String expiryDate) {

        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Get ID
    public int getId() {
        return id;
    }

    // Set ID
    public void setId(int id) {
        this.id = id;
    }

    // Get ingredient name
    public String getName() {
        return name;
    }

    // Set ingredient name
    public void setName(String name) {
        this.name = name;
    }

    // Get quantity
    public double getQuantity() {
        return quantity;
    }

    // Set quantity
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // Get unit
    public String getUnit() {
        return unit;
    }

    // Set unit
    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Get expiry date
    public String getExpiryDate() {
        return expiryDate;
    }

    // Set expiry date
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}