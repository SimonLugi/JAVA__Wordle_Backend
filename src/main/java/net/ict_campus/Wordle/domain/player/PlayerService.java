package net.ict_campus.Wordle.domain.player;

import lombok.RequiredArgsConstructor;
import net.ict_campus.Wordle.domain.errorHandling.LoggerService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {


    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final LoggerService LOGGER;


    public List<PlayerDTO> getPlayers() {
        List<Player> players = playerRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));

        List<PlayerDTO> playerDTOs = players.stream().map(playerMapper::toDTO).toList();

        LOGGER.info("User requested all Players: {}", playerDTOs);
        return playerDTOs;
    }

    public PlayerDTO getPlayerById(Long id) {
        Player player = playerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Player with id " + id + " could not be found"));
        LOGGER.info("User requested player with id {}: {}", id, player);
        return playerMapper.toDTO(player);
    }

    public PlayerDTO getPlayerByUsername(String username){
        Player player = playerRepository.findByUsername(username);
        LOGGER.info("User requested player with username {}: {}", username, player);
        return playerMapper.toDTO(player);
    }

    public int getScoreByPlayerId(Long id) {
        Player player = playerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Player with id " + id + " could not be found"));
        return player.getScore();
    }

    public void setScoreByPlayerId(Long id, int score) {
        Player player = playerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Player with id " + id + " could not be found"));
        player.setScore(score);
        playerRepository.save(player);
    }

    public void setScoreByPlayerUsername(String username, int score){
        Player player = playerRepository.findByUsername(username);
        player.setScore(score);
        playerRepository.save(player);
    }

    public void updatePlayer(PlayerDTO dto) {
        Player player = playerMapper.toEntity(dto);
        LOGGER.info("User created/updated player: {}", player);

        // Fetch old player once
        Optional<Player> oldRegiOpt = playerRepository.findById(Math.toIntExact(dto.getIdPlayer()));
        if (oldRegiOpt.isPresent()) {
            Player currentPlayer = oldRegiOpt.get();
            currentPlayer.setScore(player.getScore());
            playerRepository.save(currentPlayer);
        }
    }
}
