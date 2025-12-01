package net.ict_campus.Wordle.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.ict_campus.Wordle.domain.player.Player;
import net.ict_campus.Wordle.domain.player.PlayerRepository;
import net.ict_campus.Wordle.security.services.PlayerDetailsServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    String firstUserSecretKey = System.getenv("FIRST_USER_SECRET_KEY") != null
            ? System.getenv("FIRST_USER_SECRET_KEY")
            : "NotConfigured";
    private final JWTService jwtService;
    private final PlayerDetailsServiceImpl playerDetailsService;
    private final PlayerRepository playerRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        //Checks if a valid format of a token is there, if not calls the next filter in the chain
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ") &&
                        !authorizationHeader.startsWith(firstUserSecretKey)
        ) {
            filterChain.doFilter(request, response);
            //We don't want to continue with the rest
            return;
        }

        //Could be commented in if signup should be "Admin / Existing user onely" (For creating first user)
        if (false /*authorizationHeader.startsWith(firstUserSecretKey)*/) {
            // If no players exist yet, create the first dummy player user
            if (playerRepository.count() == 0) {
                Player dummyUser = new Player();
                dummyUser.setUsername("chefchoch");
                dummyUser.setPassword("chefchoch12345");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        dummyUser,
                        dummyUser.getPassword(),
                        dummyUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // Continue the filter chain after setting authentication
                filterChain.doFilter(request, response);
                return; // Exit early, no need to process further filters
            }
        }


        if (authorizationHeader.startsWith("Bearer ")) {
            //"Bearer " has a length of seven
            jwt = authorizationHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            //If username exists and user isn't already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails playerDetails = this.playerDetailsService.loadUserByUsername(username); /*Load player*/
                //If token is up to date
                if (jwtService.isTokenValid(jwt, playerDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(playerDetails, null, playerDetails.getAuthorities());
                    //Add some more details from the request
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Update SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
