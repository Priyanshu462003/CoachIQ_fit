package com.fitness.coachiq.UserService.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createUser(String email,
                             String password,
                             String firstName,
                             String lastName) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        try (Response response = keycloak
                .realm(realm)
                .users()
                .create(user)) {

            if (response.getStatus() != 201) {
                throw new RuntimeException(
                        "Failed to create user in Keycloak. Status: "
                                + response.getStatus()
                );
            }

            return CreatedResponseUtil.getCreatedId(response);
        }
    }

    public void deleteUser(String userId) {

        keycloak.realm(realm)
                .users()
                .delete(userId);
    }
}