package com.fitness.coachiq.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "CoachIQ API", version = "1.0", description = "CoachIQ modular monolith REST API"))
public class OpenApiConfig {
}
