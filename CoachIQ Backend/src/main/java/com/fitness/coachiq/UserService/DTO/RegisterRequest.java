package com.fitness.coachiq.UserService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message= "Enter a valid email")
    private String email;
    @NotBlank(message="password is required")
    @Size(min=6, message="Password must be atleast 6 characters")
    private String password;
    private String keycloakId;
    private String firstname;
    private String lastname;

}
