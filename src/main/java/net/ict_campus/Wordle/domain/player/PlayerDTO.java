package net.ict_campus.Wordle.domain.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {

    private Integer idPlayer;

    @NotBlank(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "Field must be between 2 and 20 characters long.")
    private String username;

    private int score;
}
