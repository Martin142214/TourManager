package com.example.TourManager.Models.Enums;

public enum NaturalTypes {
    SOUTH_EAST ("Югоизточна_България"),
    SOUTH_WEST ("Югозападна_България"),
    OLD_MOUNTAIN ("Стара_планина"),
    SOFIA_REGION ("Софийско"),
    NORTH_EAST ("Североизточна_България"),
    NORTH_WEST ("Северозападна_България"),
    RILA_MOUNTAIN ("Рила_планина"),
    RODOPI_MOUNTAIN ("Родопи");

    private String name;

    NaturalTypes(String name) {
        this.name = name;
    }

    public String getNaturalTypeName() {
        return name;
    }
}
