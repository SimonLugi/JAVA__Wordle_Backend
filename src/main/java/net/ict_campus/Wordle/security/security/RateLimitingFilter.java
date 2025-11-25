package net.ict_campus.Wordle.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.ict_campus.Wordle.domain.errorHandling.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final LoggerService LOGGER;

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedIps = new ConcurrentHashMap<>(); // Store block time for IPs

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long BLOCK_TIME_MILLIS = 60000; // 3 minutes in milliseconds

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();

        if (blockedIps.containsKey(clientIp)) {
            long blockedUntil = blockedIps.get(clientIp);

            if (System.currentTimeMillis() > blockedUntil) {
                blockedIps.remove(clientIp);
                requestCounts.remove(clientIp);
                LOGGER.info("Unblocked ip: {}", clientIp);
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests - you are blocked for 3 minutes.");
                LOGGER.warn("Blocked ip: {}", clientIp);
                return;
            }
        }

        requestCounts.putIfAbsent(clientIp, 0);
        int requestCount = requestCounts.get(clientIp);


        if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
            blockedIps.put(clientIp, System.currentTimeMillis() + BLOCK_TIME_MILLIS);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - please try again later.");
            LOGGER.warn("Block until: {}", Instant.ofEpochMilli(blockedIps.get(clientIp)));
            return;
        }


        requestCounts.put(clientIp, requestCount + 1);
        filterChain.doFilter(request, response);
    }

    @Scheduled(fixedRate = 60000) //ms
    public void resetRequestCounts() {
        requestCounts.clear();
        LOGGER.info("Request counts reset.");
    }
}
