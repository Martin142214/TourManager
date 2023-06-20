package com.example.TourManager.Models.EntityModels;

import java.util.Collection;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.TourManager.Models.ClassModels.Destination;

@Document("Users")
public class User {

    @Id
    public String id;

    public String username;

    public String email;

    public String password;

    public Collection<Destination> destinations;

    public Collection<Landmark> favoriteLandmarks;

    public boolean enabled;

    public Collection<Role> roles;

    public User() {
        
    }
    
    public User(String username, String email, String password, Collection<Destination> destinations,
            Collection<Landmark> favoriteLandmarks, boolean enabled, Collection<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.destinations = destinations;
        this.favoriteLandmarks = favoriteLandmarks;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public boolean isEnabled(){
        if (enabled) {
            return true;
        }
        else {
            return false;
        }
    }
}
