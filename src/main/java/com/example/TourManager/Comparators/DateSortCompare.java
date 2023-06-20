package com.example.TourManager.Comparators;

import java.util.Comparator;

import com.example.TourManager.Models.DateClass;

public class DateSortCompare implements Comparator<DateClass> {

    @Override
    public int compare(DateClass a, DateClass b) {
        return b.date.compareTo(a.date);  
    }
    
}
