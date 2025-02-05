package com.sib.sayitbettr.pageController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class pageController {


    @GetMapping("/index")
    public String index() {
        return "index";
    }


}
