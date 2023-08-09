package com.example.TourManager.Models.EntityModels;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.TourManager.Models.ClassModels.FileDB;
import com.example.TourManager.Models.Enums.Regions;
import com.example.TourManager.Models.Enums.Places;
import com.example.TourManager.Models.Enums.NaturalTypes;
import com.example.TourManager.Models.Enums.TypesOfLandmark;

@Document
public class Landmark implements Serializable {

    @Id
    public String id;
    public String name;
    public Places place;
    public TypesOfLandmark type;
    public NaturalTypes naturalType;
    public Regions historicalRegion;
    public String description;
    public Integer rating;
    public String dateOfOffer;
    public Integer views;
    public Integer numberOfPeopleToBeThere;
    public Integer numberOfPeopleWhoWantToVisit;
    public Integer numberOfImages;
    public List<FileDB> images;

    public Landmark(String name, Places place, TypesOfLandmark type, Regions historicalRegion,
                    NaturalTypes naturalType, String description, Integer rating, 
                    String dateOfOffer,Integer views, Integer numberOfPeopleToBeThere, 
                    Integer numberOfPeopleWhoWantToVisit, Integer numberOfImages, List<FileDB> images) {
        this.name = name;
        this.place = place;
        this.type = type;
        this.historicalRegion = historicalRegion;
        this.naturalType = naturalType;
        this.description = description;
        this.rating = rating;
        this.dateOfOffer = dateOfOffer;
        this.views = views;
        this.numberOfPeopleToBeThere = numberOfPeopleToBeThere;
        this.numberOfPeopleWhoWantToVisit = numberOfPeopleWhoWantToVisit;
        this.numberOfImages = numberOfImages;
        this.images = images;
    }
    
    public Landmark() {}
}
