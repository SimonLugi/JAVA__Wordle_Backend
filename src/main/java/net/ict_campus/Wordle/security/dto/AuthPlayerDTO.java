package net.ict_campus.Wordle.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthPlayerDTO {

    @NotBlank(message = "username cannot be empty.")
    private String username;

    @NotBlank(message = "password cannot be empty.")
    @Size(min = 2, max = 255, message = "Password must be between 2 and 255 characters long")
    private String password;

    private int score;
}
