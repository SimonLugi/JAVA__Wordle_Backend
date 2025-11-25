package net.ict_campus.Wordle.security.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.ict_campus.Wordle.domain.player.Player;
import net.ict_campus.Wordle.domain.player.PlayerRepository;
import net.ict_campus.Wordle.security.auth.Jwt;
import net.ict_campus.Wordle.security.dto.AuthPlayerDTO;
import net.ict_campus.Wordle.security.security.JWTService;
import net.ict_campus.Wordle.security.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthenticationController", description = "Processes /auth endpoint requests")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JWTService JWTService;
    private final AuthenticationService authenticationService;
    private final PlayerRepository playerRepository;

    public AuthenticationController(JWTService JWTService, AuthenticationService authenticationService, PlayerRepository playerRepository) {
        this.JWTService = JWTService;
        this.authenticationService = authenticationService;
        this.playerRepository = playerRepository;
    }


    @Operation(summary = "Create a new User", description = "Registers a new user in the database.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Player.class))}),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid request data",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = "application/json")
    public @ResponseBody Player register(@Valid @RequestBody AuthPlayerDTO authPlayerDTO) {
        try {
            return authenticationService.signup(authPlayerDTO);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid request data");
        }
    }


    @Operation(summary = "Login user", description = "Authenticates a user and returns an access token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Jwt.class))}),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public @ResponseBody Jwt authenticate(@Valid @RequestBody AuthPlayerDTO authPlayerDTO) {
        try {
            return new Jwt()
                    .setToken("Bearer " + JWTService.generateToken(authenticationService.authenticate(authPlayerDTO)))
                    .setExpiresIn(JWTService.getExpirationTime())
                    .setScore(playerRepository.findByUsername(authenticationService.authenticate(authPlayerDTO).getUsername()).getScore());

        } catch (InternalAuthenticationServiceException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}