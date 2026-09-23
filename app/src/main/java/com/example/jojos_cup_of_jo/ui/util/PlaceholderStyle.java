package com.example.jojos_cup_of_jo.ui.util;

import android.content.res.ColorStateList;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.jojos_cup_of_jo.R;

/** Shared "colored swatch + initials" placeholder look used for products, team avatars, etc. */
public final class PlaceholderStyle {

    public static final int[] SWATCH_COLORS = {
            R.color.swatch_1,
            R.color.swatch_2,
            R.color.swatch_3,
            R.color.swatch_4,
            R.color.swatch_5,
            R.color.swatch_6
    };

    private PlaceholderStyle() {
    }

    public static int swatchColorRes(int swatchIndex) {
        int index = Math.floorMod(swatchIndex, SWATCH_COLORS.length);
        return SWATCH_COLORS[index];
    }

    /** Tints {@code swatchView}'s background with the swatch color for {@code swatchIndex}. */
    public static void applySwatchTint(View swatchView, int swatchIndex) {
        int color = ContextCompat.getColor(swatchView.getContext(), swatchColorRes(swatchIndex));
        swatchView.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public static String initialsFor(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String[] words = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < words.length && initials.length() < 2; i++) {
            if (!words[i].isEmpty()) {
                initials.append(Character.toUpperCase(words[i].charAt(0)));
            }
        }
        return initials.length() > 0 ? initials.toString() : "?";
    }
}
