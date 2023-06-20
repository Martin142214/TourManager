package com.example.TourManager.Comparators;

import java.util.Comparator;

import com.example.TourManager.Models.EntityModels.Landmark;

public class MostFavoriteCompare implements Comparator<Landmark> {

    @Override
    public int compare(Landmark a, Landmark b) {
        return b.rating.compareTo(a.rating);
    }
    
}
