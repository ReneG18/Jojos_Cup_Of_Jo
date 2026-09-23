package com.example.jojos_cup_of_jo.model;

import java.util.List;

public class StoreInfo {

    private final String address;
    private final String phone;
    private final List<String> hours;
    private final String aboutUsBlurb;
    private final String storyBlurb;
    private final String thankYouNote;
    private final List<String> pickupLocations;

    public StoreInfo(String address, String phone, List<String> hours, String aboutUsBlurb,
                      String storyBlurb, String thankYouNote, List<String> pickupLocations) {
        this.address = address;
        this.phone = phone;
        this.hours = hours;
        this.aboutUsBlurb = aboutUsBlurb;
        this.storyBlurb = storyBlurb;
        this.thankYouNote = thankYouNote;
        this.pickupLocations = pickupLocations;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getHours() {
        return hours;
    }

    public String getAboutUsBlurb() {
        return aboutUsBlurb;
    }

    public String getStoryBlurb() {
        return storyBlurb;
    }

    public String getThankYouNote() {
        return thankYouNote;
    }

    public List<String> getPickupLocations() {
        return pickupLocations;
    }
}
