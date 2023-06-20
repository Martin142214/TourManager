package com.example.TourManager.Models.ClassModels;

public class Destination {

    public String destionationId;

    public String destinationName;

    public Boolean userWantToVisit;

    public Boolean userWasHere;

    public Destination() {
        
    }

    public Destination(String destionationId, String destinationName, Boolean userWantToVisit, Boolean userWasHere) {
        this.destionationId = destionationId;
        this.destinationName = destinationName;
        this.userWantToVisit = userWantToVisit;
        this.userWasHere = userWasHere;
    }

    
}
