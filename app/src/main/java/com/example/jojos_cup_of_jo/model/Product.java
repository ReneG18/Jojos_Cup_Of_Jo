package com.example.jojos_cup_of_jo.model;

import java.math.BigDecimal;

public class Product {

    private final String id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final ProductType type;
    private final ProductCategory category;
    private final int swatchIndex;
    private final boolean seasonal;

    public Product(String id, String name, String description, double price,
                    ProductType type, ProductCategory category, int swatchIndex) {
        this(id, name, description, price, type, category, swatchIndex, false);
    }

    public Product(String id, String name, String description, double price,
                    ProductType type, ProductCategory category, int swatchIndex,
                    boolean seasonal) {
        this.id = id;
        this.name = name;
        this.description = description;
        // valueOf, never new BigDecimal(double): valueOf goes through Double.toString, so the
        // literal 4.50 becomes exactly 4.5 rather than its binary expansion 4.5000000000000000277...
        this.price = BigDecimal.valueOf(price);
        this.type = type;
        this.category = category;
        this.swatchIndex = swatchIndex;
        this.seasonal = seasonal;
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

    /** Unit price as an exact decimal. Money is never a {@code double} past this point. */
    public BigDecimal getPrice() {
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

    /** True for limited-time items, which the Menu and Merch lists surface in their own section. */
    public boolean isSeasonal() {
        return seasonal;
    }
}
