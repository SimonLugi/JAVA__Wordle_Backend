package net.ict_campus.Wordle.security.services;

import net.ict_campus.Wordle.domain.player.Player;
import net.ict_campus.Wordle.domain.player.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class PlayerDetailsServiceImpl implements UserDetailsService {

    private final PlayerRepository playerRepository;


    public PlayerDetailsServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(player.getUsername(), player.getPassword(), emptyList());
    }
}
