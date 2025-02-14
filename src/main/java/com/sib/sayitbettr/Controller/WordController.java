package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/level/{level}")
    public ResponseEntity<List<Word>> getWordsByLevel(@PathVariable int level) {
        return ResponseEntity.ok(wordService.getWordsByLevel(level));

    }
}

