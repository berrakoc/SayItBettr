package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Service.UserService;
import com.sib.sayitbettr.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser( @RequestBody User user){ //gelen JSON'ı User nesnesine dönüştürdük
        try{
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser( @RequestBody Map<String, String> loginRequest, HttpSession session){
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<User> user = userService.authenticateAndGetUser(email, password);

        if(user.isPresent()){
            session.setAttribute("loggedUser", user.get());
            return ResponseEntity.ok("Giriş başarılı!"); //"redirect:/home";
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı mail veya şifre!");
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            return ResponseEntity.ok(loggedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
