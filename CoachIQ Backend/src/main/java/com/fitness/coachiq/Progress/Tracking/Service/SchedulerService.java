package com.fitness.coachiq.Progress.Tracking.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final ProgressService progressService;


    // Every month on the last day at 12:00 AM
    @Scheduled(cron = "0 40 * * * *")
    public void runMonthlyAnalysis() {

        log.info("Starting monthly analysis scheduler...");

        progressService.generateMonthlyAnalysis();

        log.info("Monthly analysis scheduler finished.");
    }
}