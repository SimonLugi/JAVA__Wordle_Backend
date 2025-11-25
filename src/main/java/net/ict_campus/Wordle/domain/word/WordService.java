package net.ict_campus.Wordle.domain.word;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public List<Word> getWords() {
        try {
            return wordRepository.findAll(Sort.by(Sort.Direction.ASC, "word"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load words", e);
        }
    }

    public String getWordById(int id) {
        try {
            return wordRepository.findById(id).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load word", e);
        }
    }
}
