package com.example.TourManager.Models.EntityModels;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Comments")
public class Comment {
    @Id
    public String id;

    public String landmarkId;

    public String username;

    public String description;

    public Date date;

    public Comment() {

    }

    public Comment(String landmarkId, String username, String description, Date date) {
        this.landmarkId = landmarkId;
        this.username = username;
        this.description = description;
        this.date = date;
    }
}
