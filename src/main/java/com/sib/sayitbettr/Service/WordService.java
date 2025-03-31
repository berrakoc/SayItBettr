package com.sib.sayitbettr.Service;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Repository.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getWordsByLevel(int level) {
        return wordRepository.findByLevel(level);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElse(null);
    }

    public Long countTotalWords() {
        return wordRepository.countTotalWords();
    }

    public Long countWordsByLevel(int level) {
        return wordRepository.countWordsByLevel(level);
    }

}

