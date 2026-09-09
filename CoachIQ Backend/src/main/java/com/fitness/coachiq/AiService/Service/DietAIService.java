package com.fitness.coachiq.AiService.Service;

import com.fitness.coachiq.AiService.DTO.DietRequest;
import com.fitness.coachiq.AiService.DTO.response.DietDto;
import com.fitness.coachiq.AiService.prompt.DietPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietAIService {

    private final ChatClient chatClient;
    private final DietPromptBuilder promptBuilder;

    public DietDto generateDiet(DietRequest request) {

        log.info("Generating diet plan for user: {}", request.getUserId());

        try {

            String prompt = promptBuilder.build(request);

            DietDto dietDto = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(DietDto.class);

            if (dietDto == null) {
                throw new RuntimeException("AI returned empty diet");
            }

            if (dietDto.getDays() == null || dietDto.getDays().isEmpty()) {
                throw new RuntimeException("AI returned diet without days");
            }

            // These values should always come from the authenticated/request user
            dietDto.setUserId(request.getUserId());
            dietDto.setGoal(request.getGoal());

            log.info(
                    "Diet generated successfully for user: {}",
                    request.getUserId()
            );

            return dietDto;

        } catch (Exception e) {

            log.error(
                    "Diet generation failed for user: {}",
                    request.getUserId(),
                    e
            );

            throw new RuntimeException(
                    "Unable to generate diet for user: "
                            + request.getUserId(),
                    e
            );
        }
    }
}