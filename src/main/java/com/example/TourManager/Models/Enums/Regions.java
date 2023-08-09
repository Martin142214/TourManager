package com.example.TourManager.Models.Enums;

public enum Regions {
    RUINS ("Крепости_и_руини"),
    SANCTUARIES ("Светилища"),
    THRACIAN ("Тракийски"),
    ROMAN ("Римски"),
    MEDIEVAL ("Средновековни"),
    VAZROZHDENSKY ("Възрожденски"),
    TOMBS ("Гробници");

    private String name;

    Regions(String name) {
        this.name = name;
    }

    public String getHistoricalRegionName() {
        return name;
    }
}
