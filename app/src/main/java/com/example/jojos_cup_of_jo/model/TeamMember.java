package com.example.jojos_cup_of_jo.model;

public class TeamMember {

    private final String name;
    private final String role;
    private final String quote;
    private final int swatchIndex;

    public TeamMember(String name, String role, String quote, int swatchIndex) {
        this.name = name;
        this.role = role;
        this.quote = quote;
        this.swatchIndex = swatchIndex;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getQuote() {
        return quote;
    }

    public int getSwatchIndex() {
        return swatchIndex;
    }
}
