package com.fitness.coachiq.Progress.Tracking.Repository;



import com.fitness.coachiq.Progress.Tracking.Entity.DailyProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyProgressRepository extends MongoRepository<DailyProgress, String> {

    Optional<DailyProgress> findByUserIdAndDate(String userId, LocalDate date);

    List<DailyProgress> findByUserIdOrderByDateDesc(String userId);

    List<DailyProgress> findByUserIdAndDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );
}