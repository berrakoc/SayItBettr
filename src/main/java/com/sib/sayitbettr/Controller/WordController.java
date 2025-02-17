package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // @RestController değil!
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

    // IDye göre sıralıyor ve word.html sayfasına yönlendiriyor
    @GetMapping("/{id}")
    public String getWordById(@PathVariable Long id, Model model) {
        Word word = wordService.getWordById(id);
        model.addAttribute("word", word); // Thymeleaf
        return "word"; // word.html döndür
    }
}
