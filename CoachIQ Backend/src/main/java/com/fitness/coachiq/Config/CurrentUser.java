package com.fitness.coachiq.Config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    public String id(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) throw new IllegalStateException("Authentication required");
        return authentication.getName();
    }
}
