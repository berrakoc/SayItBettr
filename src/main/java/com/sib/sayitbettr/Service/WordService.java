package com.sib.sayitbettr.Service;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
}

