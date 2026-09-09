package com.fitness.coachiq.Progress.Tracking.Repository;

import com.fitness.coachiq.Progress.Tracking.Entity.ProgressAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgressAnalysisRepository extends MongoRepository<ProgressAnalysis, String> {
    Optional<ProgressAnalysis> findFirstByUserIdOrderByReportMonthDesc(String userId);

    Optional<ProgressAnalysis> findByUserIdAndReportMonth(
            String userId,
            LocalDate reportMonth
    );

    List<ProgressAnalysis> findByUserIdOrderByReportMonthDesc(String userId);
}