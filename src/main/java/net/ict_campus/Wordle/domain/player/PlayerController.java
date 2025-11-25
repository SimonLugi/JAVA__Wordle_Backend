package net.ict_campus.Wordle.domain.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ict_campus.Wordle.domain.score.Score;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerDTO> getPlayers() {
        return this.playerService.getPlayers();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDTO getPlayerById(@PathVariable Long id) {
        return this.playerService.getPlayerById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDTO getPlayerById(@PathVariable String username) {
        return this.playerService.getPlayerByUsername(username);
    }

    @PutMapping("/{id}/score")
    @ResponseStatus(HttpStatus.OK)
    public void setPlayerScore(@PathVariable Long id, @RequestBody Score request) {
        this.playerService.setScoreByPlayerId(id, request.score);
    }

    @PutMapping("/username/{username}/score")
    @ResponseStatus(HttpStatus.CREATED)
    public void setPlayerScore(@PathVariable String  username, @RequestBody Score request) {
        this.playerService.setScoreByPlayerUsername(username, request.score);
    }

    @GetMapping("/{id}/score")
    @ResponseStatus(HttpStatus.OK)
    public int getPlayerScore(@PathVariable Long id) {
        return this.playerService.getScoreByPlayerId(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePlayer(@Valid @RequestBody PlayerDTO dto) {
        this.playerService.updatePlayer(dto);
    }
}
