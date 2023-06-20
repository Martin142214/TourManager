package com.example.TourManager.Repositories.AuthRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.TourManager.Models.EntityModels.Privilege;

public interface PrivilegeRepo extends MongoRepository<Privilege, String> {
    
    Privilege findByName(String name);
}
