package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/word")
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    // zorluk seviyesine göre sıralıyor
    @GetMapping("/level/{level}")
    @ResponseBody // JSON dönsün diye
    public List<Word> getWordsByLevel(@PathVariable int level) {
        return wordService.getWordsByLevel(level);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable Long id) {
        Word word = wordService.getWordById(id);

        if (word != null) {
            return ResponseEntity.ok(word);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count") //toplam kelime sayısı
    public ResponseEntity<Long> getTotalWordsCount() {
        return ResponseEntity.ok(wordService.countTotalWords());
    }

    @GetMapping("/level/{level}/count") //kelimeleri seviyelerine göre gruplama
    public ResponseEntity<Long> getWordsCountByLevel(@PathVariable int level) {
        return ResponseEntity.ok(wordService.countWordsByLevel(level));
    }
}
