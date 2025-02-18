package com.sib.sayitbettr.Controller;
import com.sib.sayitbettr.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/audio")
public class AudioController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadAudio(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("wordName") String wordName,
            HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı oturumu bulunamadı.");
        }

        String userId = String.valueOf(loggedUser.getId()); // Kullanıcı ID'sini al
        String fileName = userId + "_" + wordName + ".wav";

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, audioFile.getBytes());

            return ResponseEntity.ok("Ses başarıyla kaydedildi: " + filePath.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ses kaydedilemedi.");
        }
    }

}

