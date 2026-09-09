package com.fitness.coachiq.Progress.Tracking.Repository;


import com.fitness.coachiq.Progress.Tracking.Entity.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends MongoRepository<Assessment, String> {

    Optional<Assessment> findByUserIdAndAssessmentDate(
            String userId,
            LocalDate assessmentDate
    );

    List<Assessment> findByUserIdOrderByAssessmentDateDesc(String userId);

    List<Assessment> findByUserIdAndAssessmentDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );
}