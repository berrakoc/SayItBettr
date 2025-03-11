package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private WordService wordService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }


    @GetMapping("/word-detail")
    public String wordDetailPage(@RequestParam Long id, Model model) {
        // id'ye göre notu veritabanından al
        Word word = wordService.getWordById(id);

        // Not bilgilerini model'e ekle
        model.addAttribute("word", word);
        return "word";
    }

}
