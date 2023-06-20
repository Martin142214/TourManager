package com.example.TourManager.Models.EntityModels;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Roles")
public class Role {
    
    @Id
    public String id;

    public String name;

    public Collection<Privilege> privileges;

    public Role() {

    }

    public Role(String name) {
      this.name = name;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
      this.privileges = privileges;
    }
}
