package com.fitness.coachiq.DietService.Repository;

import com.fitness.coachiq.DietService.Entity.Diet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietRepository extends MongoRepository<Diet, String> {

    List<Diet> findByUserId(String userId);
}