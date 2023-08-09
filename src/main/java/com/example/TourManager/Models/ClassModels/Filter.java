package com.example.TourManager.Models.ClassModels;

public class Filter {
    public String type;

    public String value;

    public String bulgarianValue;

    public Filter() {}

    public Filter(String type, String value, String bulgarianValue) {
        this.type = type;
        this.value = value;
        this.bulgarianValue = bulgarianValue;
    }
}
