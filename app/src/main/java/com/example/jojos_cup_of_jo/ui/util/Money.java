package com.example.jojos_cup_of_jo.ui.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * The one place money becomes text. Every price, line total and receipt row goes through here so
 * they cannot drift apart in how they round or where the dollar sign sits.
 */
public final class Money {

    private Money() {
    }

    /** Formats an exact amount as {@code $4.50}. */
    public static String format(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        return String.format(Locale.US, "$%s", amount.setScale(2, RoundingMode.HALF_UP));
    }
}
