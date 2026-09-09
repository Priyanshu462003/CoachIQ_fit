package com.fitness.coachiq.AiService.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.coachiq.AiService.DTO.MonthlyProgressRequest;
import com.fitness.coachiq.AiService.DTO.response.ProgressAnalysisDto;
import com.fitness.coachiq.AiService.prompt.ProgressPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressAnalysisService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    private final ProgressPrompt progressPrompt;

    public ProgressAnalysisDto generateAnalysis(MonthlyProgressRequest request) {

        try {

            String prompt = progressPrompt.buildPrompt(request);

            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            ProgressAnalysisDto analysis =
                    objectMapper.readValue(aiResponse, ProgressAnalysisDto.class);

            analysis.setUserId(request.getUserId());

            log.info("Monthly Progress Analysis generated for user {}", request.getUserId());
            return analysis;

        } catch (Exception e) {

            log.error("Failed to generate monthly progress analysis for user: {}", request.getUserId(), e);
            throw new RuntimeException("Failed to generate monthly progress analysis", e);
        }
    }
}