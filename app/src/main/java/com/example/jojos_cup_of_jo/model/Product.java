package com.example.jojos_cup_of_jo.model;

public class Product {

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final ProductType type;
    private final ProductCategory category;
    private final int swatchIndex;

    public Product(String id, String name, String description, double price,
                    ProductType type, ProductCategory category, int swatchIndex) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.category = category;
        this.swatchIndex = swatchIndex;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public ProductType getType() {
        return type;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public int getSwatchIndex() {
        return swatchIndex;
    }
}
