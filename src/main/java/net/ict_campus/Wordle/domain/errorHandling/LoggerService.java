package net.ict_campus.Wordle.domain.errorHandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    private final Logger logger = LoggerFactory.getLogger(LoggerService.class);

    public void info(String message, Object... args) {
        logger.info(colorize(GREEN, message), args);
    }

    public void error(String message, Object... args) {
        logger.error(colorize(RED, message), args);
    }

    public void warn(String message, Object... args) {
        logger.warn(colorize(YELLOW, message), args);
    }

    public void say(String message, Object... args) {
        logger.info(message, args);
    }

    private String colorize(String color, String message) {
        return color + message + RESET;
    }
}
