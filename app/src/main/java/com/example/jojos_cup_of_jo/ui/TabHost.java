package com.example.jojos_cup_of_jo.ui;

/**
 * Implemented by the hosting Activity so any Fragment can request a bottom-nav tab switch
 * (e.g. Home's "Explore" dropdown, Cart's empty-state CTA) without depending on the concrete
 * Activity type.
 */
public interface TabHost {
    void selectTab(int tabId);
}
