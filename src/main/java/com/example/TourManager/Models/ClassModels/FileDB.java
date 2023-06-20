package com.example.TourManager.Models.ClassModels;

public class FileDB {
  
    public String name;

    public String type;

    public String imagePath; 

    public FileDB() {
    }

    public FileDB(String name, String type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }
}
