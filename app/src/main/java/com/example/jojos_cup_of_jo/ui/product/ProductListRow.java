package com.example.jojos_cup_of_jo.ui.product;

import androidx.annotation.StringRes;

import com.example.jojos_cup_of_jo.model.Product;

/**
 * One row in the Menu or Merch list: either a section heading or a product. Built by
 * {@link ProductSections} and rendered by {@link ProductAdapter}.
 */
public final class ProductListRow {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_PRODUCT = 1;

    private final int type;
    private final int headerRes;
    private final Product product;

    private ProductListRow(int type, @StringRes int headerRes, Product product) {
        this.type = type;
        this.headerRes = headerRes;
        this.product = product;
    }

    public static ProductListRow header(@StringRes int headerRes) {
        return new ProductListRow(TYPE_HEADER, headerRes, null);
    }

    public static ProductListRow product(Product product) {
        return new ProductListRow(TYPE_PRODUCT, 0, product);
    }

    public int getType() {
        return type;
    }

    /** Only meaningful when {@link #getType()} is {@link #TYPE_HEADER}. */
    @StringRes
    public int getHeaderRes() {
        return headerRes;
    }

    /** Null for header rows. */
    public Product getProduct() {
        return product;
    }
}
