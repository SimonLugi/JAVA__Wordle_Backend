package net.ict_campus.Wordle.security.services;

import jakarta.validation.Valid;
import net.ict_campus.Wordle.domain.player.Player;
import net.ict_campus.Wordle.domain.player.PlayerRepository;
import net.ict_campus.Wordle.security.dto.AuthPlayerDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PlayerRepository playerRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Creates the valid signup
    public Player signup(@Valid AuthPlayerDTO input) {
        if (playerRepository.findByUsername(input.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        Player player = new Player()
                .setUsername(input.getUsername())
                .setPassword(passwordEncoder.encode(input.getPassword()));
        return playerRepository.save(player);
    }

    //Checks against our Virtual data from the DTO the autendication
    public UserDetails authenticate(@Valid AuthPlayerDTO input) {
        if (playerRepository.findByUsername(input.getUsername()) == null){
            throw new IllegalArgumentException("The Username or Password is incorrect");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()));
        return playerRepository.findByUsername(input.getUsername());
    }

}
