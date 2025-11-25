package net.ict_campus.Wordle.domain.word;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

@Entity
@Table(name = "word")
@Getter
@Setter
public class Word{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_word;

    private String word;

}
