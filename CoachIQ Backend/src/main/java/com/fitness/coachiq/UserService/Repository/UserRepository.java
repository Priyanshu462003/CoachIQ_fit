package com.fitness.coachiq.UserService.Repository;
import com.fitness.coachiq.UserService.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {

    boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String userId);

    Users findByEmail(String email);

    Optional<Users> findByKeycloakId(String keycloakId);
}
