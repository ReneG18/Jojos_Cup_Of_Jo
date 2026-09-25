package com.example.jojos_cup_of_jo.ui.util;

import com.example.jojos_cup_of_jo.R;

/**
 * Maps a product id to its photograph. Photos live in {@code res/drawable-nodpi} as 600x600 JPEGs;
 * see {@code PHOTO_CREDITS.md} for their sources. Products without a photo fall back to a
 * placeholder icon.
 */
public final class ProductArt {

    private ProductArt() {
    }

    public static int photoRes(String productId) {
        if (productId == null) {
            return R.drawable.ic_product_placeholder;
        }
        switch (productId) {
            case "menu_pumpkin_spice_latte":
                return R.drawable.photo_pumpkin_spice_latte;
            case "menu_vanilla_latte":
                return R.drawable.photo_vanilla_latte;
            case "menu_cappuccino":
                return R.drawable.photo_cappuccino;
            case "menu_drip_coffee":
                return R.drawable.photo_drip_coffee;
            case "menu_cold_brew":
                return R.drawable.photo_cold_brew;
            case "menu_chai_latte":
                return R.drawable.photo_chai_latte;
            case "menu_green_tea":
                return R.drawable.photo_green_tea;
            case "menu_croissant":
                return R.drawable.photo_croissant;
            case "menu_muffin":
                return R.drawable.photo_muffin;
            case "menu_avocado_toast":
                return R.drawable.photo_avocado_toast;
            case "merch_autumn_scarf":
                return R.drawable.photo_autumn_scarf;
            case "merch_logo_mug":
                return R.drawable.photo_logo_mug;
            case "merch_travel_tumbler":
                return R.drawable.photo_travel_tumbler;
            case "merch_hoodie":
                return R.drawable.photo_hoodie;
            case "merch_cropped_tee":
                return R.drawable.photo_cropped_tee;
            case "merch_tote_bag":
                return R.drawable.photo_tote_bag;
            case "merch_enamel_pin":
                return R.drawable.photo_enamel_pin;
            default:
                return R.drawable.ic_product_placeholder;
        }
    }
}
