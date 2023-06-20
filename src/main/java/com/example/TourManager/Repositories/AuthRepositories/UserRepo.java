package com.example.TourManager.Repositories.AuthRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.TourManager.Models.EntityModels.User;

public interface UserRepo extends MongoRepository<User, String>{
    
    //@Query("SELECT u FROM User u WHERE u.username = ?1")
    //@Query("{'username':?0}")
    User findByUsername(String username);
    
    User findByEmail(String email);
}
