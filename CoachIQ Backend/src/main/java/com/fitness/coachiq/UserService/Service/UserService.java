
package com.fitness.coachiq.UserService.Service;
import com.fitness.coachiq.UserService.DTO.LoginRequest;
import com.fitness.coachiq.UserService.DTO.LoginResponse;
import com.fitness.coachiq.UserService.DTO.RegisterRequest;
import com.fitness.coachiq.UserService.DTO.UserResponse;
import com.fitness.coachiq.UserService.Entity.Users;
import com.fitness.coachiq.UserService.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final KeycloakUserService keycloakUserService;
    private final KeycloakAuthService keycloakAuthService;


    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        String keycloakId = keycloakUserService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstname(),
                request.getLastname()
        );

        try {

            Users user = new Users();
            user.setEmail(request.getEmail());
            user.setKeycloakId(keycloakId);
            user.setFirstName(request.getFirstname());
            user.setLastName(request.getLastname());

            Users savedUser = repository.save(user);

            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setKeycloakId(savedUser.getKeycloakId());
            response.setEmail(savedUser.getEmail());
            response.setFirstName(savedUser.getFirstName());
            response.setLastName(savedUser.getLastName());
            response.setCreatedAt(savedUser.getCreatedAt());
            response.setUpdatedAt(savedUser.getUpdatedAt());

            return response;

        } catch (Exception e) {

            // Roll back Keycloak user if database save fails
            keycloakUserService.deleteUser(keycloakId);
            throw e;
        }
    }

    public UserResponse getUserProfile(String userId) {
        log.info("Received userId = {}", userId);
        Users user = repository.findByKeycloakId(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    public String getUserEmail(String userId) {

        Users user = repository.findByKeycloakId(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return user.getEmail();
    }

    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return repository.existsByKeycloakId(userId);
    }

    public List<String> getAllUserIds() {
        return repository.findAll()
                .stream()
                .map(Users::getKeycloakId)
                .toList();
    }

    public LoginResponse login(LoginRequest request) {
        return keycloakAuthService.login(request);
    }
}