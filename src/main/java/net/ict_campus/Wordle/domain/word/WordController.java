package net.ict_campus.Wordle.domain.word;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/word")
public class WordController {

    private final WordService wordService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Word> getAllWords() {
        return this.wordService.getWords();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getWordById(@PathVariable int id){
        return this.wordService.getWordById(id);
    }
}
