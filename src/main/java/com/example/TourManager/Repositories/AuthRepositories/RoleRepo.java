package com.example.TourManager.Repositories.AuthRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.TourManager.Models.EntityModels.Role;

public interface RoleRepo extends MongoRepository<Role, String>{
    
    Role findByName(String name);
}
