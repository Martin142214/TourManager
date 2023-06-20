package com.example.TourManager.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.TourManager.Models.EntityModels.Landmark;

@Repository
public interface LandmarkRepository extends MongoRepository<Landmark, String> {
    
}
