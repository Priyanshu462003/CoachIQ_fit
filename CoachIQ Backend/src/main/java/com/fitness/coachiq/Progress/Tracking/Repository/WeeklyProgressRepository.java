package com.fitness.coachiq.Progress.Tracking.Repository;


import com.fitness.coachiq.Progress.Tracking.Entity.WeeklyProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyProgressRepository extends MongoRepository<WeeklyProgress, String> {

    Optional<WeeklyProgress> findByUserIdAndWeekStartDate(
            String userId,
            LocalDate weekStartDate
    );

    List<WeeklyProgress> findByUserIdOrderByWeekStartDateDesc(String userId);

    List<WeeklyProgress> findByUserIdAndWeekStartDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );
}