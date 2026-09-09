package com.fitness.coachiq.UserService.Controller;

import com.fitness.coachiq.UserService.DTO.LoginRequest;
import com.fitness.coachiq.UserService.DTO.LoginResponse;
import com.fitness.coachiq.UserService.DTO.RegisterRequest;
import com.fitness.coachiq.UserService.DTO.UserResponse;
import com.fitness.coachiq.UserService.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.fitness.coachiq.Config.CurrentUser;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
        name = "User Service",
        description = "Endpoints for user registration, authentication, and profile management."
)
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    @Operation(
            summary = "Get User Profile",
            description = "Returns the profile of a user."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(
            @PathVariable String userId, Authentication authentication) {
        String currentUserId = currentUser.id(authentication);
        if (!currentUserId.equals(userId)) throw new org.springframework.security.access.AccessDeniedException("You can only access your own profile");
        return ResponseEntity.ok(userService.getUserProfile(currentUserId));
    }

    @Operation(
            summary = "Register User",
            description = "Registers a new user."
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                userService.register(request)
        );
    }

    @Operation(
            summary = "Get User Email",
            description = "Returns the email address of a user."
    )
    @GetMapping("/{userId}/email")
    public ResponseEntity<String> getUserEmail(
            @PathVariable String userId, Authentication authentication) {
        String currentUserId = currentUser.id(authentication);
        if (!currentUserId.equals(userId)) throw new org.springframework.security.access.AccessDeniedException("You can only access your own profile");
        return ResponseEntity.ok(userService.getUserEmail(currentUserId));
    }



    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns the login response."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                userService.login(request)
        );
    }

}
