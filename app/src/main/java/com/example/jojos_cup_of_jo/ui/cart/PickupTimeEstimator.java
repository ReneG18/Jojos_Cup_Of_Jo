package com.example.jojos_cup_of_jo.ui.cart;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Pure helper for a mock "ready by" pickup time estimate. No backend involved. */
public final class PickupTimeEstimator {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a");
    private static final int DEFAULT_PREP_MINUTES = 15;

    private PickupTimeEstimator() {
    }

    public static String estimateReadyTimeLabel() {
        return estimateReadyTimeLabel(DEFAULT_PREP_MINUTES);
    }

    public static String estimateReadyTimeLabel(int prepMinutes) {
        LocalTime readyAt = LocalTime.now().plusMinutes(prepMinutes);
        return "Ready by " + readyAt.format(TIME_FORMAT) + " (~" + prepMinutes + " min)";
    }
}
