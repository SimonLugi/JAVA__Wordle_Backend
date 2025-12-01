package net.ict_campus.Wordle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages =  {"net.ict_campus.Wordle.domain.player","net.ict_campus.Wordle.domain.word"})
@EntityScan(basePackages = "net.ict_campus.Wordle.domain")
public class WordleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordleApplication.class, args);
    }
}
