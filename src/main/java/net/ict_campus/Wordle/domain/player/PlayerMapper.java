package net.ict_campus.Wordle.domain.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final PlayerRepository playerRepository;

    public PlayerDTO toDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setIdPlayer(player.getId_player());
        dto.setUsername(player.getUsername());
        dto.setScore(player.getScore());

        return dto;
    }

    public Player toEntity(PlayerDTO dto) {
        Player player = new Player();
        if (dto.getIdPlayer() != null) {
            player.setId_player(dto.getIdPlayer());
        }
        player.setUsername(dto.getUsername());
        player.setScore(player.getScore());

        return player;
    }
}
