package com.example.jojos_cup_of_jo.ui.product;

import androidx.annotation.StringRes;

import com.example.jojos_cup_of_jo.R;
import com.example.jojos_cup_of_jo.model.Product;
import com.example.jojos_cup_of_jo.model.ProductCategory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns a flat product list into headed sections: seasonal items first, then one section per
 * category. Categories appear in the order they first occur in the source list, so the ordering
 * chosen in the data source is preserved rather than being reshuffled into enum order.
 */
public final class ProductSections {

    private ProductSections() {
    }

    public static List<ProductListRow> build(List<Product> products) {
        List<Product> seasonal = new ArrayList<>();
        Map<ProductCategory, List<Product>> byCategory = new LinkedHashMap<>();

        for (Product product : products) {
            if (product.isSeasonal()) {
                seasonal.add(product);
            } else {
                List<Product> group = byCategory.get(product.getCategory());
                if (group == null) {
                    group = new ArrayList<>();
                    byCategory.put(product.getCategory(), group);
                }
                group.add(product);
            }
        }

        List<ProductListRow> rows = new ArrayList<>();
        if (!seasonal.isEmpty()) {
            addSection(rows, R.string.section_seasonal, seasonal);
        }
        for (Map.Entry<ProductCategory, List<Product>> entry : byCategory.entrySet()) {
            addSection(rows, labelFor(entry.getKey()), entry.getValue());
        }
        return rows;
    }

    private static void addSection(List<ProductListRow> rows, @StringRes int headerRes,
                                    List<Product> products) {
        rows.add(ProductListRow.header(headerRes));
        for (Product product : products) {
            rows.add(ProductListRow.product(product));
        }
    }

    @StringRes
    private static int labelFor(ProductCategory category) {
        switch (category) {
            case COFFEE:
                return R.string.category_coffee;
            case TEA:
                return R.string.category_tea;
            case PASTRY:
                return R.string.category_pastry;
            case APPAREL:
                return R.string.category_apparel;
            case DRINKWARE:
                return R.string.category_drinkware;
            case ACCESSORY:
            default:
                return R.string.category_accessory;
        }
    }
}
